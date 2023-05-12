package com.priceline.role.service;

import java.util.List;

import com.priceline.role.dto.MembershipDTO;
import com.priceline.role.model.Membership;
import com.priceline.role.model.Role;
import com.priceline.role.service.base.AbstractDelete;
import com.priceline.role.service.base.AbstractRead;
import com.priceline.role.service.base.AbstractSave;
import com.priceline.role.service.base.AbstractValidate;

public abstract class MembershipService implements AbstractRead<Membership>,
	AbstractSave<Membership, MembershipDTO>, AbstractDelete, AbstractValidate<MembershipDTO> {
	
	public abstract Role findRoleOfMembership(String membershipUid);
	
	public abstract List<Membership> findMembershipsOfRole(String roleUid);

}
