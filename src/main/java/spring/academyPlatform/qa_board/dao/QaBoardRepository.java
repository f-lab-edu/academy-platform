package spring.academyPlatform.qa_board.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import spring.academyPlatform.qa_board.domain.QaBoard;

@Repository
public interface QaBoardRepository extends JpaRepository<QaBoard, Long> {

}
