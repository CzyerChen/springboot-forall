package com.mappers.mapper;

import com.mappers.domain.ProfilePO;
import com.mappers.domain.dto.ProfileDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    ProfileDTO profilePOToProfileDTO(ProfilePO profilePO);

    void updateProfileDTOFromProfilePO(ProfileDTO profileDTO, @MappingTarget ProfilePO profilePO);

    //内容一致的话，可以不用写映射关系
    /*@Mappings({
            @Mapping(source = "id",target = "id"),
            @Mapping(source = "name",target = "name"),
            @Mapping(source = "age",target = "age")
    })
    ProfileDTO profilePOToProfileDTO(ProfilePO profilePO);

    public static ProfileMapper INSTANCE = Mappers.getMapper(ProfileMapper.class);

    直接通过ProfileMapper.INSTANCE.profilePOToProfileDTO(.......)
    */
}
