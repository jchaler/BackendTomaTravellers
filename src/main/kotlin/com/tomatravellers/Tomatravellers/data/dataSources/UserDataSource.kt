package com.tomatravellers.Tomatravellers.data.dataSources


import com.tomatravellers.Tomatravellers.data.models.Role
import com.tomatravellers.Tomatravellers.data.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface UserDataSource: JpaRepository<User, UUID> {
    fun findByEmail(email:String): User?
    fun findByRoles(role: Role): MutableList<User>

    @Query("SELECT u FROM User u where u.id in :ids")
    fun findAllById(@Param("ids") userIds: List<String>): MutableList<User>
}