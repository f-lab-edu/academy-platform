package spring.academyPlatform.qa_board.dao;

import java.time.LocalDateTime;

import spring.academyPlatform.global.util.CustomPage;
import spring.academyPlatform.qa_board.dto.QaBoardChangeResponse;

public interface QaBoardCustomRepository {
	CustomPage<QaBoardChangeResponse> findBoard(Long boardId, String title, String userId, LocalDateTime startDate,
		LocalDateTime endDate,
		int page,
		int size);
}
