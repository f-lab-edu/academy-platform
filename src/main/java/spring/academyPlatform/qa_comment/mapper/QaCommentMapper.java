package spring.academyPlatform.qa_comment.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import spring.academyPlatform.qa_comment.domain.QaComment;
import spring.academyPlatform.qa_comment.dto.QaCommentResponse;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface QaCommentMapper {

	QaCommentResponse change(QaComment qaComment);

	List<QaCommentResponse> toDto(List<QaComment> qaComment);

}
