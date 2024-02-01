package com.school.sba.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.school.sba.entity.AcademicProgram;
import com.school.sba.enums.UserRole;
import com.school.sba.exceptions.AcademicProgramNotFoundByIdException;
import com.school.sba.exceptions.AcademicsProgramIsNotPresentException;
import com.school.sba.exceptions.AdminCannotBeAddedToAcademicsProgramException;
import com.school.sba.exceptions.InvalidUserRoleException;
import com.school.sba.exceptions.NoSubjectFoundInAcademinException;
import com.school.sba.exceptions.SchoolNotFoundByIdException;
import com.school.sba.exceptions.UserNotFoundByIdException;
import com.school.sba.repository.AcademicProgramRepo;
import com.school.sba.repository.ClassHourRepo;
import com.school.sba.repository.SchoolRepo;
import com.school.sba.repository.UserRepo;
import com.school.sba.requestdto.AcademicProgramRequestDto;
import com.school.sba.responnsedto.AcademicsProgramResponseDto;
import com.school.sba.service.AcademicProgramService;
import com.school.sba.util.ResponseStructure;

@Service
public class AcademicProgramServiceImpl implements AcademicProgramService {

	@Autowired
	private AcademicProgramRepo academicProgramRepo;

	@Autowired
	private SchoolRepo schoolRepo;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private ClassHourRepo classHourRepo;

	@Autowired
	private ResponseStructure<AcademicsProgramResponseDto> structure;

	public AcademicProgram mapToAcademicProgram(AcademicProgramRequestDto academicProgramRequestDto) {
		return AcademicProgram.builder().beginsAt(academicProgramRequestDto.getBeginsAt())
				.endsAt(academicProgramRequestDto.getEndsAt()).programName(academicProgramRequestDto.getProgramName())
				.programType(academicProgramRequestDto.getProgramType()).build();

	}

	public AcademicsProgramResponseDto mapToAcademicsProgramResponseDto(AcademicProgram academicProgram) {
		List<String> names = new ArrayList<>();
		if (academicProgram.getSubjects() != null) {
			academicProgram.getSubjects().forEach(subject -> {
				names.add(subject.getSubjectName());
			});
		}

		return AcademicsProgramResponseDto.builder().programId(academicProgram.getProgramId())
				.beginsAt(academicProgram.getBeginsAt()).endsAt(academicProgram.getEndsAt())
				.programName(academicProgram.getProgramName()).programType(academicProgram.getProgramType())
				.subjects(names).isDeleted(academicProgram.isDeleted()).build();
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
			if (!school.getAcademicProgram().isEmpty()) {
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
			} else {
				throw new AcademicsProgramIsNotPresentException(" Academics not assigned to School!!!!");
			}
		}).orElseThrow(() -> new SchoolNotFoundByIdException("invalid school id"));
	}

	@Override
	public ResponseEntity<ResponseStructure<AcademicsProgramResponseDto>> assignUser(int programId, int userId) {

		return userRepo.findById(userId).map(user -> {
			if (user.getUserRole().toString().equals("ADMIN")) {
				throw new AdminCannotBeAddedToAcademicsProgramException("can't add admin");
			} else {
				academicProgramRepo.findById(programId).map(program -> {

					if (program.getUsers().contains(user) != true && user.getUserRole() == UserRole.TEACHER) {
//						program.getUsers().add(user);
						if (program.getSubjects().contains(user.getSubject())) {
							program.getUsers().add(user);
							academicProgramRepo.save(program);
						} else {
							throw new NoSubjectFoundInAcademinException("invlaid subject from the user ");
						}
					} else {
						throw new InvalidUserRoleException("only teacher allowed");
					}
					structure.setData(mapToAcademicsProgramResponseDto(program));
					structure.setMessage(" user added successfully");
					structure.setStatus(HttpStatus.OK.value());
					return new ResponseEntity<ResponseStructure<AcademicsProgramResponseDto>>(structure, HttpStatus.OK);
				}).orElseThrow(() -> new AcademicProgramNotFoundByIdException(" invalid academicprogram ID !!!"));
			}
			return new ResponseEntity<ResponseStructure<AcademicsProgramResponseDto>>(structure, HttpStatus.OK);

		}).orElseThrow(() -> new UserNotFoundByIdException("invalid user ID !!!"));
	}

	@Override
	public ResponseEntity<ResponseStructure<AcademicsProgramResponseDto>> deleteAcademicProgram(int programId) {
		return academicProgramRepo.findById(programId).map(program -> {
			if (!program.isDeleted()) {
				program.setDeleted(true);
				academicProgramRepo.save(program);
			}
			structure.setData(mapToAcademicsProgramResponseDto(program));
			structure.setMessage("academic program delete successfully");
			structure.setStatus(HttpStatus.CONTINUE.value());
			return new ResponseEntity<ResponseStructure<AcademicsProgramResponseDto>>(structure, HttpStatus.OK);
		}).orElseThrow(() -> new AcademicProgramNotFoundByIdException("invalid ID!!!"));
	}

	@Override
	public void permanentDeleteAP() {
		List<AcademicProgram> programs = academicProgramRepo.findAllByIsDeleted(true);
		programs.forEach(program -> classHourRepo.deleteAll(program.getClassHours()));
		academicProgramRepo.deleteAll(programs);
	}

}
