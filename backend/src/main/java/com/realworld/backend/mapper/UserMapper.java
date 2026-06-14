package com.realworld.backend.mapper;

import com.realworld.backend.user.dto.ResponseProfile.ResponseProfileDetails;
import com.realworld.backend.user.dto.ResponseUser.ResponseUserDetails;
import com.realworld.backend.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  ResponseUserDetails userToDetail(User user);

  @Mapping(source = "following", target = "following")
  ResponseProfileDetails userToProfile(User user, boolean following);

}
