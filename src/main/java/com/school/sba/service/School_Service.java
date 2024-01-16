package com.school.sba.service;

import java.util.List;

import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.school.sba.entity.School;
import com.school.sba.repository.School_Repo;
import com.school.sba.responsestructure.School_ResponseStructure;

@Service
public class School_Service {
	@Autowired
	School_Repo repo;

	public ResponseEntity<School_ResponseStructure<School>> save(School school) {
		School_ResponseStructure<School> rs = new School_ResponseStructure<>();
		rs.setData(school);
		rs.setMessage("school added successfully");
		rs.setStatus(HttpStatus.OK.value());
		repo.save(school);
		ResponseEntity<School_ResponseStructure<School>> resp = new ResponseEntity<>(rs,HttpStatus.OK);
		return resp;
	}

	public School update(School school) {
		School orElseThrow = repo.findById(school.getSchoolId())
				.orElseThrow(() -> new ObjectNotFoundException("Object", school));
		orElseThrow.setAddress(school.getAddress());
		orElseThrow.setEmailId(school.getEmailId());
		orElseThrow.setContactNo(school.getContactNo());
		orElseThrow.setShoolName(school.getShoolName());
		return repo.save(orElseThrow);
	}

	public String delete(int id) {
		School deleteEntity = repo.findById(id).orElseThrow(() -> new ObjectNotFoundException("not found ", repo));
		repo.delete(deleteEntity);
		return "Object delete successfully ";
	}

	public List<School> getAll() {
		return repo.findAll();
	}

	public School getSchoolByID(int id) {
		return repo.findById(id).orElseThrow(() -> new ObjectNotFoundException(repo, null));
	}
}