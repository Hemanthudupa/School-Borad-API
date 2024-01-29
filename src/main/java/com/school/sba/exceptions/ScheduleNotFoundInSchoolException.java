package com.school.sba.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class ScheduleNotFoundInSchoolException extends RuntimeException {
	private String message;
}
