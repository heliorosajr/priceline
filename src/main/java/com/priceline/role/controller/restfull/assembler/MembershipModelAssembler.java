package com.priceline.role.controller.restfull.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.priceline.role.controller.MembershipController;
import com.priceline.role.model.Membership;

import lombok.SneakyThrows;

@Component
public class MembershipModelAssembler implements RepresentationModelAssembler<Membership, EntityModel<Membership>> {

    @SneakyThrows
    @Override
    public EntityModel<Membership> toModel(Membership membership) {
        return EntityModel.of(membership,
            linkTo(methodOn(MembershipController.class).findByUid(membership.getUid())).withSelfRel(),
            linkTo(methodOn(MembershipController.class).findAll()).withRel("memberships")
        );
    }

    @SneakyThrows
    public CollectionModel<EntityModel<Membership>> toCollectionModel(List<Membership> membershipList) {
        List<EntityModel<Membership>> entityModelList = membershipList.stream()
                .map(this::toModel).collect(Collectors.toList());

        return CollectionModel.of(entityModelList, linkTo(methodOn(MembershipController.class).findAll()).withSelfRel());
    }
}