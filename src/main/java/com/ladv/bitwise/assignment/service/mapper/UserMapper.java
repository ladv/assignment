package com.ladv.bitwise.assignment.service.mapper;

import com.ladv.bitwise.assignment.domain.User;
import com.ladv.bitwise.assignment.service.dto.UserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserDTO userDTO);

    UserDTO toDto(User user);
}
