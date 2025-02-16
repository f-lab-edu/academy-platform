package spring.academyPlatform.global.util;

import java.util.List;

import org.springframework.data.domain.Pageable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomPage<T> {

	private List<T> content;
	private int pageNumber;
	private int pageSize;
	private long totalElements;
	private int totalPages;
	private boolean isLast;

	public CustomPage(List<T> content, Pageable pageable, long totalElements, int internalPage) {
		this.content = content;
		this.pageNumber = internalPage; // 사용자 요청 페이지 번호를 반환
		this.pageSize = pageable.getPageSize();
		this.totalElements = totalElements;
		this.totalPages = (int)Math.ceil((double)totalElements / pageSize);
		this.isLast = (internalPage == totalPages); // 마지막 페이지 여부
	}
}
