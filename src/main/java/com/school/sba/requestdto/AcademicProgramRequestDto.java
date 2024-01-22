package com.school.sba.requestdto;

import java.time.LocalTime;

import com.school.sba.enums.ProgramType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AcademicProgramRequestDto {
	private ProgramType programType;
	private String programName;
	private LocalTime beginsAt;
	private LocalTime endsAt;
}
