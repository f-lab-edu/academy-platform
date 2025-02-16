package spring.academyPlatform.qa_board.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import spring.academyPlatform.global.model.YnCode;
import spring.academyPlatform.global.util.CustomPage;
import spring.academyPlatform.qa_board.domain.QQaBoard;
import spring.academyPlatform.qa_board.domain.QaBoard;
import spring.academyPlatform.qa_board.dto.QaBoardChangeResponse;
import spring.academyPlatform.qa_board.dto.QaBoardSearchResponse;
import spring.academyPlatform.qa_board.mapper.QaBoardMapper;
import spring.academyPlatform.qa_comment.domain.QQaComment;
import spring.academyPlatform.qa_comment.domain.QaComment;
import spring.academyPlatform.qa_comment.dto.QaCommentResponse;
import spring.academyPlatform.qa_comment.mapper.QaCommentMapper;

@Repository
@RequiredArgsConstructor
public class QaBoardRepositoryImpl implements QaBoardCustomRepository {

	private final JPAQueryFactory queryFactory;
	QQaBoard qaBoard = QQaBoard.qaBoard;
	QQaComment qaComment = QQaComment.qaComment;
	private final QaBoardMapper qaBoardMapper;
	private final QaCommentMapper qaCommentMapper;

	@Override
	public CustomPage<QaBoardSearchResponse> findBoard(Long boardId, String title, String userId,
		LocalDateTime startDate,
		LocalDateTime endDate,
		int page, int size) {
		// 실제 로직에서는 0부터 시작하도록
		int internalPage = page - 1;

		// 0 이하의 값이 입력되었을 경우 기본값(0) 처리
		if (internalPage < 0)
			internalPage = 0;

		Pageable pageable = PageRequest.of(internalPage, size, Sort.by("created_at").descending());

		List<QaBoard> content = queryFactory
			.selectDistinct(qaBoard)
			.from(qaBoard)
			.where(likeTitle(title), eqBoardId(boardId), betweenDate(startDate, endDate), eqUserId(userId),
				qaBoard.deletedYn.eq(YnCode.N))
			.orderBy(qaBoard.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		List<QaBoardChangeResponse> dtoContent = qaBoardMapper.toChange(content);

		List<Long> boardIds = content.stream()
			.map(QaBoard::getBoardId)
			.toList();

		if (!boardIds.isEmpty()) {
			// 3. 해당 boardIds에 속하는 Comment를 한 번의 쿼리로 조회
			List<QaComment> comments = queryFactory
				.selectFrom(qaComment)
				.where(
					qaComment.boardId.in(boardIds)
						.and(qaComment.deletedYn.eq(YnCode.N))
				)
				.fetch();

			List<QaCommentResponse> dto = qaCommentMapper.toDto(comments);

			// 4. boardId별로 Comment를 그룹핑
			Map<Long, List<QaCommentResponse>> commentMap = dto.stream()
				.collect(Collectors.groupingBy(QaCommentResponse::getBoardId));

			// 5. 각 Board 엔티티에 댓글 리스트 설정 (컬렉션 초기화)
			dtoContent.forEach(qaBoardDto ->
				qaBoardDto.getComments()
					.addAll(commentMap.getOrDefault(qaBoardDto.getBoardId(), List.of())));

		}

		Long total = queryFactory
			.select(qaBoard.count())
			.where(likeTitle(title), eqBoardId(boardId), betweenDate(startDate, endDate), eqUserId(userId),
				qaBoard.deletedYn.eq(YnCode.N))
			.from(qaBoard)
			.fetchOne();

		List<QaBoardSearchResponse> result = qaBoardMapper.change(content);

		return new CustomPage<>(result, pageable, total, internalPage + 1);
	}

	private BooleanExpression likeTitle(String title) {
		if (StringUtils.hasText(title)) {
			return qaBoard.title.like(title); // 값이 유효하면 eq() 사용
		}
		return null; // 값이 없으면 조건을 포함하지 않음
	}

	private BooleanExpression eqBoardId(Long boardId) {
		if (boardId != null) {
			return qaBoard.boardId.eq(boardId); // 값이 유효하면 eq() 사용
		}
		return null; // 값이 없으면 조건을 포함하지 않음
	}

	private BooleanExpression eqUserId(String userId) {
		if (StringUtils.hasText(userId)) {
			return qaBoard.userId.eq(userId); // 값이 유효하면 eq() 사용
		}
		return null; // 값이 없으면 조건을 포함하지 않음
	}

	private BooleanExpression betweenDate(LocalDateTime start, LocalDateTime end) {
		if (start != null && end != null) {
			return qaBoard.createdAt.between(start, end);
		}
		return null;
	}

}
