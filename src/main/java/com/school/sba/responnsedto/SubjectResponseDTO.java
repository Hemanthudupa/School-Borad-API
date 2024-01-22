package com.school.sba.responnsedto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SubjectResponseDTO {
	private int subjectId;
	private String subjectName;

}
