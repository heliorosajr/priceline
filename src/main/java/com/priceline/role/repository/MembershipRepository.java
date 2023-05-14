package com.priceline.role.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.priceline.role.model.Membership;
import com.priceline.role.model.Role;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Long> {
    
	Membership findByUid(String uid);
	
	Membership findByUserIdAndTeamIdAndRole(String userId, String teamId, Role role);
	
	List<Membership> findByRole_uid(String roleUid);
	
	void deleteByUid(String uid);

}