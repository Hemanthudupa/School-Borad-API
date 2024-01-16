package com.school.sba.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.school.sba.entity.User;
import com.school.sba.enums.UserRole;

public interface User_Repo extends JpaRepository<User, Integer> {

	boolean existsByUserRole(UserRole role);
}