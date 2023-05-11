package com.priceline.role.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.priceline.role.controller.restfull.annotation.ApiErrorResponses;
import com.priceline.role.controller.restfull.assembler.RoleModelAssembler;
import com.priceline.role.model.Role;
import com.priceline.role.model.exception.PricelineApiException;
import com.priceline.role.service.RoleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/role")
@Tag(name = "Role", description = "Endpoints to work with roles")
public class RoleController {
	
	@Autowired
    private RoleService roleService;

    @Autowired
    private RoleModelAssembler assembler;

    @Operation(summary = "Get role by id")
    @ApiResponse(responseCode = "200", description = "Country was found",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation =  Role.class)) })
    @ApiErrorResponses
    @GetMapping("/{id}")
    public ResponseEntity<?> findByUid(
            @Parameter(description = "The role id") @PathVariable String id) throws PricelineApiException {
        EntityModel<Role> entityModel = assembler.toModel(roleService.findByUid(id));

        return ResponseEntity.status(HttpStatus.OK).body(entityModel);
    }

    @Operation(summary = "List all roles")
    @ApiResponse(responseCode = "200", description = "Role list retrieved", content = {
        @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = Role.class)))})
    @ApiErrorResponses
    @GetMapping("/all")
    public ResponseEntity<?> findAll() throws PricelineApiException {
        CollectionModel<EntityModel<Role>> collectionModel = assembler.toCollectionModel(roleService.findAll());

        return ResponseEntity.status(HttpStatus.OK).body(collectionModel);
    }

}
