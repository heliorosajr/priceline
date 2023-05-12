package com.priceline.role.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TeamDTO {

private String id;
	
	private String name;
	
	private String teamLeadId;
	
	private List<String> teamMemberIds;

}
