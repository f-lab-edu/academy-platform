package spring.academyPlatform.history.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import spring.academyPlatform.history.domain.History;

public interface HistoryRepository extends JpaRepository<History, Long>, HistoryCustomRepository {

}
