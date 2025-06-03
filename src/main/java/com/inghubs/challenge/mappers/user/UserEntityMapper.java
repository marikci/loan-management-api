package com.inghubs.challenge.mappers.user;

import com.inghubs.challenge.entities.UserEntity;
import com.inghubs.challenge.services.models.user.UserModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserEntityMapper {
  UserModel toModel(UserEntity entity);
}
