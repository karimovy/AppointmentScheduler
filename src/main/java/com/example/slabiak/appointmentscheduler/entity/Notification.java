package com.example.slabiak.appointmentscheduler.entity;

import com.example.slabiak.appointmentscheduler.entity.user.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Notification extends BaseEntity {

	@Column(name = "title")
	private String title;

	@Column(name = "message")
	private String message;

	@Column(name = "created_at")
	private Date createdAt;

	@Column(name = "url")
	private String url;

	@Column(name = "is_read")
	private boolean isRead;

	@ManyToOne
	@JoinColumn(name = "id_user")
	private User user;
}
