package com.school.sba.requestdto;

import com.school.sba.enums.UserRole;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDTO {
	@NotNull(message = "username cannot be null")
	@NotBlank
	private String userName;
	@NotNull(message = "password cannot be null")
	@NotBlank
	private String password;
	private String firstName;
	@NotNull
	@NotBlank
	private String lastName;
	@NotNull
	private long contactNo;
	@NotNull
	@NotBlank
	private String email;
	private UserRole userRole;

}
