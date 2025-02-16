package spring.academyPlatform.qa_board.dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spring.academyPlatform.global.model.YnCode;
import spring.academyPlatform.global.util.CustomPage;
import spring.academyPlatform.qa_board.domain.QQaBoard;
import spring.academyPlatform.qa_board.domain.QaBoard;
import spring.academyPlatform.qa_board.dto.QaBoardChangeResponse;
import spring.academyPlatform.qa_board.mapper.QaBoardMapper;
import spring.academyPlatform.qa_comment.domain.QQaComment;
import spring.academyPlatform.qa_comment.domain.QaComment;
import spring.academyPlatform.qa_comment.dto.QaCommentResponse;
import spring.academyPlatform.qa_comment.mapper.QaCommentMapper;

@Repository
@RequiredArgsConstructor
@Slf4j
public class QaBoardRepositoryImpl implements QaBoardCustomRepository {

	private final JPAQueryFactory queryFactory;
	QQaBoard qaBoard = QQaBoard.qaBoard;
	QQaComment qaComment = QQaComment.qaComment;
	private final QaBoardMapper qaBoardMapper;
	private final QaCommentMapper qaCommentMapper;

	@Override
	public CustomPage<QaBoardChangeResponse> findBoard(Long boardId, String title, String userId,
		LocalDateTime startDate,
		LocalDateTime endDate,
		int page, int size) {
		// 1. 페이지 번호 조정 (0부터 시작)
		int internalPage = page - 1;
		if (internalPage < 0) {
			internalPage = 0;
		}
		Pageable pageable = PageRequest.of(internalPage, size, Sort.by("created_at").descending());

		// 2. 게시글 목록 조회
		List<QaBoard> content = queryFactory
			.selectDistinct(qaBoard)
			.from(qaBoard)
			.where(
				likeTitle(title),
				eqBoardId(boardId),
				betweenDate(startDate, endDate),
				eqUserId(userId),
				qaBoard.deletedYn.eq(YnCode.N)
			)
			.orderBy(qaBoard.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		// 3. 조회된 게시글을 DTO로 변환
		List<QaBoardChangeResponse> dtoContent = qaBoardMapper.toChange(content);

		// 4. 게시글의 boardId 추출
		List<Long> boardIds = dtoContent.stream()
			.map(QaBoardChangeResponse::getBoardId)
			.toList();

		// 5. 댓글 목록 조회 및 DTO 변환
		List<QaCommentResponse> commentDtoList = new ArrayList<>();
		if (!boardIds.isEmpty()) {
			List<QaComment> comments = queryFactory
				.selectFrom(qaComment)
				.where(
					qaComment.boardId.in(boardIds)
						.and(qaComment.deletedYn.eq(YnCode.N))
				)
				.fetch();
			commentDtoList = qaCommentMapper.toDto(comments);
		}

		// 6. boardId별로 댓글 그룹핑
		Map<Long, List<QaCommentResponse>> commentsByBoardId = commentDtoList.stream()
			.collect(Collectors.groupingBy(QaCommentResponse::getBoardId));

		// 7. 각 게시글별로 댓글 트리 구조 구성
		dtoContent.forEach(qaBoardDto -> {
			Long currentBoardId = qaBoardDto.getBoardId();

			// 해당 게시글의 모든 댓글 목록 (없으면 빈 리스트)
			List<QaCommentResponse> boardComments =
				commentsByBoardId.getOrDefault(currentBoardId, List.of());

			// 7-1. getCommentId 키로 하는 매핑 생성 (부모–자식 연결을 위해)
			Map<Long, QaCommentResponse> commentMap = boardComments.stream()
				.collect(Collectors.toMap(QaCommentResponse::getCommentId, Function.identity()));

			// 7-2. 모든 댓글에 대해 부모가 있으면, 부모의 children 리스트에 추가
			for (QaCommentResponse comment : boardComments) {
				Long parentId = comment.getParentsCommentId();
				if (parentId != null) {
					QaCommentResponse parent = commentMap.get(parentId);
					if (parent != null) {
						parent.getChildren().add(comment);
					}
				}
			}

			// 7-3. 부모가 없는(최상위) 댓글만 필터링
			List<QaCommentResponse> topLevelComments = boardComments.stream()
				.filter(comment -> comment.getParentsCommentId() == null)
				.toList();

			// 7-4. setter 대신 DTO 내부의 댓글 리스트를 가져와서 수정 (clear 후 addAll)
			qaBoardDto.getComments().clear();
			qaBoardDto.getComments().addAll(topLevelComments);
		});

		// 8. 전체 게시글 수 조회
		Long total = queryFactory
			.select(qaBoard.count())
			.where(
				likeTitle(title),
				eqBoardId(boardId),
				betweenDate(startDate, endDate),
				eqUserId(userId),
				qaBoard.deletedYn.eq(YnCode.N)
			)
			.from(qaBoard)
			.fetchOne();

		return new CustomPage<>(dtoContent, pageable, total, internalPage + 1);
	}

	private BooleanExpression likeTitle(String title) {
		if (StringUtils.hasText(title)) {
			return qaBoard.title.like(title);
		}
		return null; // 값이 없으면 조건을 포함하지 않음
	}

	private BooleanExpression eqBoardId(Long boardId) {
		if (boardId != null) {
			return qaBoard.boardId.eq(boardId);
		}
		return null;
	}

	private BooleanExpression eqUserId(String userId) {
		if (StringUtils.hasText(userId)) {
			return qaBoard.userId.eq(userId);
		}
		return null;
	}

	private BooleanExpression betweenDate(LocalDateTime start, LocalDateTime end) {
		if (start != null && end != null) {
			return qaBoard.createdAt.between(start, end);
		}
		return null;
	}

}
