package com.school.sba.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SubjectNotFoundExceptionByID extends RuntimeException {
	private String message;
}
