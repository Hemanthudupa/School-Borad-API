package com.school.sba.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.school.sba.entity.Subject;
import com.school.sba.exceptions.AcademicProgramNotFoundByIdException;
import com.school.sba.repository.AcademicProgramRepo;
import com.school.sba.repository.SubjectRepo;
import com.school.sba.requestdto.SubjectRequestDTO;
import com.school.sba.responnsedto.AcademicsProgramResponseDto;
import com.school.sba.responnsedto.SubjectResponseDTO;
import com.school.sba.service.SubjectService;
import com.school.sba.util.ResponseStructure;

@Service
public class SubjectServiceImpl implements SubjectService {

	@Autowired
	private SubjectRepo subjectRepo;

	@Autowired
	private AcademicProgramRepo academicProgramRepo;

	@Autowired
	private AcademicProgramServiceImpl academicProgramServiceImpl;

	@Autowired
	private ResponseStructure<AcademicsProgramResponseDto> structure;

	public SubjectResponseDTO mapToSubjectResponseDTO(Subject subject) {
		return SubjectResponseDTO.builder().subjectId(subject.getSubjectId()).subjectName(subject.getSubjectName())
				.build();
	}

	@Override
	public ResponseEntity<ResponseStructure<AcademicsProgramResponseDto>> addSubjects(int programId,
			SubjectRequestDTO subjectRequestDTO) {

		return academicProgramRepo.findById(programId).map(program -> {
			List<Subject> subjects = program.getSubjects();
			subjectRequestDTO.getSubjectNames().forEach(name -> {
				subjectRepo.findBySubjectName(name).map(subject -> {
					if(subjects.contains(subject)==false)
					subjects.add(subject);
					return subject;
				}).orElseGet(() -> {
					Subject subject = new Subject();
					subject.setSubjectName(name);
					System.err.println("Creating new Subject");
					subject = subjectRepo.save(subject);
					subjects.add(subject);
					return subject;
				});
			});
			program.setSubjects(subjects);
			academicProgramRepo.save(program);
			structure.setMessage(" subjects added successfully !!! ");
			structure.setStatus(HttpStatus.OK.value());
			structure.setData(academicProgramServiceImpl.mapToAcademicsProgramResponseDto(program));
			return new ResponseEntity<ResponseStructure<AcademicsProgramResponseDto>>(structure, HttpStatus.OK);
		}).orElseThrow(() -> new AcademicProgramNotFoundByIdException(" invalid id "));

	}

	@Override
	public ResponseEntity<ResponseStructure<AcademicsProgramResponseDto>> updateSubjects(int programId,
			SubjectRequestDTO subjectRequestDTO) {
		List<String> subjectNames = subjectRequestDTO.getSubjectNames();

		return academicProgramRepo.findById(programId).map(program -> {
			program.getSubjects().clear();

			subjectNames.forEach(name -> {
				subjectRepo.findBySubjectName(name).map(sub -> {
					program.getSubjects().add(sub);
					academicProgramRepo.save(program);
					return program;
				}).orElseGet(() -> {
					Subject subject2 = new Subject();
					subject2.setSubjectName(name);
					subjectRepo.save(subject2);
					program.getSubjects().add(subject2);
					academicProgramRepo.save(program);
					return program;
				});
			});
			structure.setData(academicProgramServiceImpl.mapToAcademicsProgramResponseDto(program));
			structure.setMessage(" updated successfully");
			structure.setStatus(HttpStatus.OK.value());
			return new ResponseEntity<ResponseStructure<AcademicsProgramResponseDto>>(structure, HttpStatus.OK);
		}).orElseThrow(() -> new AcademicProgramNotFoundByIdException("invalid ID !!!"));

	}

	@Override
	public ResponseEntity<ResponseStructure<List<SubjectResponseDTO>>> findAllSubjects() {
		List<SubjectResponseDTO> subjects = new ArrayList<>();
		subjectRepo.findAll().forEach(subject -> {
			subjects.add(mapToSubjectResponseDTO(subject));
		});
		ResponseStructure<List<SubjectResponseDTO>> rs = new ResponseStructure<>();

		rs.setData(subjects);
		rs.setMessage("Subjects Fetched Succesfully");
		rs.setStatus(HttpStatus.OK.value());
		return new ResponseEntity<ResponseStructure<List<SubjectResponseDTO>>>(rs, HttpStatus.OK);
	}

}

//	List<Subject> subjects = new ArrayList<>();
//	subjectRequestDTO.getSubjectNames().forEach(name -> {
//		subjects.add(
//				subjectRepo.findBySubjectName(name).map(subject -> {
//					System.out.println(subject.getSubjectName());
//					return subject;
//				}).orElseGet(() -> {
//					Subject subject = new Subject();
//					subject.setSubjectName(name);
//					System.err.println("Creating new Subject");
//					subject = subjectRepo.save(subject);
//					return subject;
//				})
//				);
//	});
//	program.setSubjects(subjects);
//	academicProgramRepo.save(program);
//	structure.setMessage(" subjects added successfully !!! ");
//	structure.setStatus(HttpStatus.OK.value());
//	structure.setData(academicProgramServiceImpl.mapToAcademicsProgramResponseDto(program));
//	return new ResponseEntity<ResponseStructure<AcademicsProgramResponseDto>>(structure, HttpStatus.OK);
//}).orElseThrow(() -> new AcademicProgramNotFoundByIdException(" invalid id "));
//
//}
