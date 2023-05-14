package com.priceline.role.utils;

import java.util.Random;
import java.util.UUID;

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
    
    // Membership
    public static Membership createMembership(Role role) {
    	Membership membership = new Membership();
    	membership.setUid(UUID.randomUUID().toString());
    	membership.setUserId(UUID.randomUUID().toString());
    	membership.setTeamId(UUID.randomUUID().toString());
        membership.setRole(role);
    	
    	return membership;
    }

}
