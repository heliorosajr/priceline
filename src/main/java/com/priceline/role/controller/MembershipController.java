package com.priceline.role.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.priceline.role.controller.restfull.annotation.ApiErrorResponses;
import com.priceline.role.controller.restfull.assembler.MembershipModelAssembler;
import com.priceline.role.controller.restfull.assembler.RoleModelAssembler;
import com.priceline.role.dto.MembershipDTO;
import com.priceline.role.model.Membership;
import com.priceline.role.model.Role;
import com.priceline.role.model.exception.PricelineApiException;
import com.priceline.role.service.MembershipService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/membership")
@Tag(name = "Membership", description = "Endpoints to work with memberships")
public class MembershipController {
	
    private final MembershipService membershipService;

    private final MembershipModelAssembler assembler;

    private final RoleModelAssembler roleAssembler;
    
    public MembershipController(MembershipService membershipService,
    		MembershipModelAssembler assembler, RoleModelAssembler roleAssembler) {
    	this.membershipService = membershipService;
    	this.assembler = assembler;
    	this.roleAssembler = roleAssembler;
    }

    @Operation(summary = "Get membership by id")
    @ApiResponse(responseCode = "200", description = "Membership was found",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation =  Membership.class)) })
    @ApiErrorResponses
    @GetMapping("/{uid}")
    public ResponseEntity<?> findByUid(
            @Parameter(description = "The membership id") @PathVariable String uid) throws PricelineApiException {
        EntityModel<Membership> entityModel = assembler.toModel(membershipService.findByUid(uid));

        return ResponseEntity.status(HttpStatus.OK).body(entityModel);
    }

    @Operation(summary = "List all memberships")
    @ApiResponse(responseCode = "200", description = "Membership list retrieved", content = {
        @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = Membership.class)))})
    @ApiErrorResponses
    @GetMapping("/all")
    public ResponseEntity<?> findAll() throws PricelineApiException {
        CollectionModel<EntityModel<Membership>> collectionModel = assembler.toCollectionModel(membershipService.findAll());

        return ResponseEntity.status(HttpStatus.OK).body(collectionModel);
    }
    
    @Operation(summary = "Get role by membership")
    @ApiResponse(responseCode = "200", description = "Role was found",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation =  Role.class)) })
    @ApiErrorResponses
    @GetMapping("/role-of-membership/{membershipUid}")
    public ResponseEntity<?> findRoleOfMembership(
            @Parameter(description = "The membership id") @PathVariable String membershipUid) throws PricelineApiException {
        EntityModel<Role> entityModel = roleAssembler.toModel(membershipService.findRoleOfMembership(membershipUid));

        return ResponseEntity.status(HttpStatus.OK).body(entityModel);
    }
    
    @Operation(summary = "List memberships of role")
    @ApiResponse(responseCode = "200", description = "Membership list retrieved", content = {
        @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = Membership.class)))})
    @ApiErrorResponses
    @GetMapping("/by-role/{roleUid}")
    public ResponseEntity<?> findMembershipsOfRole(
    		@Parameter(description = "The role id") @PathVariable String roleUid) throws PricelineApiException {
        CollectionModel<EntityModel<Membership>> collectionModel = assembler.toCollectionModel(membershipService.findMembershipsOfRole(roleUid));

        return ResponseEntity.status(HttpStatus.OK).body(collectionModel);
    }

    @PostMapping
    @Operation(summary = "Add membership")
    @ApiResponse(responseCode = "201", description = "Created", content = {
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = Membership.class)))})
    @ApiErrorResponses
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<?> create(@RequestBody MembershipDTO membershipDTO) throws PricelineApiException {
      EntityModel<Membership> entityModel = assembler.toModel(membershipService.save(membershipDTO));

      return ResponseEntity
          .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
          .body(entityModel);
    }
    
    @DeleteMapping("/{uid}")
    @Operation(summary = "Delete membership")
    @ApiResponse(responseCode = "204", description = "Deleted")
    @ApiErrorResponses
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public ResponseEntity<?> delete(@PathVariable String uid) throws PricelineApiException {
    	membershipService.delete(uid);

      return ResponseEntity.noContent().build();

    }

}
