package com.example.userapp.domain.user.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class UsernameAlreadyExistsException(message: String) : RuntimeException(message)
