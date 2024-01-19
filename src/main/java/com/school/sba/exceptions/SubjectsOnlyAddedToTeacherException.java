package com.school.sba.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SubjectsOnlyAddedToTeacherException extends RuntimeException {
	private String message;
}
