package spring.academyPlatform.qa_comment.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import spring.academyPlatform.global.model.YnCode;
import spring.academyPlatform.qa_comment.domain.QaComment;

public interface QaCommentRepository extends JpaRepository<QaComment, Long> {

	List<QaComment> findByParentsCommentIdAndDeletedYn(Long parentsCommentId, YnCode deletedYn);

	List<QaComment> findByBoardIdAndParentsCommentIdIsNullAndDeletedYn(Long BoardId, YnCode deletedYn);
}
