package com.example.slabiak.appointmentscheduler.model;

import com.example.slabiak.appointmentscheduler.validation.CurrentPasswordMatches;
import com.example.slabiak.appointmentscheduler.validation.FieldsMatches;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@FieldsMatches(	field = "password", 
				matchingField = "matchingPassword")
@CurrentPasswordMatches()
@Data
@NoArgsConstructor
public class ChangePasswordForm {

	@NotNull
	private int id;

	@Size(	min = 5, max = 10,
			message = "Password should have 5-15 letters")
	@NotBlank()
	private String password;

	@Size(	min = 5, max = 10,
			message = "Password should have 5-15 letters")
	@NotBlank()
	private String matchingPassword;

	private String currentPassword;

	public ChangePasswordForm(int id) {
		this.id = id;
	}
}