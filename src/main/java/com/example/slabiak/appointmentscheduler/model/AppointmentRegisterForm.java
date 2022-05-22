package com.example.slabiak.appointmentscheduler.model;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor @AllArgsConstructor
public class AppointmentRegisterForm {

	private int workId;
	private int providerId;
	private int customerId;

	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime start;

	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime end;

	/**
	 * Custom Constructor
	 * 
	 * @param workId
	 * @param providerId
	 * @param start
	 * @param end
	 */
	public AppointmentRegisterForm(int workId, int providerId, LocalDateTime start, LocalDateTime end) {
		super();
		this.workId = workId;
		this.providerId = providerId;
		this.start = start;
		this.end = end;
	}

}