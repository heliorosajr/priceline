package com.priceline.role.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.priceline.role.dto.RoleDTO;
import com.priceline.role.enums.MessageEnum;
import com.priceline.role.model.Role;
import com.priceline.role.model.exception.EntityNotFoundException;
import com.priceline.role.model.exception.PricelineApiException;
import com.priceline.role.repository.RoleRepository;
import com.priceline.role.service.system.ExceptionService;
import com.priceline.role.service.system.ValidationService;

@Service
public class RoleServiceImpl implements RoleService {
	
	@Autowired
    private RoleRepository roleRepository;
	
    @Autowired
    private ExceptionService exceptionService;
    
    @Autowired
    private ValidationService validationService;

    // ----------------------------------------------------
    // Read
    // ----------------------------------------------------
    public Role findByUid(String uid) throws PricelineApiException {
        try {
            return Optional.ofNullable(roleRepository.findByUid(uid)).orElseThrow(() -> new EntityNotFoundException(uid));
        } catch (Exception exception) {
            throw exceptionService.throwRuntimeException(exception, MessageEnum.ROLE_ERROR_FIND_BY_ID_HELP, uid);
        }
    }

    public List<Role> findAll() throws PricelineApiException {
        try {
            return roleRepository.findAll();
        } catch (Exception exception) {
            throw exceptionService.throwRuntimeException(exception, MessageEnum.ROLE_ERROR_FIND_ALL_HELP);
        }
    }

    public Role findDefaultRole() throws PricelineApiException {
        try {
            return Optional.ofNullable(roleRepository.findByDefaultRoleTrue()).orElseThrow(() -> new EntityNotFoundException("default"));
        } catch (Exception exception) {
            throw exceptionService.throwRuntimeException(exception, MessageEnum.ROLE_ERROR_FIND_DEFAULT_HELP);
        }
    }
    
    // ----------------------------------------------------
    // Persist
    // ----------------------------------------------------
    public Role save(RoleDTO dto) throws PricelineApiException {    	
        // validate DTO before saving
    	dto.setUid(null);
    	validate(dto);
    	
    	// convert DTO to object and create UUID
    	Role role = dto.toRole();
    	role.setUid(UUID.randomUUID().toString());
    	
    	if(dto.isDefaultRole()) {
    		updateDefaultRole();
    	}
    	
    	return roleRepository.save(role);
    }

    public Role update(RoleDTO dto, String uid) throws PricelineApiException {
    	// validate DTO before saving
    	dto.setUid(uid);
    	validate(dto);
    	
    	Role role = findByUid(uid);
    	role.setName(dto.getName());

        return roleRepository.save(role);
    }

    public Role updateDefaultRole(String uid) throws PricelineApiException {
    	// load current default role
    	Role oldDefaultRole = findDefaultRole();
    	
    	if(oldDefaultRole.getUid().equals(uid)) {
    		return oldDefaultRole;
    	}
    	
    	// load role to ensure it exists
    	Role newDefaultRole = findByUid(uid);
    	
    	// update statuses
    	oldDefaultRole.setDefaultRole(false);
    	newDefaultRole.setDefaultRole(true);
    	roleRepository.saveAll(List.of(oldDefaultRole, newDefaultRole));
    	
    	return newDefaultRole;
    }
    
    private void updateDefaultRole() throws PricelineApiException {
    	// load current default role and update
    	Role oldDefaultRole = findDefaultRole();
    	oldDefaultRole.setDefaultRole(false);
    	
    	roleRepository.save(oldDefaultRole);
    }
    
    // ----------------------------------------------------
    // Delete
    // ----------------------------------------------------
    public void delete(String uid) throws PricelineApiException {
    	 try {
	        roleRepository.deleteByUid(uid);
	    } catch (Exception exception) {
	    	throw exceptionService.throwRuntimeException(exception, MessageEnum.ROLE_ERROR_DELETE_HELP, uid);
	    }
    }

    // ----------------------------------------------------
    // Validation
    // ----------------------------------------------------
    public void validate(RoleDTO dto) throws PricelineApiException {
    	// name is required
    	validationService.validateRequired(dto.getName(), "name");
    	
    	// name cannot exceed 150 characters
    	validationService.validateStringMaxLength(dto.getName(), "name", 150);
    	
    	// ensure name is unique
    	Role role = roleRepository.findByName(dto.getName());
    	validationService.validateUniqueness(dto.getUid(), role);

    }

}
