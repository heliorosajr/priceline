package com.priceline.role.utils;

import java.util.Random;
import java.util.UUID;

import com.priceline.role.dto.MembershipDTO;
import com.priceline.role.dto.RoleDTO;
import com.priceline.role.model.Membership;
import com.priceline.role.model.Role;

public class TestUtils {
	
	private static Random random = new Random();
	
    // Role
    public static Role createRole() {
    	return createRole(true);
    }
    
    public static Role createRole(boolean defaultRole) {
    	Role role = new Role();
    	role.setUid(UUID.randomUUID().toString());
    	role.setName("Role " + random.nextInt() + System.currentTimeMillis());
    	role.setDefaultRole(defaultRole);

    	return role;
    }

	public static RoleDTO createRoleDTO(boolean defaultRole) {
		RoleDTO dto = new RoleDTO();
		dto.setName("Role " + random.nextInt() + System.currentTimeMillis());
		dto.setDefaultRole(defaultRole);
		
		return dto;
	}
    
    // Membership
    public static Membership createMembership() {
    	return createMembership(createRole(true));
    }
    
    public static Membership createMembership(Role role) {
    	Membership membership = new Membership();
    	membership.setUid(UUID.randomUUID().toString());
    	membership.setUserId(UUID.randomUUID().toString());
    	membership.setTeamId(UUID.randomUUID().toString());
        membership.setRole(role);
    	
    	return membership;
    }
    
    public static MembershipDTO createMembershipDTO() {
    	return createMembershipDTO(createRole(true));
    }
    
	public static MembershipDTO createMembershipDTO(Role role) {
		MembershipDTO dto = new MembershipDTO();
		dto.setUserId(UUID.randomUUID().toString());
		dto.setTeamId(UUID.randomUUID().toString());
		dto.setRole(role);
		
		return dto;
	}

}
