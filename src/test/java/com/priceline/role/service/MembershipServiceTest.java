package com.priceline.role.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import com.priceline.role.config.MessageConfig;
import com.priceline.role.dto.MembershipDTO;
import com.priceline.role.dto.TeamDTO;
import com.priceline.role.dto.UserDTO;
import com.priceline.role.enums.MessageEnum;
import com.priceline.role.facade.PricelineFacade;
import com.priceline.role.model.Membership;
import com.priceline.role.model.Role;
import com.priceline.role.model.exception.EntityNotFoundException;
import com.priceline.role.model.exception.PricelineApiException;
import com.priceline.role.repository.MembershipRepository;
import com.priceline.role.service.system.ExceptionService;
import com.priceline.role.service.system.MessageService;
import com.priceline.role.service.system.ValidationService;
import com.priceline.role.utils.TestUtils;

import jakarta.persistence.PersistenceException;

@ExtendWith(MockitoExtension.class)
public class MembershipServiceTest {
	
	@Mock
	private MembershipRepository membershipRepository;
	
	private MembershipService membershipService;
    
    @Mock
    private PricelineFacade pricelineFacade;
    
    @Mock
    private RoleService roleService;
    
    private ValidationService validationService;

	// Dependencies
    private ExceptionService exceptionService;
    
    private MessageService messageService;
    
    @BeforeEach
    public void setUp() {
    	messageService = new MessageService(new MessageConfig().messageSource());
    	exceptionService = new ExceptionService(messageService);
    	validationService = new ValidationService(exceptionService);
    	membershipService = new MembershipServiceImpl(membershipRepository, exceptionService, pricelineFacade, roleService, validationService);
    }
    
	@Test
	@DisplayName("Get membership by uid")
    public void testGetMembershipByUid() {
		// create role
		Membership expected = TestUtils.createMembership();
		
		// configure mock
		when(membershipRepository.findByUid(anyString())).thenReturn(expected);
		
		// find by uid
		Membership actual = membershipService.findByUid(expected.getUid());
		
		// assert
		assertEquals(expected, actual);
	}
	
	@Test
	@DisplayName("Get membership by uid with unexpected exception")
    public void testGetMembershipByUidWithUnexpectedException() {
		// create random id
		String uid = UUID.randomUUID().toString();
		
		// configure mock
		doThrow(new PersistenceException()).when(membershipRepository).findByUid(uid);
		
		// delete role
		PricelineApiException exception = assertThrows(PricelineApiException.class, () -> {
			membershipService.findByUid(uid);
	    });
		
		// assert
		String message = messageService.getMessage(MessageEnum.MEMBERSHIP_ERROR_FIND_BY_ID_HELP, uid);
		assertEquals(exception.getHelp(), message);
	}
	
	@Test
	@DisplayName("Get membership by nonexistent uid")
    public void testGetMembershipByNonexistentUid() {
		// create random uid
		String uid = UUID.randomUUID().toString();
		
		// configure mock
		when(membershipRepository.findByUid(anyString())).thenThrow(new EntityNotFoundException(uid));

		// find by uid
		PricelineApiException exception = assertThrows(PricelineApiException.class, () -> {
			membershipService.findByUid(uid);
	    });
		
		// assert
		String message = messageService.getMessage(MessageEnum.EXCEPTION_ENTITY_NOT_FOUND_ERR, uid);
		assertEquals(exception.getMessage(), message);
	}
	
	@Test
	@DisplayName("Get all memberships")
    public void testGetAllMemberships() {
		// create memberships
		Membership membership1 = TestUtils.createMembership();
		Membership membership2 = TestUtils.createMembership();
		Membership membership3 = TestUtils.createMembership();
		Membership membership4 = TestUtils.createMembership();
		Membership membership5 = TestUtils.createMembership();
		List<Membership> expected = List.of(membership1, membership2, membership3, membership4, membership5);
		
		// configure mock
		when(membershipRepository.findAll()).thenReturn(expected);

		// find all memberships
		List<Membership> actual = membershipService.findAll();
		
		// assert
		assertEquals(5, actual.size());
		assertEquals(actual, expected);
	}
	
	@Test
	@DisplayName("Get role of membership")
    public void testGetRoleOfMembership() {
		// create memberships
		Membership membership = TestUtils.createMembership();
		
		// configure mock
		when(membershipRepository.findByUid(any())).thenReturn(membership);

		// find role of membership
		Role actual = membershipService.findRoleOfMembership(membership.getUid());
		
		// assert
		assertEquals(actual, membership.getRole());
	}

	@Test
	@DisplayName("Get all memberships of role")
    public void testGetAllMembershipsOfRole() {
		// create memberships
		Membership membership1 = TestUtils.createMembership();
		Membership membership2 = TestUtils.createMembership(membership1.getRole());
		Membership membership3 = TestUtils.createMembership(membership1.getRole());
		Membership membership4 = TestUtils.createMembership(membership1.getRole());
		Membership membership5 = TestUtils.createMembership(membership1.getRole());
		List<Membership> expected = List.of(membership1, membership2, membership3, membership4, membership5);
		
		// configure mock
		when(membershipRepository.findByRole_uid(any())).thenReturn(expected);

		// find all memberships
		List<Membership> actual = membershipService.findMembershipsOfRole(membership1.getRole().getUid());
		
		// assert
		assertEquals(5, actual.size());
		assertEquals(actual, expected);
	}

	@Test
	@DisplayName("Save membership")
    public void testSaveMembership() {
		// create DTO 
		MembershipDTO dto = TestUtils.createMembershipDTO();
		
		// create membership
		Membership expected = dto.toMembership();
		
		// create user DTO
		UserDTO userDTO = new UserDTO();
		userDTO.setId(dto.getUserId());
		
		// create team DTO
		TeamDTO teamDTO = new TeamDTO();
		teamDTO.setId(dto.getTeamId());

		// configure mock
		when(membershipRepository.save(any())).thenReturn(expected);
		when(pricelineFacade.findUserById(any())).thenReturn(userDTO);
		when(pricelineFacade.findTeamById(any())).thenReturn(teamDTO);
		
		// save membership
		Membership actual = membershipService.save(dto);
				
		// assert
		assertEquals(expected,  actual);
	}
	
	@Test
	@DisplayName("Save membership with default role")
    public void testSaveMembershipWithDefaultRole() {
		// create default role
		Role role = TestUtils.createRole(true);
		
		// create DTO 
		MembershipDTO dto = TestUtils.createMembershipDTO();
		dto.setRole(null);
		
		// create membership
		Membership expected = dto.toMembership();
		expected.setRole(role);
		
		// create user DTO
		UserDTO userDTO = new UserDTO();
		userDTO.setId(dto.getUserId());
		
		// create team DTO
		TeamDTO teamDTO = new TeamDTO();
		teamDTO.setId(dto.getTeamId());

		// configure mock
		when(membershipRepository.save(any())).thenReturn(expected);
		when(pricelineFacade.findUserById(any())).thenReturn(userDTO);
		when(pricelineFacade.findTeamById(any())).thenReturn(teamDTO);
		when(roleService.findDefaultRole()).thenReturn(role);
		
		// save membership
		Membership actual = membershipService.save(dto);
				
		// assert
		assertEquals(expected,  actual);
	}

	@Test
	@DisplayName("Delete membership")
	@Transactional
    public void testDeleteMembership() {
		// create membership
		Membership expected = TestUtils.createMembership();
		
		// configure mock
		when(membershipRepository.findByUid(expected.getUid())).thenReturn(expected).thenThrow(new EntityNotFoundException(expected.getUid()));
		
		// fetch membership
		Membership actual = membershipService.findByUid(expected.getUid());
		
		// assert
		assertEquals(expected, actual);

		// delete membership
		membershipService.delete(expected.getUid());
		
		// fetch membership again
		PricelineApiException exception = assertThrows(PricelineApiException.class, () -> {
			membershipService.findByUid(expected.getUid());
	    });
		
		// assert
		String message = messageService.getMessage(MessageEnum.EXCEPTION_ENTITY_NOT_FOUND_ERR, expected.getUid());
		assertEquals(exception.getMessage(), message);
	}
	
	@Test
	@DisplayName("Delete membership with unexpected exception")
	@Transactional
    public void testDeleteMembershipWithUnexpectedException() {
		// create random id
		String uid = UUID.randomUUID().toString();
		
		// configure mock
		doThrow(new PersistenceException()).when(membershipRepository).deleteByUid(uid);
		
		// delete membership
		PricelineApiException exception = assertThrows(PricelineApiException.class, () -> {
			membershipService.delete(uid);
	    });
		
		// assert
		String message = messageService.getMessage(MessageEnum.MEMBERSHIP_ERROR_DELETE_HELP, uid);
		assertEquals(exception.getHelp(), message);
	}

	@Test
	@DisplayName("Validate dto without user id")
	public void testValidateDTOWithoutUserId() {
		// create DTO
		MembershipDTO dto = TestUtils.createMembershipDTO();
		dto.setUserId(null);
		
		// validate
		PricelineApiException exception = assertThrows(PricelineApiException.class, () -> {
			membershipService.validate(dto);
	    });
		
		// assert
		String message = messageService.getMessage(MessageEnum.VALIDATION_FAILURE_REQUIRED_ERR, "userId");
		assertEquals(exception.getMessage(), message);
	}
	
	@Test
	@DisplayName("Validate dto with user id that exceeds character limit")
	public void testValidateDTOWithUserIdThatExceedsCharacterLimit() {
		// create DTO
		MembershipDTO dto = TestUtils.createMembershipDTO();
		dto.setUserId(RandomStringUtils.randomAlphabetic(41));
		
		// validate
		PricelineApiException exception = assertThrows(PricelineApiException.class, () -> {
			membershipService.validate(dto);
	    });
		
		// assert
		String message = messageService.getMessage(MessageEnum.VALIDATION_FAILURE_STRING_MAX_LENGTH_ERR, "userId", 40);
		assertEquals(exception.getMessage(), message);
	}
	
	@Test
	@DisplayName("Validate dto without team id")
	public void testValidateDTOWithoutTeamId() {
		// create DTO
		MembershipDTO dto = TestUtils.createMembershipDTO();
		dto.setTeamId(null);
		
		// validate
		PricelineApiException exception = assertThrows(PricelineApiException.class, () -> {
			membershipService.validate(dto);
	    });
		
		// assert
		String message = messageService.getMessage(MessageEnum.VALIDATION_FAILURE_REQUIRED_ERR, "teamId");
		assertEquals(exception.getMessage(), message);
	}
	
	@Test
	@DisplayName("Validate dto with team id that exceeds character limit")
	public void testValidateDTOWithTeamIdThatExceedsCharacterLimit() {
		// create DTO
		MembershipDTO dto = TestUtils.createMembershipDTO();
		dto.setTeamId(RandomStringUtils.randomAlphabetic(41));
		
		// validate
		PricelineApiException exception = assertThrows(PricelineApiException.class, () -> {
			membershipService.validate(dto);
	    });
		
		// assert
		String message = messageService.getMessage(MessageEnum.VALIDATION_FAILURE_STRING_MAX_LENGTH_ERR, "teamId", 40);
		assertEquals(exception.getMessage(), message);
	}
	
	@Test
	@DisplayName("Validate dto with nonexistent user")
	public void testValidateDTOWithNonexistentUser() {
		// create DTO
		MembershipDTO dto = TestUtils.createMembershipDTO();
		
		// configure mock
		when(pricelineFacade.findUserById(any())).thenReturn(null);
		
		// validate
		PricelineApiException exception = assertThrows(PricelineApiException.class, () -> {
			membershipService.validate(dto);
	    });
		
		// assert
		String message = messageService.getMessage(MessageEnum.USER_API_USER_NOT_FOUND_HELP, dto.getUserId());
		assertEquals(exception.getMessage(), message);
	}
	
	@Test
	@DisplayName("Validate dto with nonexistent team")
	public void testValidateDTOWithNonexistentTeam() {
		// create DTO
		MembershipDTO dto = TestUtils.createMembershipDTO();
		
		// create user DTO
		UserDTO userDTO = new UserDTO();
		userDTO.setId(dto.getUserId());
				
		// configure mock
		when(pricelineFacade.findUserById(any())).thenReturn(userDTO);
		when(pricelineFacade.findTeamById(any())).thenReturn(null);
		
		// validate
		PricelineApiException exception = assertThrows(PricelineApiException.class, () -> {
			membershipService.validate(dto);
	    });
		
		// assert
		String message = messageService.getMessage(MessageEnum.TEAM_API_TEAM_NOT_FOUND_HELP, dto.getTeamId());
		assertEquals(exception.getMessage(), message);
	}
	
	@Test
	@DisplayName("Validate dto with nonexistent role")
	public void testValidateDTOWithNonexistentRole() {
		// create DTO
		MembershipDTO dto = TestUtils.createMembershipDTO();
		dto.getRole().setUid(UUID.randomUUID().toString());
		
		// create user DTO
		UserDTO userDTO = new UserDTO();
		userDTO.setId(dto.getUserId());
		
		// create team DTO
		TeamDTO teamDTO = new TeamDTO();
		teamDTO.setId(dto.getTeamId());
		
		// create exception
		String description = messageService.getMessage(MessageEnum.EXCEPTION_ENTITY_NOT_FOUND_DESCRIPTION, new Object[] {});
		String errorMessage = messageService.getMessage(MessageEnum.EXCEPTION_ENTITY_NOT_FOUND_ERR, dto.getRole().getUid());
		String help = messageService.getMessage(MessageEnum.EXCEPTION_ENTITY_NOT_FOUND_HELP, new Object[] {});
		PricelineApiException expectedException = new PricelineApiException(description, errorMessage, help, HttpStatus.NOT_FOUND);
				
		// configure mock
		when(pricelineFacade.findUserById(any())).thenReturn(userDTO);
		when(pricelineFacade.findTeamById(any())).thenReturn(teamDTO);
		when(roleService.findByUid(anyString())).thenThrow(expectedException);

		// validate
		PricelineApiException exception = assertThrows(PricelineApiException.class, () -> {
			membershipService.validate(dto);
	    });
		
		// assert
		String message = messageService.getMessage(MessageEnum.EXCEPTION_ENTITY_NOT_FOUND_ERR, dto.getRole().getUid());
		assertEquals(exception.getMessage(), message);
	}
	
	@Test
	@DisplayName("Validate dto with membership that already exists")
	public void testValidateDTOWithMembershipThatAlreadyExists() {
		// create DTO
		MembershipDTO dto = TestUtils.createMembershipDTO();
		
		// create user DTO
		UserDTO userDTO = new UserDTO();
		userDTO.setId(dto.getUserId());
		
		// create team DTO
		TeamDTO teamDTO = new TeamDTO();
		teamDTO.setId(dto.getTeamId());
				
		// configure mock
		when(pricelineFacade.findUserById(any())).thenReturn(userDTO);
		when(pricelineFacade.findTeamById(any())).thenReturn(teamDTO);
		when(roleService.findByUid(anyString())).thenReturn(dto.getRole());
		when(membershipRepository.findByUserIdAndTeamIdAndRole(anyString(), anyString(), any())).thenReturn(dto.toMembership());

		// validate
		PricelineApiException exception = assertThrows(PricelineApiException.class, () -> {
			membershipService.validate(dto);
	    });
		
		// assert
		String message = messageService.getMessage(MessageEnum.VALIDATION_FAILURE_UNIQUENESS_ERR);
		assertEquals(exception.getMessage(), message);
	}

}
