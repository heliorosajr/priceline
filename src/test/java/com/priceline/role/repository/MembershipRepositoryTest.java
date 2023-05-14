package com.priceline.role.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.priceline.role.model.Membership;
import com.priceline.role.model.Role;
import com.priceline.role.utils.TestUtils;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
public class MembershipRepositoryTest {
	
	@Autowired
    private RoleRepository roleRepository;

    @Autowired
    private MembershipRepository membershipRepository;
    
    private Role defaultRole;
    
    @BeforeEach
    public void setUp() {
    	if(defaultRole == null) {
    		defaultRole = roleRepository.save(TestUtils.createRole(true));
    	}
    }
    
    @AfterEach
    public void destroyAll(){
    	membershipRepository.deleteAll();
    }
    
    @Test
    @DisplayName("Get membership by uid")
    public void testGetMembershipByUid() {
    	// create memberships
    	Membership membership1 = membershipRepository.save(TestUtils.createMembership(defaultRole));
    	Membership membership2 = membershipRepository.save(TestUtils.createMembership(defaultRole));
    	
    	// fetch membership1
    	Membership actual = membershipRepository.findByUid(membership1.getUid());
    	
    	// assert membership1
    	assertNotNull(actual);
    	assertEquals(membership1, actual);
    	assertNotEquals(membership2, actual);
    	
    	// fetch membership2
    	actual = membershipRepository.findByUid(membership2.getUid());
    	
    	// assert membership2
    	assertNotNull(actual);
    	assertEquals(membership2, actual);
    	assertNotEquals(membership1, actual);
    }
    
    @Test
    @DisplayName("Get membership by user id, team id and role")
    public void testGetMembershipByUserIdAndTeamIdAndRole() {
    	// create membership
    	Membership membership1 = membershipRepository.save(TestUtils.createMembership(defaultRole));
    	Membership membership2 = membershipRepository.save(TestUtils.createMembership(defaultRole));
    	Membership membership3 = membershipRepository.save(TestUtils.createMembership(defaultRole));
    	
    	// fetch membership1
    	Membership actual = membershipRepository.findByUserIdAndTeamIdAndRole(membership1.getUserId(), membership1.getTeamId(), defaultRole);
    	
    	// assert membership1
    	assertEquals(membership1, actual);
    	
    	// fetch membership2
    	actual = membershipRepository.findByUserIdAndTeamIdAndRole(membership2.getUserId(), membership2.getTeamId(), defaultRole);
    	
    	// assert membership2
    	assertEquals(membership2, actual);
    	
    	// fetch membership3
    	actual = membershipRepository.findByUserIdAndTeamIdAndRole(membership3.getUserId(), membership3.getTeamId(), defaultRole);
    	
    	// assert membership3
    	assertEquals(membership3, actual);
    }
    
    @Test
    @DisplayName("Get membership by role")
    public void testGetMembershipByRole() {
    	// create memberships with default role
    	Membership membership1RoleDefault = membershipRepository.save(TestUtils.createMembership(defaultRole));
    	Membership membership2RoleDefault = membershipRepository.save(TestUtils.createMembership(defaultRole));
    	Membership membership3RoleDefault = membershipRepository.save(TestUtils.createMembership(defaultRole));
    	
    	// create role2
    	Role role2 = roleRepository.save(TestUtils.createRole(false));
    	
    	// create memberships for role2
    	Membership membership1Role2 = membershipRepository.save(TestUtils.createMembership(role2));
    	Membership membership2Role2 = membershipRepository.save(TestUtils.createMembership(role2));
    	Membership membership3Role2 = membershipRepository.save(TestUtils.createMembership(role2));
    	Membership membership4Role2 = membershipRepository.save(TestUtils.createMembership(role2));
    	Membership membership5Role2 = membershipRepository.save(TestUtils.createMembership(role2));
    	
    	// fetch memberships for role default
    	List<Membership> actual = membershipRepository.findByRole_uid(defaultRole.getUid());
    	
    	// assert memberships for role default
    	assertEquals(3, actual.size());
    	assertTrue(actual.contains(membership1RoleDefault));
    	assertTrue(actual.contains(membership2RoleDefault));
    	assertTrue(actual.contains(membership3RoleDefault));
    	
    	assertFalse(actual.contains(membership1Role2));
    	assertFalse(actual.contains(membership2Role2));
    	assertFalse(actual.contains(membership3Role2));
    	assertFalse(actual.contains(membership4Role2));
    	assertFalse(actual.contains(membership5Role2));
    	
    	// fetch memberships for role2
    	actual = membershipRepository.findByRole_uid(role2.getUid());
    	
    	// assert
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
    	// create membership
    	Membership membership = membershipRepository.save(TestUtils.createMembership(defaultRole));
    	
    	// fetch membership
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

}
