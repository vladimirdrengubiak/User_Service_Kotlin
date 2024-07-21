package com.example.userapp.domain.user.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType.IDENTITY
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = IDENTITY)
    var id: Long? = null,

    @Column(nullable = false, length = 50)
    var name: String,

    @Column(nullable = false, unique = true, length = 50)
    var username: String,

    @Column(nullable = false)
    var password: String,

    @Column(nullable = false)
    var role: String
)
