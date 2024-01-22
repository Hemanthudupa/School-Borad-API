package com.school.sba.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ScheduledAlreadyPresentException extends RuntimeException {
	String message;
}
