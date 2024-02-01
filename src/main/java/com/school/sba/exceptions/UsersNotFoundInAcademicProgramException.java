package com.school.sba.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UsersNotFoundInAcademicProgramException extends RuntimeException {
	private String message;
}
