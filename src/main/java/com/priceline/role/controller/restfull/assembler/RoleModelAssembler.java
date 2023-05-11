package com.priceline.role.controller.restfull.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.priceline.role.controller.RoleController;
import com.priceline.role.model.Role;

import lombok.SneakyThrows;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class RoleModelAssembler implements RepresentationModelAssembler<Role, EntityModel<Role>> {

    @SneakyThrows
    @Override
    public EntityModel<Role> toModel(Role role) {
        return EntityModel.of(role,
            linkTo(methodOn(RoleController.class).findByUid(role.getUid())).withSelfRel(),
            linkTo(methodOn(RoleController.class).findAll()).withRel("roles")
        );
    }

    @SneakyThrows
    public CollectionModel<EntityModel<Role>> toCollectionModel(List<Role> roleList) {
        List<EntityModel<Role>> entityModelList = roleList.stream()
                .map(this::toModel).collect(Collectors.toList());

        return CollectionModel.of(entityModelList, linkTo(methodOn(RoleController.class).findAll()).withSelfRel());
    }
}