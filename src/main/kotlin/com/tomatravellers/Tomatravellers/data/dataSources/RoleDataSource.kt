package com.tomatravellers.Tomatravellers.data.dataSources


import com.tomatravellers.Tomatravellers.data.models.Role
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface RoleDataSource: JpaRepository<Role, UUID> {
    fun findByName(name: String): Role?

}