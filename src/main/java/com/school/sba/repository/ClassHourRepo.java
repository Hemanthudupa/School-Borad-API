package com.school.sba.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.school.sba.entity.ClassHour;

public interface ClassHourRepo extends JpaRepository<ClassHour, Integer> {


	public boolean existsByBeginsAtLocalTimeAndRoomNo(LocalDateTime beginsAt, int roomNo);

//	public boolean existsByRoomNoAndBeginsAtBetween(int roomNo, LocalDateTime startTime, LocalDateTime endTime);

}
