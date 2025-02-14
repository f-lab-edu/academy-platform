package spring.academyPlatform.history.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import spring.academyPlatform.history.domain.History;
import spring.academyPlatform.history.domain.QHistory;

@Repository
@RequiredArgsConstructor
public class HistoryCustomRepositoryImpl implements HistoryCustomRepository {

	QHistory history = QHistory.history;
	private final JPAQueryFactory queryFactory;

	@Override
	public List<History> readHistory(String tableName, String operationType, LocalDateTime startDate,
		LocalDateTime endDate, String createBy) {
		return queryFactory.selectFrom(history)
			.where(eqTableName(tableName), eqOperationType(operationType), eqCreateBy(createBy),
				history.createdAt.between(startDate, endDate))
			.orderBy(history.createdAt.desc())
			.fetch();
	}

	private BooleanExpression eqTableName(String tableName) {
		if (StringUtils.hasText(tableName)) {
			return history.tableName.eq(tableName); // 값이 유효하면 eq() 사용
		}
		return null; // 값이 없으면 조건을 포함하지 않음
	}

	private BooleanExpression eqOperationType(String operationType) {
		if (StringUtils.hasText(operationType)) {
			return history.operationType.eq(operationType); // 값이 유효하면 eq() 사용
		}
		return null; // 값이 없으면 조건을 포함하지 않음
	}

	private BooleanExpression eqCreateBy(String createBy) {
		if (StringUtils.hasText(createBy)) {
			return history.createdBy.eq(createBy); // 값이 유효하면 eq() 사용
		}
		return null; // 값이 없으면 조건을 포함하지 않음
	}

}
