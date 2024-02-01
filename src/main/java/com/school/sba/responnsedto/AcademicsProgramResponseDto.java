package com.school.sba.responnsedto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.school.sba.enums.ProgramType;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AcademicsProgramResponseDto {
	private int programId;
	private ProgramType programType;
	private String programName;
	private LocalDate beginsAt;
	private LocalDate endsAt;
	private boolean isDeleted;
	private List<String> subjects;
}
