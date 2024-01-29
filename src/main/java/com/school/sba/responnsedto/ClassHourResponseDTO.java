package com.school.sba.responnsedto;

import java.time.LocalDateTime;

import com.school.sba.entity.Subject;
import com.school.sba.enums.ClassStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ClassHourResponseDTO {
	private int classHourId;
	private LocalDateTime beginsAtLocalTime;
	private LocalDateTime endsALocalTime;
	private int roomNumber;
	private ClassStatus classStatus;
	private Subject subject;
}
