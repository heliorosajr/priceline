package com.priceline.role.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.priceline.role.dto.TeamDTO;
import com.priceline.role.dto.UserDTO;
import com.priceline.role.enums.MessageEnum;
import com.priceline.role.model.exception.PricelineApiException;
import com.priceline.role.service.system.ExceptionService;

@Service
public class PricelineFacade {

	private RestTemplate restTemplate = new RestTemplate();
	
	@Autowired
	private ExceptionService exceptionService;
	
	@Value("${base.url.userApi}")
	private String baseUrlUserApi;
	
	@Value("${base.url.teamApi}")
	private String baseUrlTeamApi;
	
	public UserDTO findUserById(String userId) throws PricelineApiException {
		UserDTO userDTO = null;
		final String url = baseUrlUserApi + "/{userId}";
		
		try {
			userDTO = restTemplate.getForObject(url, UserDTO.class, userId);
		} catch (Exception exception) {
			throw exceptionService.throwRuntimeException(exception, MessageEnum.USER_API_ERROR_FIND_BY_ID_HELP);
		}
		
		return userDTO;
	}
	
	public TeamDTO findTeamById(String teamId) throws PricelineApiException  {
		TeamDTO teamDTO = null;
		final String url = baseUrlTeamApi + "/{teamId}";
		
		try {
			teamDTO = restTemplate.getForObject(url, TeamDTO.class, teamId);
		} catch (Exception exception) {
			throw exceptionService.throwRuntimeException(exception, MessageEnum.TEAM_API_ERROR_FIND_BY_ID_HELP);
		}
		
		return teamDTO;
	}

}
