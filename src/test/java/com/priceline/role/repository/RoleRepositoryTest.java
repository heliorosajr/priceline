package com.priceline.role.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.priceline.role.model.Role;
import com.priceline.role.utils.TestUtils;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;
    
    @AfterEach
    public void destroyAll(){
    	roleRepository.deleteAll();
    }
    
    @Test
    @DisplayName("Get role by name")
    public void testGetRoleByName() {
    	// create roles
    	Role role1 = roleRepository.save(TestUtils.createRole());
    	Role role2 = roleRepository.save(TestUtils.createRole());
    	
    	// fetch role1
    	Role actual = roleRepository.findByName(role1.getName());
    	
    	// assert
    	assertNotNull(actual);
    	assertEquals(role1, actual);
    	assertNotEquals(role2, actual);
    	
    	// fetch role2 and assert
    	actual = roleRepository.findByName(role2.getName());
    	assertNotNull(actual);
    	assertEquals(role2, actual);
    	assertNotEquals(role1, actual);
    }
    
    @Test
    @DisplayName("Get role by uid")
    public void testGetRoleByUid() {
    	// create roles
    	Role role1 = roleRepository.save(TestUtils.createRole());
    	Role role2 = roleRepository.save(TestUtils.createRole());
    	
    	// fetch role1
    	Role actual = roleRepository.findByUid(role1.getUid());
    	
    	// assert role1
    	assertNotNull(actual);
    	assertEquals(role1, actual);
    	assertNotEquals(role2, actual);
    	
    	// fetch role2
    	actual = roleRepository.findByUid(role2.getUid());
    	
    	// assert role2
    	assertNotNull(actual);
    	assertEquals(role2, actual);
    	assertNotEquals(role1, actual);
    }
    
    @Test
    @DisplayName("Get default role")
    public void testGetDefaultRole() {
    	// create roles
    	Role role1 = TestUtils.createRole(false);
    	Role role2 = TestUtils.createRole(false);
    	Role role3 = TestUtils.createRole(true);
    	Role role4 = TestUtils.createRole(false);
    	Role role5 = TestUtils.createRole(false);
    	
    	List<Role> roles = new ArrayList<>();
    	roles.add(role1);
    	roles.add(role2);
    	roles.add(role3);
    	roles.add(role4);
    	roles.add(role5);
    	
    	// persist values
    	roleRepository.saveAll(roles);
    	
    	// fetch default role
    	Role defaultRole = roleRepository.findByDefaultRoleTrue();
    	
    	// assert
    	assertNotNull(defaultRole);
    	assertEquals(role3, defaultRole);
    	assertFalse(role1.isDefaultRole());
    	assertFalse(role2.isDefaultRole());
    	assertTrue(role3.isDefaultRole());
    	assertFalse(role4.isDefaultRole());
    	assertFalse(role5.isDefaultRole());
    }
    
    @Test
    @DisplayName("Delete role by uid")
    public void testDeletetRoleByUid() {
    	// create role
    	Role role = roleRepository.save(TestUtils.createRole());
    	
    	// fetch role
    	Role actual = roleRepository.findByUid(role.getUid());
    	
    	// assert exists
    	assertNotNull(actual);
    	assertEquals(role, actual);
    	
    	// delete
    	roleRepository.deleteByUid(role.getUid());
    	
    	// assert no longer exists
    	actual = roleRepository.findByUid(role.getUid());
    	assertNull(actual);
    }

}
