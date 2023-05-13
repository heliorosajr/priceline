package com.priceline.role.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.priceline.role.RoleApplication;
import com.priceline.role.enums.MessageEnum;
import com.priceline.role.model.Role;
import com.priceline.role.model.exception.PricelineApiException;
import com.priceline.role.repository.RoleRepository;
import com.priceline.role.service.system.MessageService;

@RunWith(SpringRunner.class)
@TestPropertySource("classpath:application.properties")
@SpringBootTest(classes = RoleApplication.class, properties = {"spring.config.name=roleDb","store.trx.datasource.url=jdbc:h2:mem:roleDb"})
@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
public class RoleServiceTest {

	@Autowired
	private MessageService messageService;

	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private RoleService roleService;
	
    private static Random random = new Random();
    
    @BeforeAll
    public static void init() {
    	random = new Random();
    }

    @AfterEach
    public void destroyAll(){
    	roleRepository.deleteAll();
    }
	
	@Test
	@DisplayName("Get role by uid")
    public void testGetRoleByUid() {
		Role role = roleRepository.save(createRole(true));
		Role actual = roleService.findByUid(role.getUid());
		
		assertEquals(role, actual);
	}
	
	@Test
	@DisplayName("Get role by nonexistent uid")
    public void testGetRoleByNonexistentUid() {
		String uid = UUID.randomUUID().toString();

		PricelineApiException exception = assertThrows(PricelineApiException.class, () -> {
			roleService.findByUid(uid);
	    });
		
		// assert
		String message = messageService.getMessage(MessageEnum.EXCEPTION_ENTITY_NOT_FOUND_ERR, uid);
		assertEquals(exception.getMessage(), message);
	}
	
	@Test
	@DisplayName("Get all roles")
    public void testGetAllRoles() {
		Role role1 = roleRepository.save(createRole(true));
		Role role2 = roleRepository.save(createRole(false));
		Role role3 = roleRepository.save(createRole(false));
		Role role4 = roleRepository.save(createRole(false));
		Role role5 = roleRepository.save(createRole(false));

		List<Role> actual = roleService.findAll();
		
		// assert
		assertEquals(5, actual.size());
		assertEquals(actual, List.of(role1, role2, role3, role4, role5));
	}
	
	@Test
	@DisplayName("Get default role")
    public void testGetDefaultRole() {
		Role expected = roleRepository.save(createRole(true));
		roleRepository.save(createRole(false));
		roleRepository.save(createRole(false));
		roleRepository.save(createRole(false));
		roleRepository.save(createRole(false));

		List<Role> allRoles = roleService.findAll();
		Role actual = roleService.findDefaultRole();
		
		int total = 0;
		for(Role role : allRoles) {
			total += role.isDefaultRole() ? 1 : 0;
		}
		
		// assert
		assertEquals(1, total);
		assertEquals(expected, actual);
	}
	
	@Test
	@DisplayName("Get default role when a default is not set")
    public void testGetDefaultRoleWhenADefaultIsNotSet() {
		roleRepository.save(createRole(false));
		roleRepository.save(createRole(false));
		roleRepository.save(createRole(false));
		roleRepository.save(createRole(false));
		roleRepository.save(createRole(false));
		
		List<Role> allRoles = roleService.findAll();
		
		int total = 0;
		for(Role role : allRoles) {
			total += role.isDefaultRole() ? 1 : 0;
		}
		
		// assert
		assertEquals(0, total);

		PricelineApiException exception = assertThrows(PricelineApiException.class, () -> {
			roleService.findDefaultRole();
	    });
		
		// assert
		String message = messageService.getMessage(MessageEnum.EXCEPTION_DEFAULT_ROLE_NOT_FOUND_ERR);
		assertEquals(exception.getMessage(), message);
	}
	
	// Utilities
	private Role createRole(boolean defaultRole) {
    	Role role = new Role();
    	role.setUid(UUID.randomUUID().toString());
    	role.setName("Role " + random.nextInt() + System.currentTimeMillis());
    	role.setDefaultRole(defaultRole);

    	return role;
    }

}
