package com.school.sba.responnsedto;

import com.school.sba.enums.UserRole;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserResponseDTO {
	private int userId;
	private String userName;
	private String firstName;
	private String lastName;
	private long contactNo;
	private String email;
	private UserRole userRole;
}
