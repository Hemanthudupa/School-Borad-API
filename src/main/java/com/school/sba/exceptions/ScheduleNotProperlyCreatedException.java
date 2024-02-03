package com.school.sba.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ScheduleNotProperlyCreatedException extends RuntimeException {
	private String message;
}