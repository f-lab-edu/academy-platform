package spring.academyPlatform.qa_comment.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.academyPlatform.global.model.YnCode;

@Entity
@Table(name = "qa_comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
public class QaComment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "comment_id")
	private Long commentId;

	@Column(name = "board_id")
	private Long boardId;

	// 사용자 정보
	@Column(name = "user_id", nullable = false)
	private String userId;

	// 부모 댓글의 ID (최상위 댓글이면 null)
	@Column(name = "parents_comment_id")
	private Long parentsCommentId;

	// 우선순위 번호 (정렬 등에 사용 가능)
	@Column(name = "priority_number")
	private Long priorityNumber;

	@Column(name = "title")
	private String title;

	@Column(name = "post")
	private String post;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "modified_by")
	private String modifiedBy;

	@Column(name = "created_at", updatable = false)
	@CreatedDate
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime createdAt;

	@Column(name = "modified_at", updatable = false)
	@LastModifiedDate
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime modifiedAt;

	// 삭제 여부 (Y/N)
	@Enumerated(EnumType.STRING)
	@Column(name = "deleted_yn")
	private YnCode deletedYn;

	@Builder(toBuilder = true)
	public QaComment(Long commentId, Long boardId, String userId, Long parentsCommentId, Long priorityNumber,
		String title,
		String post, String createdBy, String modifiedBy, LocalDateTime createdAt, LocalDateTime modifiedAt,
		YnCode deletedYn) {
		this.commentId = commentId;
		this.boardId = boardId;
		this.userId = userId;
		this.parentsCommentId = parentsCommentId;
		this.priorityNumber = priorityNumber;
		this.title = title;
		this.post = post;
		this.createdBy = createdBy;
		this.modifiedBy = modifiedBy;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
		this.deletedYn = (deletedYn == null) ? YnCode.N : deletedYn;
	}
}
