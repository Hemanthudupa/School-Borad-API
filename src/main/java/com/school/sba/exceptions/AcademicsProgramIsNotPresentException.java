package com.school.sba.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AcademicsProgramIsNotPresentException extends RuntimeException {
	private String message;
}
