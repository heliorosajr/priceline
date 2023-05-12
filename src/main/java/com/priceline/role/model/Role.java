package com.priceline.role.model;

import com.priceline.role.constants.DatabaseConstants;
import com.priceline.role.model.base.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity(name = DatabaseConstants.TABLE_ROLE)
public class Role extends BaseEntity {

    @Column(length = 150, unique = true, nullable = false)
    private String name;
    
    @Column(name = "default_role")
    private boolean defaultRole;

}