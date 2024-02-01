package com.school.sba.requestdto;

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