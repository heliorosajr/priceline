package com.priceline.role.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.priceline.role.config.MessageConfig;
import com.priceline.role.controller.advice.ApiRestExceptionHandler;
import com.priceline.role.controller.restfull.assembler.RoleModelAssembler;
import com.priceline.role.dto.RoleDTO;
import com.priceline.role.enums.MessageEnum;
import com.priceline.role.model.Role;
import com.priceline.role.model.error.PricelineApiError;
import com.priceline.role.model.exception.PricelineApiException;
import com.priceline.role.service.RoleService;
import com.priceline.role.service.system.MessageService;
import com.priceline.role.utils.TestUtils;

@ExtendWith(MockitoExtension.class)
public class RoleControllerTest {
	
	private final String BASE_API = "/api/v1/role";

	@Mock
	private RoleService roleService;

	private RoleController roleController;
	
	@Autowired
	private MockMvc mockMvc;
	
	private MessageService messageService;
	
	@BeforeEach
	public void setup(){
		messageService = new MessageService(new MessageConfig().messageSource());
		roleController = new RoleController(roleService, new RoleModelAssembler());
		mockMvc = MockMvcBuilders.standaloneSetup(roleController).setControllerAdvice(new ApiRestExceptionHandler()).build();
	}
	
	@Test
	@DisplayName("Get role by uid")
    public void testGetRoleByUid() throws Exception {
		// create role
		Role role = TestUtils.createRole();
		
		// configure mock
	    when(roleService.findByUid(role.getUid())).thenReturn(role);
	    
	    // send request
	    MvcResult result = mockMvc.perform(
	    			MockMvcRequestBuilders.get(BASE_API + "/" + role.getUid())
	    			.contentType(MediaType.APPLICATION_JSON))
//	           	.andDo(MockMvcResultHandlers.print()) // debug purposes only
	           	.andReturn();
        
        // convert JSON to object
        Role actual = TestUtils.convertToObject(result.getResponse().getContentAsString(), Role.class);

        // assert
        assertEquals(actual, role);
	}
	
	@Test
	@DisplayName("Get all roles")
    public void testGetAllRoles() throws Exception {
		// create roles
		Role role1 = TestUtils.createRole(true);
		Role role2 = TestUtils.createRole(false);
		Role role3 = TestUtils.createRole(false);
		Role role4 = TestUtils.createRole(false);
		Role role5 = TestUtils.createRole(false);
		List<Role> expected = List.of(role1, role2, role3, role4, role5);
		
		// configure mock
		when(roleService.findAll()).thenReturn(expected);

		// send request
		MvcResult result = mockMvc.perform(
					MockMvcRequestBuilders.get(BASE_API + "/all")
					.contentType(MediaType.APPLICATION_JSON))
//				.andDo(MockMvcResultHandlers.print()) // debug purposes only
                .andReturn();
		 
		// convert JSON to list
		List<Role> actual = TestUtils.convertResponseToObjectList(result.getResponse().getContentAsString(), Role.class);

	    // assert
		assertEquals(actual, expected);
	}
	
	@Test
	@DisplayName("Get default role")
    public void testGetDefaultRole() throws Exception {
		// create role
		Role role = TestUtils.createRole(true);
		
		// configure mock
	    when(roleService.findDefaultRole()).thenReturn(role);
	    
	    // send request
	    MvcResult result = mockMvc.perform(
	    			MockMvcRequestBuilders.get(BASE_API + "/default")
	    			.contentType(MediaType.APPLICATION_JSON))
//	           	.andDo(MockMvcResultHandlers.print()) // debug purposes only
	           	.andReturn();
        
        // convert JSON to object
        Role actual = TestUtils.convertToObject(result.getResponse().getContentAsString(), Role.class);

        // assert
        assertEquals(actual, role);
	}
	
	@Test
	@DisplayName("Get default role when a default is not set")
    public void testGetDefaultRoleWhenADefaultIsNotSet() throws Exception {
		// create exception
		String description = messageService.getMessage(MessageEnum.EXCEPTION_DEFAULT_ROLE_NOT_FOUND_DESCRIPTION, new Object[] {});
		String errorMessage = messageService.getMessage(MessageEnum.EXCEPTION_DEFAULT_ROLE_NOT_FOUND_ERR, new Object[] {});
		String help = messageService.getMessage(MessageEnum.EXCEPTION_DEFAULT_ROLE_NOT_FOUND_HELP, new Object[] {});
		PricelineApiException expectedException = new PricelineApiException(description, errorMessage, help, HttpStatus.INTERNAL_SERVER_ERROR);
		
		// create error
		PricelineApiError error = new PricelineApiError(expectedException, null);
		
		// configure mock
		when(roleService.findDefaultRole()).thenThrow(expectedException);
	    
	    // send request
	    MvcResult result = mockMvc.perform(
	    			MockMvcRequestBuilders.get(BASE_API + "/default")
	    			.contentType(MediaType.APPLICATION_JSON))
//	           	.andDo(MockMvcResultHandlers.print()) // debug purposes only
	    		.andExpect(status().isInternalServerError())
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
	@DisplayName("Save role without name")
    public void testSaveRoleWithoutName() throws Exception {
		// create DTO
		RoleDTO dto = TestUtils.createRoleDTO(false);
		dto.setName(null);
		
		// create exception
		String description = messageService.getMessage(MessageEnum.VALIDATION_FAILURE_DESCRIPTION, new Object[] {});
		String errorMessage = messageService.getMessage(MessageEnum.VALIDATION_FAILURE_REQUIRED_ERR, "name");
		PricelineApiException expectedException = new PricelineApiException(description, errorMessage, null, HttpStatus.BAD_REQUEST);
		
		// create error
		PricelineApiError error = new PricelineApiError(expectedException, null);
		
		// configure mock
		doThrow(expectedException).when(roleService).save(any());
	    
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
	@DisplayName("Save role with name that exceeds character limit")
    public void testSaveRoleWithNameThatExceedsCharacterLimit() throws Exception {
		// create DTO
		RoleDTO dto = TestUtils.createRoleDTO(false);
		dto.setName(RandomStringUtils.randomAlphabetic(151));
		
		// create exception
		String description = messageService.getMessage(MessageEnum.VALIDATION_FAILURE_DESCRIPTION, new Object[] {});
		String errorMessage = messageService.getMessage(MessageEnum.VALIDATION_FAILURE_STRING_MAX_LENGTH_ERR, "name", 150);
		PricelineApiException expectedException = new PricelineApiException(description, errorMessage, null, HttpStatus.BAD_REQUEST);
		
		// create error
		PricelineApiError error = new PricelineApiError(expectedException, null);
		
		// configure mock
		doThrow(expectedException).when(roleService).save(any());
	    
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

}
