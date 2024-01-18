package com.school.sba.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.school.sba.entity.AcademicProgram;
import com.school.sba.entity.Subject;
import com.school.sba.repository.AcademicProgramRepo;
import com.school.sba.repository.SubjectRepo;
import com.school.sba.requestdto.SubjectRequestDTO;
import com.school.sba.responnsedto.SubjectResponseDTO;
import com.school.sba.util.ResponseStructure;

@Service
public class SubjectServiceImpl implements SubjectService {

	@Autowired
	private SubjectRepo subjectRepo;

	@Autowired
	private AcademicProgramRepo academicProgramRepo;

	public Subject mapToSubject(SubjectRequestDTO subjectRequestDTO) {
		return Subject.builder().subjectName(subjectRequestDTO.getSubjectNames()).build();

	}

	@Override
	public ResponseEntity<ResponseStructure<SubjectResponseDTO>> addSubjects(int programId,
			SubjectRequestDTO subjectRequestDTO) {
academicProgramRepo.findById(programId).map(academicProgram->{
	subjectRequestDTO.getSubjectNames().forEach(name->{
		List<Subject> subjects = academicProgram.getSubjects();
		subjectRepo.findBySubjectName(name).map(subject -> {
			academicProgram.getSubjects().add(subject);
			
		}).orElse(() -> {
			
		}).get();
		
	})
	
	
	subjectRepo.save(mapToSubject(subjectRequestDTO));
})
	}

}
