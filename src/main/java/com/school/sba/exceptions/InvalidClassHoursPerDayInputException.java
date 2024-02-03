package com.school.sba.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InvalidClassHoursPerDayInputException extends RuntimeException {
	private String message;
}
