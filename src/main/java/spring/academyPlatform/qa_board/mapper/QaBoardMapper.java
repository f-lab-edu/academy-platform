package spring.academyPlatform.qa_board.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import spring.academyPlatform.qa_board.domain.QaBoard;
import spring.academyPlatform.qa_board.dto.QaBoardChangeResponse;
import spring.academyPlatform.qa_board.dto.QaBoardCreateRequest;
import spring.academyPlatform.qa_board.dto.QaBoardCreateResponse;
import spring.academyPlatform.qa_board.dto.QaBoardSearchResponse;
import spring.academyPlatform.qa_comment.mapper.QaCommentMapper;

/**
 * @Mapper를 이용하여 mapper임을 알려주고, MapStruct를 이용해 코드를 generate해야 함을 알려준다.
 * componentModel="spring"을 통해서 spring container에 Bean으로 등록 해 준다.
 * unmappedTargetPolicy IGNORE 만약, target class에 매핑되지 않는 필드가 있으면, null로 넣게 되고, 따로 report하지 않는다
 *
 */
@Mapper(componentModel = "spring", uses = {QaCommentMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface QaBoardMapper {

	QaBoardCreateResponse changeDto(QaBoard qaBoard);

	QaBoardCreateResponse changeDto(QaBoardCreateRequest qaBoardCreateRequest);

	QaBoard toEntity(QaBoardCreateResponse qaBoardCreateResponse);

	List<QaBoardSearchResponse> change(List<QaBoard> qaBoard);

	@Mapping(target = "comments", source = "comments")
	QaBoardChangeResponse createDto(QaBoard qaBoard);

	List<QaBoardChangeResponse> toChange(List<QaBoard> qaBoard);
}
