package com.priceline.role.facade;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;

import com.priceline.role.config.MessageConfig;
import com.priceline.role.dto.TeamDTO;
import com.priceline.role.dto.UserDTO;
import com.priceline.role.enums.MessageEnum;
import com.priceline.role.model.exception.PricelineApiException;
import com.priceline.role.service.system.ExceptionService;
import com.priceline.role.service.system.MessageService;
import com.priceline.role.utils.TestUtils;

@ExtendWith(MockitoExtension.class)
public class PricelineFacadeTest {
	
	private final String DUMMY_URI = "http://localhost:8080"; 
	
	@Mock
    private RestTemplate restTemplate;

    private PricelineFacade pricelineFacade;
    
    private MessageService messageService;
	
	@BeforeEach
    public void setUp() {
		messageService = new MessageService(new MessageConfig().messageSource());
    	ExceptionService exceptionService = new ExceptionService(messageService);
    	pricelineFacade = new PricelineFacade(exceptionService, restTemplate, DUMMY_URI, DUMMY_URI);
    }

    @Test
    @DisplayName("Get user by id")
    public void testGetUserById() {
    	// create DTO
    	UserDTO actual = TestUtils.createUserDTO();
    	
    	// configure mock
    	doReturn(actual).when(restTemplate).getForObject(any(), any(), anyString());
    	
    	// find user
        UserDTO expected = pricelineFacade.findUserById(UUID.randomUUID().toString());
 
        assertEquals(actual, expected);
    }
    
    @Test
    @DisplayName("Get user by nonexistent id")
    public void testGetUserByNonexistentUId() {
    	// create random id
    	String id = UUID.randomUUID().toString();

    	// create exception
		String description = messageService.getMessage(MessageEnum.UNKNOWN_ERROR_DESCRIPTION, new Object[] {});
		String help = messageService.getMessage(MessageEnum.USER_API_ERROR_FIND_BY_ID_HELP, new Object[] { id });

		PricelineApiException expectedException = new PricelineApiException(description, null, help, HttpStatus.NOT_FOUND);
    	
    	// configure mock
    	doThrow(expectedException).when(restTemplate).getForObject(any(), any(), anyString());
    	
    	// find user
 		PricelineApiException exception = assertThrows(PricelineApiException.class, () -> {
 			pricelineFacade.findUserById(UUID.randomUUID().toString());
 	    });
 		
 		// assert
 		String message = messageService.getMessage(MessageEnum.USER_API_ERROR_FIND_BY_ID_HELP, new Object[] { id });
 		assertEquals(exception.getHelp(), message);
    }
    
    @Test
    @DisplayName("Get team by id")
    public void testGetTeamById() {
    	// create DTO
    	TeamDTO actual = TestUtils.createTeamDTO();
    	
    	// configure mock
    	doReturn(actual).when(restTemplate).getForObject(any(), any(), anyString());
    	
    	// find team
    	TeamDTO expected = pricelineFacade.findTeamById(UUID.randomUUID().toString());
 
        assertEquals(actual, expected);
    }
    
    @Test
    @DisplayName("Get team by nonexistent id")
    public void testGetTeamByNonexistentUId() {
    	// create random id
    	String id = UUID.randomUUID().toString();

    	// create exception
		String description = messageService.getMessage(MessageEnum.UNKNOWN_ERROR_DESCRIPTION, new Object[] {});
		String help = messageService.getMessage(MessageEnum.TEAM_API_ERROR_FIND_BY_ID_HELP, new Object[] { id });

		PricelineApiException expectedException = new PricelineApiException(description, null, help, HttpStatus.NOT_FOUND);
    	
    	// configure mock
    	doThrow(expectedException).when(restTemplate).getForObject(any(), any(), anyString());
    	
    	// find user
 		PricelineApiException exception = assertThrows(PricelineApiException.class, () -> {
 			pricelineFacade.findTeamById(id);
 	    });
 		
 		// assert
 		String message = messageService.getMessage(MessageEnum.TEAM_API_ERROR_FIND_BY_ID_HELP, new Object[] { id });
 		assertEquals(exception.getHelp(), message);
    }

}
