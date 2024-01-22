package com.school.sba.service;

import org.springframework.http.ResponseEntity;

import com.school.sba.requestdto.ScheduleRequestDTO;
import com.school.sba.responnsedto.ScheduleResponseDTO;
import com.school.sba.util.ResponseStructure;

public interface ScheduleService {

	ResponseEntity<ResponseStructure<ScheduleResponseDTO>> saveSchedule(int schoolId,
			ScheduleRequestDTO scheduleRequestDTO);

	ResponseEntity<ResponseStructure<ScheduleResponseDTO>> findScheduleBySchool(int schoolId);

	ResponseEntity<ResponseStructure<ScheduleResponseDTO>> updateSchedule(int scheduleId,
			ScheduleRequestDTO scheduleRequestDTO);

}
