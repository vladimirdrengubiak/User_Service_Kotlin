package com.example.userapp.domain.user.mapper

import com.example.userapp.domain.user.dto.UserDTO
import com.example.userapp.domain.user.entity.User
import org.springframework.stereotype.Component

@Component
class UserMapper {
    fun toUser(userDTO: UserDTO): User = User(
        id = userDTO.id,
        name = userDTO.name,
        username = userDTO.username,
        password = userDTO.password,
        role = userDTO.role
    )

    fun fromUser(user: User): UserDTO = UserDTO(
        id = user.id,
        name = user.name,
        username = user.username,
        password = "",
        role = user.role
    )
}
