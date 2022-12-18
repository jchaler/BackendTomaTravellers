package com.tomatravellers.Tomatravellers.presentation.DTO

class RoleDTO {
    val name: String
    val id: String

    constructor(id: String,
    name: String) {
        this.id = id
        this.name = name
    }
}