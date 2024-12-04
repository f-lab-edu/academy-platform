package spring.academyPlatform.history.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import spring.academyPlatform.history.domain.History;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long>, HistoryCustomRepository {

}
