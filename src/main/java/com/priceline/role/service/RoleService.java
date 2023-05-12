package com.priceline.role.service;

import com.priceline.role.dto.RoleDTO;
import com.priceline.role.model.Role;
import com.priceline.role.model.exception.PricelineApiException;
import com.priceline.role.service.base.AbstractCrud;

public interface RoleService extends AbstractCrud<Role, RoleDTO> {
	
	Role findDefaultRole() throws PricelineApiException;
	
	Role updateDefaultRole(String uid) throws PricelineApiException;

}
