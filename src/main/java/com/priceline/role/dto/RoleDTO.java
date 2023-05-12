package com.priceline.role.dto;

import com.priceline.role.model.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class RoleDTO extends AbstractDTO {

	private String name;
	
	public Role toRole() {
		Role role = new Role();
		role.setName(this.name);
		
		return role;
	}

}
