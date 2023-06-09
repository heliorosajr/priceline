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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.priceline.role.controller.restfull.annotation.ApiErrorResponses;
import com.priceline.role.controller.restfull.assembler.RoleModelAssembler;
import com.priceline.role.dto.RoleDTO;
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
	
    private final RoleService roleService;

    private final RoleModelAssembler assembler;
    
    public RoleController(RoleService roleService, RoleModelAssembler assembler) {
    	this.roleService = roleService;
    	this.assembler = assembler;
    }

    @Operation(summary = "Get role by id")
    @ApiResponse(responseCode = "200", description = "Role was found",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation =  Role.class)) })
    @ApiErrorResponses
    @GetMapping("/{uid}")
    public ResponseEntity<?> findByUid(
            @Parameter(description = "The role id") @PathVariable String uid) throws PricelineApiException {
        EntityModel<Role> entityModel = assembler.toModel(roleService.findByUid(uid));

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

    @Operation(summary = "Get default role")
    @ApiResponse(responseCode = "200", description = "Role was found",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation =  Role.class)) })
    @ApiErrorResponses
    @GetMapping("/default")
    public ResponseEntity<?> findDefaultRole() throws PricelineApiException {
        EntityModel<Role> entityModel = assembler.toModel(roleService.findDefaultRole());

        return ResponseEntity.status(HttpStatus.OK).body(entityModel);
    }

    @PostMapping
    @Operation(summary = "Add role")
    @ApiResponse(responseCode = "201", description = "Created", content = {
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = Role.class)))})
    @ApiErrorResponses
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<?> create(@RequestBody RoleDTO roleDTO) {
      EntityModel<Role> entityModel = assembler.toModel(roleService.save(roleDTO));

      return ResponseEntity
          .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
          .body(entityModel);
    }
    
    @PutMapping("/{uid}")
    @Operation(summary = "Update role")
    @ApiResponse(responseCode = "200", description = "Updated", content = {
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = Role.class)))})
    @ApiErrorResponses
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<?> update(@RequestBody RoleDTO roleDTO, @PathVariable String uid) throws PricelineApiException {
      EntityModel<Role> entityModel = assembler.toModel(roleService.update(roleDTO, uid));

      return ResponseEntity
          .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
          .body(entityModel);
    }
    
    @PutMapping("/set-default/{uid}")
    @Operation(summary = "Set role as default")
    @ApiResponse(responseCode = "200", description = "Updated", content = {
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = Role.class)))})
    @ApiErrorResponses
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<?> updateDefaultRole(@PathVariable String uid) throws PricelineApiException {
      EntityModel<Role> entityModel = assembler.toModel(roleService.updateDefaultRole(uid));

      return ResponseEntity
          .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
          .body(entityModel);
    }
    
    @DeleteMapping("/{uid}")
    @Operation(summary = "Delete role")
    @ApiResponse(responseCode = "204", description = "Deleted")
    @ApiErrorResponses
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public ResponseEntity<?> delete(@PathVariable String uid) throws PricelineApiException {
      roleService.delete(uid);

      return ResponseEntity.noContent().build();

    }

}
