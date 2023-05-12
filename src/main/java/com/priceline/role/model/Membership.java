package com.priceline.role.model;

import com.priceline.role.constants.DatabaseConstants;
import com.priceline.role.model.base.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity(name = DatabaseConstants.TABLE_MEMBERSHIP)
public class Membership extends BaseEntity {

    @Column(length = 40, nullable = false)
    private String userId;
    
    @Column(length = 40, nullable = false)
    private String teamId;
    
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

}