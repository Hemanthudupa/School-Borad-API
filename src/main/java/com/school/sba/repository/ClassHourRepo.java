package com.school.sba.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.school.sba.entity.ClassHour;
import com.school.sba.entity.User;
import com.school.sba.responnsedto.ClassHourResponseDTO;

public interface ClassHourRepo extends JpaRepository<ClassHour, Integer> {

	public boolean existsByBeginsAtLocalTimeAndRoomNo(LocalDateTime beginsAt, int roomNo);

	public List<ClassHour> findByUser(User user);

	public List<ClassHour> findAllByBeginsAtLocalTime(LocalDate localDate);


	public List<ClassHour> findByEndsALocalTimeBetween(LocalDateTime minusDays,
			LocalDateTime minusDays2);

//	public boolean existsByRoomNoAndBeginsAtBetween(int roomNo, LocalDateTime startTime, LocalDateTime endTime);

}
