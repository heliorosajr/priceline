package com.priceline.role.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import com.priceline.role.config.MessageConfig;
import com.priceline.role.dto.RoleDTO;
import com.priceline.role.enums.MessageEnum;
import com.priceline.role.model.Role;
import com.priceline.role.model.exception.DefaultRoleNotFoundException;
import com.priceline.role.model.exception.EntityNotFoundException;
import com.priceline.role.model.exception.PricelineApiException;
import com.priceline.role.repository.RoleRepository;
import com.priceline.role.service.system.ExceptionService;
import com.priceline.role.service.system.MessageService;
import com.priceline.role.service.system.ValidationService;

import jakarta.persistence.PersistenceException;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {

	@Mock
	private RoleRepository roleRepository;
	
	private RoleService roleService;

	private Random random = new Random();

	// Dependencies
	private ExceptionService exceptionService;
	
	private MessageService messageService;

	private ValidationService validationService;	
    
    @BeforeEach
    public void setUp() {
    	messageService = new MessageService(new MessageConfig().messageSource());
    	exceptionService = new ExceptionService(messageService);
    	validationService = new ValidationService(exceptionService);
    	roleService = new RoleServiceImpl(roleRepository, exceptionService, validationService);
    }
    
	@Test
	@DisplayName("Get role by uid")
    public void testGetRoleByUid() {
		// create role
		Role expected = createRole(true);
		
		// configure mock
		when(roleRepository.findByUid(anyString())).thenReturn(expected);
		
		// find by uid
		Role actual = roleService.findByUid(expected.getUid());
		
		// assert
		assertEquals(expected, actual);
	}
	
	@Test
	@DisplayName("Get role by uid with unexpected exception")
	@Transactional
    public void testGetRoleByUidWithUnexpectedException() {
		// create random id
		String uid = UUID.randomUUID().toString();
		
		// configure mock
		doThrow(new PersistenceException()).when(roleRepository).findByUid(uid);
		
		// delete role
		PricelineApiException exception = assertThrows(PricelineApiException.class, () -> {
			roleService.findByUid(uid);
	    });
		
		// assert
		String message = messageService.getMessage(MessageEnum.ROLE_ERROR_FIND_BY_ID_HELP, uid);
		assertEquals(exception.getHelp(), message);
	}
	
	@Test
	@DisplayName("Get role by nonexistent uid")
    public void testGetRoleByNonexistentUid() {
		// create random uid
		String uid = UUID.randomUUID().toString();
		
		// configure mock
		when(roleRepository.findByUid(anyString())).thenThrow(new EntityNotFoundException(uid));

		// find by uid
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
		// create roles
		Role role1 = createRole(true);
		Role role2 = createRole(false);
		Role role3 = createRole(false);
		Role role4 = createRole(false);
		Role role5 = createRole(false);
		List<Role> expected = List.of(role1, role2, role3, role4, role5);
		
		// configure mock
		when(roleRepository.findAll()).thenReturn(expected);

		// find all roles
		List<Role> actual = roleService.findAll();
		
		// assert
		assertEquals(5, actual.size());
		assertEquals(actual, expected);
	}
	
	@Test
	@DisplayName("Get default role")
    public void testGetDefaultRole() {
		// create role
		Role expected = createRole(true);
		
		// configure mock
		when(roleRepository.findByDefaultRoleTrue()).thenReturn(expected);

		// find default role
		Role actual = roleService.findDefaultRole();

		// assert
		assertEquals(expected, actual);
	}
	
	@Test
	@DisplayName("Get default role when a default is not set")
    public void testGetDefaultRoleWhenADefaultIsNotSet() {
		// configure mock
		when(roleRepository.findByDefaultRoleTrue()).thenThrow(new DefaultRoleNotFoundException());

		// find default role
		PricelineApiException exception = assertThrows(PricelineApiException.class, () -> {
			roleService.findDefaultRole();
	    });
		
		// assert
		String message = messageService.getMessage(MessageEnum.EXCEPTION_DEFAULT_ROLE_NOT_FOUND_ERR);
		assertEquals(exception.getMessage(), message);
	}
	
	@Test
	@DisplayName("Save first role in database")
    public void testSaveFirstRoleInDatabase() {
		// create DTO 
		RoleDTO dto = createRoleDTO(false);
		
		// create role
		Role expected = dto.toRole();
		expected.setDefaultRole(true);

		// configure mock
		when(roleRepository.count()).thenReturn(0L);
		when(roleRepository.save(any())).thenReturn(expected);
		
		// save role
		Role actual = roleService.save(dto);
				
		// assert
		assertEquals(expected,  actual);
	}
	
	@Test
	@DisplayName("Save role in database")
    public void testSaveRoleInDatabase() {
		// create DTO 
		RoleDTO dto = createRoleDTO(false);
		
		// create role
		Role expected = dto.toRole();

		// configure mock
		when(roleRepository.count()).thenReturn(1L);
		when(roleRepository.save(any())).thenReturn(expected);
		
		// save role
		Role actual = roleService.save(dto);
				
		// assert
		assertEquals(expected,  actual);
	}
	
	@Test
	@DisplayName("Save role setting role as default")
    public void testSaveRoleSettingRoleAsDefault() {
		// create DTO 
		RoleDTO dto = createRoleDTO(true);
		
		// create role
		Role expected = dto.toRole();
		
		// create previous default role
		Role oldDefaultRole = createRole(true);

		// configure mock
		when(roleRepository.count()).thenReturn(1L);
		when(roleRepository.findByDefaultRoleTrue()).thenReturn(oldDefaultRole);
		when(roleRepository.save(any())).thenReturn(oldDefaultRole).thenReturn(expected);
				
		// save role
		Role actual = roleService.save(dto);
				
		// assert
		assertEquals(expected,  actual);
	}

	@Test
	@DisplayName("Update role")
    public void testUpdateRole() {
		// create role
		Role role = createRole(true);
		
		// configure mock
		when(roleRepository.findByUid(role.getUid())).thenReturn(role);
		
		// fetch role
		Role expected = roleService.findByUid(role.getUid());
		
		// assert
		assertEquals(role, expected);
		
		// create DTO
		String newName = RandomStringUtils.randomAlphabetic(10); 
		RoleDTO dto = createRoleDTO(false);
		dto.setName(newName);
		
		// configure mock
		expected.setName(newName);
		when(roleRepository.save(any())).thenReturn(expected);
				
		// update role
		expected = roleService.update(dto, role.getUid());
		
		// assert
		assertEquals(newName, expected.getName());
	}
	
	@Test
	@DisplayName("Update nonexistent role")
    public void testUpdateNonexistentRole() {
		// create random uid
		String uid = UUID.randomUUID().toString();
		
		// configure mock
		when(roleRepository.findByUid(anyString())).thenThrow(new EntityNotFoundException(uid));
		
		// create DTO
		String newName = RandomStringUtils.randomAlphabetic(10); 
		RoleDTO dto = createRoleDTO(false);
		dto.setName(newName);

		// update
		PricelineApiException exception = assertThrows(PricelineApiException.class, () -> {
			roleService.update(dto, uid);
	    });
		
		// assert
		String message = messageService.getMessage(MessageEnum.EXCEPTION_ENTITY_NOT_FOUND_ERR, uid);
		assertEquals(exception.getMessage(), message);
	}

	@Test
	@DisplayName("Update default role")
    public void testUpdateDefaultRole() {
		// create role
		Role role1 = createRole(true);
		
		// configure mock
		when(roleRepository.findByDefaultRoleTrue()).thenReturn(role1);
				
		// get default role
		Role defaultRole = roleService.findDefaultRole();
		
		// assert
		assertEquals(role1, defaultRole);

		// create role2
		Role role2 = createRole(false);
		
		// configure mock
		when(roleRepository.findByUid(role2.getUid())).thenReturn(role2);
		
		// update default role to role3
		defaultRole = roleService.updateDefaultRole(role2.getUid());
		
		// assert
		assertEquals(role2, defaultRole);
	}
	
	@Test
	@DisplayName("Update default role using the current default")
    public void testUpdateDefaultRoleUsginTheCurrentDefault() {
		// create role
		Role role = createRole(true);

		// configure mock
		when(roleRepository.findByDefaultRoleTrue()).thenReturn(role);
				
		// get default role
		Role defaultRole = roleService.findDefaultRole();

		// assert
		assertEquals(role, defaultRole);
		
		// update default role to role3
		defaultRole = roleService.updateDefaultRole(role.getUid());
		
		// assert
		assertEquals(role, defaultRole);
	}

	@Test
	@DisplayName("Delete role")
	@Transactional
    public void testDeleteRole() {
		// create role
		Role expected = createRole(true);
		
		// configure mock
		when(roleRepository.findByUid(expected.getUid())).thenReturn(expected).thenThrow(new EntityNotFoundException(expected.getUid()));
		
		// fetch role
		Role actual = roleService.findByUid(expected.getUid());
		
		// assert
		assertEquals(expected, actual);

		// delete role
		roleService.delete(expected.getUid());
		
		// fetch role again
		PricelineApiException exception = assertThrows(PricelineApiException.class, () -> {
			roleService.findByUid(expected.getUid());
	    });
		
		// assert
		String message = messageService.getMessage(MessageEnum.EXCEPTION_ENTITY_NOT_FOUND_ERR, expected.getUid());
		assertEquals(exception.getMessage(), message);
		assertEquals(0, roleRepository.count());
	}
	
	@Test
	@DisplayName("Delete role with unexpected exception")
	@Transactional
    public void testDeleteRoleWithUnexpectedException() {
		// create random id
		String uid = UUID.randomUUID().toString();
		
		// configure mock
		doThrow(new PersistenceException()).when(roleRepository).deleteByUid(uid);
		
		// delete role
		PricelineApiException exception = assertThrows(PricelineApiException.class, () -> {
			roleService.delete(uid);
	    });
		
		// assert
		String message = messageService.getMessage(MessageEnum.ROLE_ERROR_DELETE_HELP, uid);
		assertEquals(exception.getHelp(), message);
	}
	
	@Test
	@DisplayName("Validate dto without name")
	public void testValidateDTOWithoutName() {
		// create DTO
		RoleDTO dto = createRoleDTO(false);
		dto.setName(null);
		
		// validate
		PricelineApiException exception = assertThrows(PricelineApiException.class, () -> {
			roleService.validate(dto);
	    });
		
		// assert
		String message = messageService.getMessage(MessageEnum.VALIDATION_FAILURE_REQUIRED_ERR, "name");
		assertEquals(exception.getMessage(), message);
	}
	
	@Test
	@DisplayName("Validate dto with name that exceeds character limit")
	public void testValidateDTOWithNameThatExceedsCharacterLimit() {
		// create DTO
		RoleDTO dto = createRoleDTO(false);
		dto.setName(RandomStringUtils.randomAlphabetic(151));
		
		// validate
		PricelineApiException exception = assertThrows(PricelineApiException.class, () -> {
			roleService.validate(dto);
	    });
		
		// assert
		String message = messageService.getMessage(MessageEnum.VALIDATION_FAILURE_STRING_MAX_LENGTH_ERR, "name", 150);
		assertEquals(exception.getMessage(), message);
	}
	
	@Test
	@DisplayName("Validate dto with name that already exists while creating role")
	public void testValidateDTOWithNameThatAlreadyExistWhileCreatingRole() {
		// create role
		Role role = createRole(true);
		
		// create DTO with repeated name
		RoleDTO dto = createRoleDTO(false);
		dto.setUid(null); // uid is null during creation
		dto.setName(role.getName());
		
		// configure mock
		when(roleRepository.findByName(role.getName())).thenReturn(role);
		
		// validate
		PricelineApiException exception = assertThrows(PricelineApiException.class, () -> {
			roleService.validate(dto);
	    });
		
		// assert
		String message = messageService.getMessage(MessageEnum.VALIDATION_FAILURE_UNIQUENESS_ERR);
		assertEquals(exception.getMessage(), message);
	}
	
	@Test
	@DisplayName("Validate dto with name that already exists while updating role")
	public void testValidateDTOWithNameThatAlreadyExistWhileUpdatingRole() {
		// create roles
		Role role1 = createRole(true);
		Role role2 = createRole(false);
		
		// create DTO setting name of role1 in role2
		RoleDTO dto = createRoleDTO(false);
		dto.setUid(role2.getUid());
		dto.setName(role1.getName());

		// configure mock
		when(roleRepository.findByName(role1.getName())).thenReturn(role1);
		
		// validate
		PricelineApiException exception = assertThrows(PricelineApiException.class, () -> {
			roleService.validate(dto);
	    });
		
		// assert
		String message = messageService.getMessage(MessageEnum.VALIDATION_FAILURE_UNIQUENESS_ERR);
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

	private RoleDTO createRoleDTO(boolean defaultRole) {
		RoleDTO dto = new RoleDTO();
		dto.setName("Role " + random.nextInt() + System.currentTimeMillis());
		dto.setDefaultRole(defaultRole);
		
		return dto;
	}
}
