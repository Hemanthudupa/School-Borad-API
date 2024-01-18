package com.school.sba.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class SchoolAlreadyPresentException extends RuntimeException {
	String message;
}
