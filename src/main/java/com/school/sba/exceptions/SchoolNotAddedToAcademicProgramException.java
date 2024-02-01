package com.school.sba.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SchoolNotAddedToAcademicProgramException extends RuntimeException {
	private String message;
}
