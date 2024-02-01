package com.school.sba.requestdto;

import java.time.DayOfWeek;

import com.school.sba.entity.Schedule;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class SchoolRequestDTO {
	private String schoolName;
	private long contactNo;
	private DayOfWeek dayOfWeek;
	private String emailId;
	private String address;
	private Schedule schedule;
}
