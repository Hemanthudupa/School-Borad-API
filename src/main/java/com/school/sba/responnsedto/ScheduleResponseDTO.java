package com.school.sba.responnsedto;

import java.time.LocalTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ScheduleResponseDTO {
	private int scheduleId;
	private LocalTime opensAt;
	private LocalTime closesAt;
	private int classHoursPerDay;
	private int classHourLengthInMinutes;
	private LocalTime breakTime;
	private int breakLengthInMinutes;
	private LocalTime lunchTime;
	private int lunchLengthInMinutes;
}
