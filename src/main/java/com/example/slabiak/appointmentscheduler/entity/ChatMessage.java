package com.example.slabiak.appointmentscheduler.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.example.slabiak.appointmentscheduler.entity.user.User;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "MESSAGES")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ChatMessage extends BaseEntity implements Comparable<ChatMessage> {

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "message")
	private String message;

	@ManyToOne
	@JoinColumn(name = "id_author")
	private User author;

	@ManyToOne
	@JoinColumn(name = "id_appointment")
	private Appointment appointment;

	@Override
	public int compareTo(ChatMessage o) {
		return this.createdAt.compareTo(o.getCreatedAt());
	}
}
