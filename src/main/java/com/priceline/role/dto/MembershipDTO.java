package com.priceline.role.dto;

import com.priceline.role.model.Membership;
import com.priceline.role.model.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class MembershipDTO extends AbstractDTO {

	private String userId;
	
	private String teamId;
	
	private Role role;
	
	public Membership toMembership() {
		Membership membership = new Membership();
		membership.setUserId(this.userId);
		membership.setTeamId(this.teamId);
		membership.setRole(this.role);
		
		return membership;
	}

}
