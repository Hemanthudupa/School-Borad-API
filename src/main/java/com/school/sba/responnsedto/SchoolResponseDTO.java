package com.school.sba.responnsedto;

import java.time.DayOfWeek;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Builder
public class SchoolResponseDTO {
	private int schoolId;
	private String shoolName;
	private long contactNo;
	private DayOfWeek dayOfWeek;
	private String emailId;
	private String address;
	private boolean isDeleted;
}
