package spring.academyPlatform.qa_comment.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import io.lettuce.core.dynamic.annotation.Param;
import spring.academyPlatform.global.model.YnCode;
import spring.academyPlatform.qa_comment.domain.QaComment;

public interface QaCommentRepository extends JpaRepository<QaComment, Long> {

	@Query("SELECT MAX(c.priorityNumber) FROM QaComment c WHERE c.parentsCommentId = :parentCommentId AND c.deletedYn = :deletedYn")
	Long findMaxPriorityByParentCommentId(@Param("parentCommentId") Long parentCommentId,
		@Param("deletedYn") YnCode deletedYn);

	@Query("SELECT MAX(c.priorityNumber) FROM QaComment c WHERE c.boardId = :boardId AND c.parentsCommentId IS NULL AND c.deletedYn = :deletedYn")
	Long findMaxPriorityByBoardIdAndParentsCommentIdIsNull(@Param("boardId") Long boardId,
		@Param("deletedYn") YnCode deletedYn);

	Optional<QaComment> findByCommentIdAndDeletedYn(Long commentId, YnCode deletedYn);

	List<QaComment> findByParentsCommentIdAndDeletedYn(Long commentId, YnCode ynCode);

	List<QaComment> findByBoardIdAndDeletedYn(Long boardId, YnCode deletedYn);
}
