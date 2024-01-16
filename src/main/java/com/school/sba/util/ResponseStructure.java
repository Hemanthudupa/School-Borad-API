package com.school.sba.util;

import org.springframework.stereotype.Component;

import com.school.sba.entity.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Component
@AllArgsConstructor
@NoArgsConstructor
public class ResponseStructure<T> {
	private int status;
	private String message;
	private T data;

}
