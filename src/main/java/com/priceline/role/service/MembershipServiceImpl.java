package com.priceline.role.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.priceline.role.dto.MembershipDTO;
import com.priceline.role.enums.MessageEnum;
import com.priceline.role.model.Membership;
import com.priceline.role.model.Role;
import com.priceline.role.model.exception.EntityNotFoundException;
import com.priceline.role.model.exception.PricelineApiException;
import com.priceline.role.repository.MembershipRepository;
import com.priceline.role.service.system.ExceptionService;
import com.priceline.role.service.system.ValidationService;

@Service
public class MembershipServiceImpl extends MembershipService {
	
	@Autowired
    private MembershipRepository membershipRepository;
	
    @Autowired
    private ExceptionService exceptionService;
    
    @Autowired
    private RoleService roleService;
    
    @Autowired
    private ValidationService validationService;

    // ----------------------------------------------------
    // Read
    // ----------------------------------------------------
    public Membership findByUid(String uid) throws PricelineApiException {
        try {
            return Optional.ofNullable(membershipRepository.findByUid(uid)).orElseThrow(() -> new EntityNotFoundException(uid));
        } catch (Exception exception) {
            throw exceptionService.throwRuntimeException(exception, MessageEnum.MEMBERSHIP_ERROR_FIND_BY_ID_HELP, uid);
        }
    }

    public List<Membership> findAll() throws PricelineApiException {
        try {
            return membershipRepository.findAll();
        } catch (Exception exception) {
            throw exceptionService.throwRuntimeException(exception, MessageEnum.MEMBERSHIP_ERROR_FIND_ALL_HELP);
        }
    }

    public Role findRoleOfMembership(String membershipUid) {
    	try {
            Membership membership = membershipRepository.findByUid(membershipUid);
            
            return membership.getRole();
        } catch (Exception exception) {
            throw exceptionService.throwRuntimeException(exception, MessageEnum.MEMBERSHIP_ERROR_FIND_ROLE_OF_MEMBERSHIP_HELP);
        }
    }
	
	public List<Membership> findMembershipsOfRole(String roleUid) {
		try {
            return membershipRepository.findByRole_uid(roleUid);
        } catch (Exception exception) {
            throw exceptionService.throwRuntimeException(exception, MessageEnum.MEMBERSHIP_ERROR_FIND_MEMBERSHIPS_OF_ROLE_HELP);
        }
	}
    
    // ----------------------------------------------------
    // Persist
    // ----------------------------------------------------
    public Membership save(MembershipDTO dto) throws PricelineApiException {
        // validate DTO before saving
    	dto.setUid(null);
    	validate(dto);
    	
    	// convert DTO to object and create UUID
    	Membership role = dto.toMembership();
    	role.setUid(UUID.randomUUID().toString());
    	
    	return membershipRepository.save(role);
    }

    // ----------------------------------------------------
    // Delete
    // ----------------------------------------------------
    public void delete(String uid) throws PricelineApiException {
    	 try {
    		 membershipRepository.deleteByUid(uid);
	    } catch (Exception exception) {
	    	throw exceptionService.throwRuntimeException(exception, MessageEnum.MEMBERSHIP_ERROR_DELETE_HELP, uid);
	    }
    }

    // ----------------------------------------------------
    // Validation
    // ----------------------------------------------------
    public void validate(MembershipDTO dto) throws PricelineApiException {
    	// user id is required
    	validationService.validateRequired(dto.getUserId(), "userId");
    	validationService.validateStringMaxLength(dto.getUserId(), "userId", 40);
    	// TODO validate user with API
    	
    	// team id is required
    	validationService.validateRequired(dto.getTeamId(), "teamId");
    	validationService.validateStringMaxLength(dto.getUserId(), "teamId", 40);
    	// TODO validate team with API
    	
    	// load role
    	Role role;
    	
    	if(dto.getRole() == null) { // if not informed, use default
    		role =roleService.findDefaultRole();
    	} else {
    		role = roleService.findByUid(dto.getRole().getUid());
    	}
    	
    	dto.setRole(role);
    }

}
