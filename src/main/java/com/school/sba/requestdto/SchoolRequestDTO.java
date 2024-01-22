package com.school.sba.requestdto;

import com.school.sba.entity.Schedule;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class SchoolRequestDTO {
	private String schoolName;
	private long contactNo;
	private String emailId;
	private String address;
	private Schedule schedule;
}
