package com.example.userapp.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.info.License
import io.swagger.v3.oas.annotations.servers.Server

@OpenAPIDefinition(
    info = Info(
        contact = Contact(
            name = "Vladimir Drengubiak",
            email = "drengubiak.v@gmail.com"
        ),
        description = "OpenApi documentation for User Service",
        title = "User Service - Vladimir Drengubiak",
        version = "1.0",
        license = License(name = "MIT License")
    ),
    servers = [
        Server(
            description = "LOCAL ENV",
            url = "http://localhost:8080"
        )
    ]
)
class OpenApiConfig
