package com.school.sba.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.school.sba.entity.AcademicProgram;
import com.school.sba.exceptions.SchoolNotFoundByIdException;
import com.school.sba.repository.AcademicProgramRepo;
import com.school.sba.repository.School_Repo;
import com.school.sba.requestdto.AcademicProgramRequestDto;
import com.school.sba.responnsedto.AcademicsProgramResponseDto;
import com.school.sba.service.AcademicProgramService;
import com.school.sba.util.ResponseStructure;

@Service
public class AcademicProgramServiceImpl implements AcademicProgramService {

	@Autowired
	private AcademicProgramRepo academicProgramRepo;

	@Autowired
	private School_Repo schoolRepo;

	@Autowired
	private ResponseStructure<AcademicsProgramResponseDto> structure;

	public AcademicProgram mapToAcademicProgram(AcademicProgramRequestDto academicProgramRequestDto) {
		return AcademicProgram.builder().beginsAt(academicProgramRequestDto.getBeginsAt())
				.endsAt(academicProgramRequestDto.getEndsAt()).programName(academicProgramRequestDto.getProgramName())
				.programType(academicProgramRequestDto.getProgramType()).build();

	}

	public AcademicsProgramResponseDto mapToAcademicsProgramResponseDto(AcademicProgram academicProgram) {
		return AcademicsProgramResponseDto.builder().programId(academicProgram.getProgramId())
				.beginsAt(academicProgram.getBeginsAt()).endsAt(academicProgram.getEndsAt())
				.programName(academicProgram.getProgramName()).programType(academicProgram.getProgramType()).build();
	}

	@Override
	public ResponseEntity<ResponseStructure<AcademicsProgramResponseDto>> addAcademicsProgram(int schoolId,
			AcademicProgramRequestDto academicProgramRequestDto) {
		return schoolRepo.findById(schoolId).map(school -> {
			AcademicProgram mapToAcademicProgram = mapToAcademicProgram(academicProgramRequestDto);
			mapToAcademicProgram.setSchool(school);
			mapToAcademicProgram = academicProgramRepo.save(mapToAcademicProgram);
			school.getAcademicProgram().add(mapToAcademicProgram);
			schoolRepo.save(school);
			structure.setData(mapToAcademicsProgramResponseDto(mapToAcademicProgram));
			structure.setMessage("academics addedd !!!");
			structure.setStatus(HttpStatus.CREATED.value());
			return new ResponseEntity<ResponseStructure<AcademicsProgramResponseDto>>(structure, HttpStatus.CREATED);
		}).orElseThrow(() -> new SchoolNotFoundByIdException("Invalid School Id "));
	}

	@Override
	public ResponseEntity<ResponseStructure<List<AcademicsProgramResponseDto>>> getAllAcademicProgarm(int schoolId) {
		return schoolRepo.findById(schoolId).map(school -> {
			List<AcademicProgram> academicProgram = school.getAcademicProgram();
			ResponseStructure<List<AcademicsProgramResponseDto>> rs = new ResponseStructure<>();

			List<AcademicsProgramResponseDto> l = new ArrayList<>();
			for (AcademicProgram obj : academicProgram) {
				l.add(mapToAcademicsProgramResponseDto(obj));
			}
			rs.setData(l);
			rs.setMessage("successfully fetched ");
			rs.setStatus(HttpStatus.OK.value());
			return new ResponseEntity<ResponseStructure<List<AcademicsProgramResponseDto>>>(rs, HttpStatus.OK);
		}).orElseThrow(() -> new SchoolNotFoundByIdException("invalid school id"));
	}

}
