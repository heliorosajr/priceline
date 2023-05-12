package com.priceline.role.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.priceline.role.model.Membership;
import com.priceline.role.model.Role;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
public class MembershipRepositoryTest {
	
	@Autowired
    private RoleRepository roleRepository;

    @Autowired
    private MembershipRepository membershipRepository;
    
    private static Random random;
    
    private Role defaultRole;
    
    @BeforeAll
    public static void init() {
    	random = new Random();
    }
    
    @BeforeEach
    public void setUp() {
    	if(defaultRole == null) {
    		defaultRole = createRole(true);
    	}
    }
    
    @AfterEach
    public void destroyAll(){
    	membershipRepository.deleteAll();
    }
    
    @Test
    @DisplayName("Get membership by uid")
    public void testGetMembershipByUid() {
    	// create roles
    	Membership membership1 = membershipRepository.save(createMembership());
    	Membership membership2 = membershipRepository.save(createMembership());
    	
    	Membership actual = membershipRepository.findByUid(membership1.getUid());
    	
    	// assert
    	assertNotNull(actual);
    	assertEquals(membership1, actual);
    	assertNotEquals(membership2, actual);
    	
    	actual = membershipRepository.findByUid(membership2.getUid());
    	assertNotNull(actual);
    	assertEquals(membership2, actual);
    	assertNotEquals(membership1, actual);
    }
    
    @Test
    @DisplayName("Get membership by role")
    public void testGetMembershipByRole() {
    	// create roles
    	Membership membership1RoleDefault = membershipRepository.save(createMembership());
    	Membership membership2RoleDefault = membershipRepository.save(createMembership());
    	Membership membership3RoleDefault = membershipRepository.save(createMembership());
    	
    	// create other role
    	Role role2 = createRole(false);
    	
    	// create memberships for role2
    	Membership membership1Role2 = membershipRepository.save(createMembership(role2));
    	Membership membership2Role2 = membershipRepository.save(createMembership(role2));
    	Membership membership3Role2 = membershipRepository.save(createMembership(role2));
    	Membership membership4Role2 = membershipRepository.save(createMembership(role2));
    	Membership membership5Role2 = membershipRepository.save(createMembership(role2));
    	
    	List<Membership> actual = membershipRepository.findByRole_uid(defaultRole.getUid());
    	
    	// assert
    	assertEquals(3, actual.size());
    	assertTrue(actual.contains(membership1RoleDefault));
    	assertTrue(actual.contains(membership2RoleDefault));
    	assertTrue(actual.contains(membership3RoleDefault));
    	
    	assertFalse(actual.contains(membership1Role2));
    	assertFalse(actual.contains(membership2Role2));
    	assertFalse(actual.contains(membership3Role2));
    	assertFalse(actual.contains(membership4Role2));
    	assertFalse(actual.contains(membership5Role2));
    	
    	actual = membershipRepository.findByRole_uid(role2.getUid());
    	assertEquals(5, actual.size());
    	assertTrue(actual.contains(membership1Role2));
    	assertTrue(actual.contains(membership2Role2));
    	assertTrue(actual.contains(membership3Role2));
    	assertTrue(actual.contains(membership4Role2));
    	assertTrue(actual.contains(membership5Role2));

    	assertFalse(actual.contains(membership1RoleDefault));
    	assertFalse(actual.contains(membership2RoleDefault));
    	assertFalse(actual.contains(membership3RoleDefault));
    }
    
    @Test
    @DisplayName("Delete membership by uid")
    public void testDeletetMembershipByUid() {
    	// create role
    	Membership membership = membershipRepository.save(createMembership());
    	Membership actual = membershipRepository.findByUid(membership.getUid());
    	
    	// assert exists
    	assertNotNull(actual);
    	assertEquals(membership, actual);
    	
    	// delete
    	membershipRepository.deleteByUid(membership.getUid());
    	
    	// assert no longer exists
    	actual = membershipRepository.findByUid(membership.getUid());
    	assertNull(actual);
    }
    
    // Utilities
    private Role createRole(boolean defaultRole) {
    	Role role = new Role();
    	role.setUid(UUID.randomUUID().toString());
    	role.setName("Role " + random.nextInt() + System.currentTimeMillis());
    	role.setDefaultRole(defaultRole);
    	
    	return roleRepository.save(role);
    }
    
    private Membership createMembership() {
    	return createMembership(defaultRole);
    }
    
    private Membership createMembership(Role role) {
    	Membership membership = new Membership();
    	membership.setUid(UUID.randomUUID().toString());
    	membership.setUserId(UUID.randomUUID().toString());
    	membership.setTeamId(UUID.randomUUID().toString());
        membership.setRole(role);
    	
    	return membership;
    }

}
