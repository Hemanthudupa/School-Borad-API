package com.school.sba.requestdto;

import java.time.LocalDateTime;

import com.school.sba.enums.ClassStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClassHourRequestDTO {
	private int roomNo;
	private int subjectId;
	private int userId;
	private int classHourId;
}