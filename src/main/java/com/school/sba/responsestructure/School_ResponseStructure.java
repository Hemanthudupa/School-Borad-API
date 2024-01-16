package com.school.sba.responsestructure;

import lombok.Data;

@Data
public class School_ResponseStructure<T> {
	private String message;
	private int status;
	private T data;

}
