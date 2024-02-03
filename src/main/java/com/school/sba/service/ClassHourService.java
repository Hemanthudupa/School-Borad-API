package com.school.sba.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.school.sba.requestdto.ClassHourRequestDTO;
import com.school.sba.responnsedto.ClassHourResponseDTO;
import com.school.sba.util.ResponseStructure;

public interface ClassHourService {

	ResponseEntity<ResponseStructure<List<ClassHourResponseDTO>>> generateClassHour(int programId);

	ResponseEntity<ResponseStructure<List<ClassHourResponseDTO>>> updateClassHour(
			ArrayList<ClassHourRequestDTO> classHours);

//	ResponseEntity<ResponseStructure<List<ClassHourResponseDTO>>> generateClassHourForNextWeek(int programId);
	void generateClassHourForNextWeek(int programId);

}
