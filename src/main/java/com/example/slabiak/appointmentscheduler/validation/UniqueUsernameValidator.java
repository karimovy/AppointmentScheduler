package com.example.slabiak.appointmentscheduler.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.slabiak.appointmentscheduler.service.UserService;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, Object> {

	@Autowired
	private UserService userService;

	@Override
	public void initialize(final UniqueUsername constraintAnnotation) {
		//empty
	}

	@Override
	public boolean isValid(final Object obj, final ConstraintValidatorContext context) {
		String userName = (String) obj;
		return !userService.userExists(userName);
	}

}