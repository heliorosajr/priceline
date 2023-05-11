package com.priceline.role.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.priceline.role.dto.RoleDTO;
import com.priceline.role.enums.MessageEnum;
import com.priceline.role.model.Role;
import com.priceline.role.model.exception.EntityNotFoundException;
import com.priceline.role.model.exception.PricelineApiException;
import com.priceline.role.repository.RoleRepository;
import com.priceline.role.service.system.ExceptionService;

@Service
public class RoleServiceImpl implements RoleService {
	
	@Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ExceptionService exceptionService;

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


    // ----------------------------------------------------
    // Persist
    // ----------------------------------------------------
    public Role save(RoleDTO dto) throws PricelineApiException {
        // TODO
    	return null;
    }

    public Role update(RoleDTO dto, String uid) throws PricelineApiException {
    	// TODO
        return null;
    }

    // ----------------------------------------------------
    // Delete
    // ----------------------------------------------------
    public void delete(String id) throws PricelineApiException {
    	// TODO
    }

    // ----------------------------------------------------
    // Validation
    // ----------------------------------------------------
    public void validate(RoleDTO dto) throws PricelineApiException {
    	// TODO
    }

}
