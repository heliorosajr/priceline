package com.priceline.role.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.priceline.role.config.MessageConfig;
import com.priceline.role.controller.advice.ApiRestExceptionHandler;
import com.priceline.role.controller.restfull.assembler.MembershipModelAssembler;
import com.priceline.role.controller.restfull.assembler.RoleModelAssembler;
import com.priceline.role.dto.MembershipDTO;
import com.priceline.role.enums.MessageEnum;
import com.priceline.role.model.Membership;
import com.priceline.role.model.Role;
import com.priceline.role.model.error.PricelineApiError;
import com.priceline.role.model.exception.PricelineApiException;
import com.priceline.role.service.MembershipService;
import com.priceline.role.service.system.MessageService;
import com.priceline.role.utils.TestUtils;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class MembershipControllerTest {
	
	private final String BASE_API = "/api/v1/membership";

	@Mock
	private MembershipService membershipService;

	private MembershipController memberhsipController;
	
	@Autowired
	private MockMvc mockMvc;
	
	private MessageService messageService;
	
	@BeforeEach
	public void setup(){
		messageService = new MessageService(new MessageConfig().messageSource());
		memberhsipController = new MembershipController(membershipService, new MembershipModelAssembler(), new RoleModelAssembler());
		mockMvc = MockMvcBuilders.standaloneSetup(memberhsipController).setControllerAdvice(new ApiRestExceptionHandler()).build();
	}
	
	@Test
	@DisplayName("Get membership by uid")
    public void testGetMembershipByUid() throws Exception {
		// create membership
		Membership membership = TestUtils.createMembership();
		
		// configure mock
	    when(membershipService.findByUid(membership.getUid())).thenReturn(membership);
	    
	    // send request
	    MvcResult result = mockMvc.perform(
	    			MockMvcRequestBuilders.get(BASE_API + "/" + membership.getUid())
	    			.contentType(MediaType.APPLICATION_JSON))
//	           	.andDo(MockMvcResultHandlers.print()) // debug purposes only
	           	.andReturn();
        
        // convert JSON to object
	    Membership actual = TestUtils.convertToObject(result.getResponse().getContentAsString(), Membership.class);

        // assert
        assertEquals(actual, membership);
	}
	
	@Test
	@DisplayName("Get membership by nonexistent uid")
    public void testGetMembershipByNonexistentUid() throws Exception {
		// create random id
		String id = UUID.randomUUID().toString();

		// create exception
		String description = messageService.getMessage(MessageEnum.EXCEPTION_ENTITY_NOT_FOUND_DESCRIPTION, new Object[] {});
		String errorMessage = messageService.getMessage(MessageEnum.EXCEPTION_ENTITY_NOT_FOUND_ERR, new Object[] { id });
		String help = messageService.getMessage(MessageEnum.EXCEPTION_ENTITY_NOT_FOUND_HELP, new Object[] {});

		PricelineApiException expectedException = new PricelineApiException(description, errorMessage, help, HttpStatus.NOT_FOUND);
		
		// create error
		PricelineApiError error = new PricelineApiError(expectedException, null);
		
		// configure mock
		when(membershipService.findByUid(any())).thenThrow(expectedException);
	    
	    // send request
	    MvcResult result = mockMvc.perform(
	    			MockMvcRequestBuilders.get(BASE_API + "/" + id)
	    			.contentType(MediaType.APPLICATION_JSON))
//	           	.andDo(MockMvcResultHandlers.print()) // debug purposes only
	    		.andExpect(status().isNotFound())
	           	.andReturn();
        
        // convert JSON to object
	    PricelineApiError actual = TestUtils.convertToObject(result.getResponse().getContentAsString(), PricelineApiError.class);

        // assert
        assertEquals(actual.getDescription(), error.getDescription());
        assertEquals(actual.getErrorMessage(), error.getErrorMessage());
        assertEquals(actual.getReason(), error.getReason());
        assertEquals(actual.getStatus(), error.getStatus());
	}
	
	@Test
	@DisplayName("Get all memberships")
    public void testGetAllMemberships() throws Exception {
		// create memberships
		Membership membership1 = TestUtils.createMembership();
		Membership membership2 = TestUtils.createMembership();
		Membership membership3 = TestUtils.createMembership();
		Membership membership4 = TestUtils.createMembership();
		Membership membership5 = TestUtils.createMembership();
		List<Membership> expected = List.of(membership1, membership2, membership3, membership4, membership5);
		
		// configure mock
		when(membershipService.findAll()).thenReturn(expected);

		// send request
		MvcResult result = mockMvc.perform(
					MockMvcRequestBuilders.get(BASE_API + "/all")
					.contentType(MediaType.APPLICATION_JSON))
//				.andDo(MockMvcResultHandlers.print()) // debug purposes only
                .andReturn();
		 
		// convert JSON to list
		List<Membership> actual = TestUtils.convertResponseToObjectList(result.getResponse().getContentAsString(), Membership.class);

	    // assert
		assertEquals(actual, expected);
	}

	@Test
	@DisplayName("Get role of membership")
    public void testGetRoleOfMembership() throws Exception {
		// create membership
		Membership expected = TestUtils.createMembership();
		
		// configure mock
		when(membershipService.findRoleOfMembership(expected.getRole().getUid())).thenReturn(expected.getRole());

		// send request
		MvcResult result = mockMvc.perform(
					MockMvcRequestBuilders.get(BASE_API + "/role-of-membership/" + expected.getRole().getUid())
					.contentType(MediaType.APPLICATION_JSON))
//				.andDo(MockMvcResultHandlers.print()) // debug purposes only
                .andReturn();
		 
		// convert JSON to list
		Role actual = TestUtils.convertToObject(result.getResponse().getContentAsString(), Role.class);

	    // assert
		assertEquals(actual, expected.getRole());
	}

	@Test
	@DisplayName("Get all memberships of role")
    public void testGetAllMembershipsOfRole() throws Exception {
		// create memberships
		Membership membership1 = TestUtils.createMembership();
		Membership membership2 = TestUtils.createMembership(membership1.getRole());
		Membership membership3 = TestUtils.createMembership(membership1.getRole());
		Membership membership4 = TestUtils.createMembership(membership1.getRole());
		Membership membership5 = TestUtils.createMembership(membership1.getRole());
		List<Membership> expected = List.of(membership1, membership2, membership3, membership4, membership5);
		
		// configure mock
		when(membershipService.findMembershipsOfRole(membership1.getRole().getUid())).thenReturn(expected);

		// send request
		MvcResult result = mockMvc.perform(
					MockMvcRequestBuilders.get(BASE_API + "/by-role/" + membership1.getRole().getUid())
					.contentType(MediaType.APPLICATION_JSON))
//				.andDo(MockMvcResultHandlers.print()) // debug purposes only
                .andReturn();
		 
		// convert JSON to list
		List<Membership> actual = TestUtils.convertResponseToObjectList(result.getResponse().getContentAsString(), Membership.class);

	    // assert
		assertEquals(actual, expected);
	}
	
	@Test
	@DisplayName("Save membership")
    public void testSaveMembership() throws Exception {
		// create DTO
		MembershipDTO dto = TestUtils.createMembershipDTO();
		
		// create role
		Membership expected = dto.toMembership();
		
		// configure mock
		when(membershipService.save(dto)).thenReturn(expected);
	    
	    // send request
	    MvcResult result = mockMvc.perform(
	    			MockMvcRequestBuilders.post(BASE_API)
	    			.contentType(MediaType.APPLICATION_JSON)
	    			.content(TestUtils.asJsonString(dto)))
//	           	.andDo(MockMvcResultHandlers.print()) // debug purposes only
	    		.andExpect(status().isCreated())
	           	.andReturn();
        
        // convert JSON to object
	    Membership actual = TestUtils.convertToObject(result.getResponse().getContentAsString(), Membership.class);

        // assert
        assertEquals(actual, expected);
	}
	
	@Test
	@DisplayName("Save membership without user id")
    public void testSaveMembershipWithoutUserId() throws Exception {
		// create DTO
		MembershipDTO dto = TestUtils.createMembershipDTO();
		dto.setUserId(null);
		
		// create exception
		String description = messageService.getMessage(MessageEnum.VALIDATION_FAILURE_DESCRIPTION, new Object[] {});
		String errorMessage = messageService.getMessage(MessageEnum.VALIDATION_FAILURE_REQUIRED_ERR, "userId");
		PricelineApiException expectedException = new PricelineApiException(description, errorMessage, null, HttpStatus.BAD_REQUEST);
		
		// create error
		PricelineApiError error = new PricelineApiError(expectedException, null);
		
		// configure mock
		doThrow(expectedException).when(membershipService).save(any());
	    
	    // send request
	    MvcResult result = mockMvc.perform(
	    			MockMvcRequestBuilders.post(BASE_API)
	    			.contentType(MediaType.APPLICATION_JSON)
	    			.content(TestUtils.asJsonString(dto)))
//	           	.andDo(MockMvcResultHandlers.print()) // debug purposes only
	    		.andExpect(status().is4xxClientError())
	           	.andReturn();
        
        // convert JSON to object
	    PricelineApiError actual = TestUtils.convertToObject(result.getResponse().getContentAsString(), PricelineApiError.class);

        // assert
        assertEquals(actual.getDescription(), error.getDescription());
        assertEquals(actual.getErrorMessage(), error.getErrorMessage());
        assertEquals(actual.getReason(), error.getReason());
        assertEquals(actual.getStatus(), error.getStatus());
	}
	
	@Test
	@DisplayName("Save membership with user id that exceeds character limit")
    public void testSaveMembershipWithUserIdThatExceedsCharacterLimit() throws Exception {
		// create DTO
		MembershipDTO dto = TestUtils.createMembershipDTO();
		dto.setUserId(RandomStringUtils.randomAlphabetic(41));
		
		// create exception
		String description = messageService.getMessage(MessageEnum.VALIDATION_FAILURE_DESCRIPTION, new Object[] {});
		String errorMessage = messageService.getMessage(MessageEnum.VALIDATION_FAILURE_STRING_MAX_LENGTH_ERR, "userId", 41);
		PricelineApiException expectedException = new PricelineApiException(description, errorMessage, null, HttpStatus.BAD_REQUEST);
		
		// create error
		PricelineApiError error = new PricelineApiError(expectedException, null);
		
		// configure mock
		doThrow(expectedException).when(membershipService).save(any());
	    
	    // send request
	    MvcResult result = mockMvc.perform(
	    			MockMvcRequestBuilders.post(BASE_API)
	    			.contentType(MediaType.APPLICATION_JSON)
	    			.content(TestUtils.asJsonString(dto)))
//	           	.andDo(MockMvcResultHandlers.print()) // debug purposes only
	    		.andExpect(status().is4xxClientError())
	           	.andReturn();
        
        // convert JSON to object
	    PricelineApiError actual = TestUtils.convertToObject(result.getResponse().getContentAsString(), PricelineApiError.class);

        // assert
        assertEquals(actual.getDescription(), error.getDescription());
        assertEquals(actual.getErrorMessage(), error.getErrorMessage());
        assertEquals(actual.getReason(), error.getReason());
        assertEquals(actual.getStatus(), error.getStatus());
	}

	@Test
	@DisplayName("Save membership without team id")
    public void testSaveMembershipWithoutTeamId() throws Exception {
		// create DTO
		MembershipDTO dto = TestUtils.createMembershipDTO();
		dto.setUserId(null);
		
		// create exception
		String description = messageService.getMessage(MessageEnum.VALIDATION_FAILURE_DESCRIPTION, new Object[] {});
		String errorMessage = messageService.getMessage(MessageEnum.VALIDATION_FAILURE_REQUIRED_ERR, "teamId");
		PricelineApiException expectedException = new PricelineApiException(description, errorMessage, null, HttpStatus.BAD_REQUEST);
		
		// create error
		PricelineApiError error = new PricelineApiError(expectedException, null);
		
		// configure mock
		doThrow(expectedException).when(membershipService).save(any());
	    
	    // send request
	    MvcResult result = mockMvc.perform(
	    			MockMvcRequestBuilders.post(BASE_API)
	    			.contentType(MediaType.APPLICATION_JSON)
	    			.content(TestUtils.asJsonString(dto)))
//	           	.andDo(MockMvcResultHandlers.print()) // debug purposes only
	    		.andExpect(status().is4xxClientError())
	           	.andReturn();
        
        // convert JSON to object
	    PricelineApiError actual = TestUtils.convertToObject(result.getResponse().getContentAsString(), PricelineApiError.class);

        // assert
        assertEquals(actual.getDescription(), error.getDescription());
        assertEquals(actual.getErrorMessage(), error.getErrorMessage());
        assertEquals(actual.getReason(), error.getReason());
        assertEquals(actual.getStatus(), error.getStatus());
	}
	
	@Test
	@DisplayName("Save membership with team id that exceeds character limit")
    public void testSaveMembershipWithTeamIdThatExceedsCharacterLimit() throws Exception {
		// create DTO
		MembershipDTO dto = TestUtils.createMembershipDTO();
		dto.setTeamId(RandomStringUtils.randomAlphabetic(41));
		
		// create exception
		String description = messageService.getMessage(MessageEnum.VALIDATION_FAILURE_DESCRIPTION, new Object[] {});
		String errorMessage = messageService.getMessage(MessageEnum.VALIDATION_FAILURE_STRING_MAX_LENGTH_ERR, "teamId", 41);
		PricelineApiException expectedException = new PricelineApiException(description, errorMessage, null, HttpStatus.BAD_REQUEST);
		
		// create error
		PricelineApiError error = new PricelineApiError(expectedException, null);
		
		// configure mock
		doThrow(expectedException).when(membershipService).save(any());
	    
	    // send request
	    MvcResult result = mockMvc.perform(
	    			MockMvcRequestBuilders.post(BASE_API)
	    			.contentType(MediaType.APPLICATION_JSON)
	    			.content(TestUtils.asJsonString(dto)))
//	           	.andDo(MockMvcResultHandlers.print()) // debug purposes only
	    		.andExpect(status().is4xxClientError())
	           	.andReturn();
        
        // convert JSON to object
	    PricelineApiError actual = TestUtils.convertToObject(result.getResponse().getContentAsString(), PricelineApiError.class);

        // assert
        assertEquals(actual.getDescription(), error.getDescription());
        assertEquals(actual.getErrorMessage(), error.getErrorMessage());
        assertEquals(actual.getReason(), error.getReason());
        assertEquals(actual.getStatus(), error.getStatus());
	}

	@Test
	@DisplayName("Delete membership")
    public void testDeleteMembership() throws Exception {
		// create membership
		Membership membership = TestUtils.createMembership();
		
		// configure mock
		doNothing().when(membershipService).delete(any());
	    
	    // send request
	    mockMvc.perform(
			MockMvcRequestBuilders.delete(BASE_API + "/" + membership.getUid())
			.contentType(MediaType.APPLICATION_JSON))
//			           	.andDo(MockMvcResultHandlers.print()) // debug purposes only
	    .andExpect(MockMvcResultMatchers.status().is(204));
	        
	}

}
