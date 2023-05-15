package com.priceline.role.utils;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.priceline.role.dto.MembershipDTO;
import com.priceline.role.dto.RoleDTO;
import com.priceline.role.dto.TeamDTO;
import com.priceline.role.dto.UserDTO;
import com.priceline.role.model.Membership;
import com.priceline.role.model.Role;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestUtils {
	
	private static Random random = new Random();
	
    // Role
    public static Role createRole() {
    	return createRole(true);
    }
    
    public static Role createRole(boolean defaultRole) {
    	Role role = new Role();
    	role.setUid(UUID.randomUUID().toString());
    	role.setName("Role " + random.nextInt() + System.currentTimeMillis());
    	role.setDefaultRole(defaultRole);

    	return role;
    }

	public static RoleDTO createRoleDTO(boolean defaultRole) {
		RoleDTO dto = new RoleDTO();
		dto.setName("Role " + random.nextInt() + System.currentTimeMillis());
		dto.setDefaultRole(defaultRole);
		
		return dto;
	}
    
    // Membership
    public static Membership createMembership() {
    	return createMembership(createRole(true));
    }
    
    public static Membership createMembership(Role role) {
    	Membership membership = new Membership();
    	membership.setUid(UUID.randomUUID().toString());
    	membership.setUserId(UUID.randomUUID().toString());
    	membership.setTeamId(UUID.randomUUID().toString());
        membership.setRole(role);
    	
    	return membership;
    }
    
    public static MembershipDTO createMembershipDTO() {
    	return createMembershipDTO(createRole(true));
    }
    
	public static MembershipDTO createMembershipDTO(Role role) {
		MembershipDTO dto = new MembershipDTO();
		dto.setUserId(UUID.randomUUID().toString());
		dto.setTeamId(UUID.randomUUID().toString());
		dto.setRole(role);
		
		return dto;
	}
	
	// UserDTO
	public static UserDTO createUserDTO() {
		UserDTO dto = new UserDTO();
		dto.setId(UUID.randomUUID().toString());
		dto.setFirstName(RandomStringUtils.randomAlphabetic(20));
		dto.setLastName(RandomStringUtils.randomAlphabetic(20));
		dto.setDisplayName(RandomStringUtils.randomAlphabetic(40));
		dto.setAvatarUrl(RandomStringUtils.randomAlphabetic(20));
		
		return dto;
	}
	
	// TeamDTO
	public static TeamDTO createTeamDTO() {
		TeamDTO dto = new TeamDTO();
		dto.setId(UUID.randomUUID().toString());
		dto.setName(RandomStringUtils.randomAlphabetic(20));
		dto.setTeamLeadId(UUID.randomUUID().toString());
		dto.setTeamMemberIds(new ArrayList<>());
		
		// add team members
		dto.getTeamMemberIds().add(UUID.randomUUID().toString());
		dto.getTeamMemberIds().add(UUID.randomUUID().toString());
		dto.getTeamMemberIds().add(UUID.randomUUID().toString());
		dto.getTeamMemberIds().add(UUID.randomUUID().toString());
		dto.getTeamMemberIds().add(UUID.randomUUID().toString());
				
		return dto;
	}
	
	// Json utils
	public static <T> T convertToObject(String value, Type type) {
        try {
            return localDateTimeGson.fromJson(value, type);
        } catch (Exception e) {
        	log.error("convertToObject(String, Type). String: " + value);
            log.error("Error: " + e.getMessage());
            throw e;
        }
    }

	public static String asJsonString(final Object object) {
		if(object == null) {
            return "{}";
        }

        try {
            return localDateTimeGson.toJson(object);
        } catch (Exception e) {
            log.error("parseObjectToString(Object). Object: " + object);
            log.error("Error: " + e.getMessage());
            throw e;
        }
    }
	
	public static JsonObject parseToJson(String value) {
        if(value == null) {
            return null;
        }

        try {
            return JsonParser.parseString(value).getAsJsonObject();
        } catch (Exception e) {
            log.error("parseToJson(String). String: " + value);
            log.error("Error: " + e.getMessage());
            throw e;
        }
    }
	
	public static <T> List<T> convertResponseToObjectList(String response, Class<T> aClass) {
        JsonObject json = parseToJson(response);
        JsonArray content = json.getAsJsonArray("content");
        Object [] array = (Object[]) java.lang.reflect.Array.newInstance(aClass, 0);
        array = localDateTimeGson.fromJson(content, array.getClass());
        List<T> list = new ArrayList<>();
        for (Object o : array) {
            list.add(aClass.cast(o));
        }

        return list;
    }
	
	private static final Gson localDateTimeGson = new GsonBuilder()
        .registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
            @Override
            public LocalDateTime deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                return LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"));
            }
        }).create();

}
