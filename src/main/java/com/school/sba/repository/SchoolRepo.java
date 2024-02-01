package com.school.sba.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.school.sba.entity.School;
import com.school.sba.enums.UserRole;

public interface SchoolRepo extends JpaRepository<School, Integer> {

	boolean existsBySchoolName(String schoolName);

	List<School> findByIsDeleted(boolean b);

}
