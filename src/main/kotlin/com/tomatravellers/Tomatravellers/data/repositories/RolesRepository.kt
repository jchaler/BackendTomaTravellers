package com.tomatravellers.Tomatravellers.data.repositories


import com.tomatravellers.Tomatravellers.data.dataSources.RoleDataSource
import com.tomatravellers.Tomatravellers.data.models.Role
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.UUID

interface RolesRepository {
    fun createRoles()
    fun getRole(name: String): Role?
}

@Service
class RolesRepositoryImpl(
    @Autowired private val roleDataSource: RoleDataSource
) : RolesRepository {

    override fun createRoles() {
        roleDataSource.save(Role(name = "ROLE_USER"))
        roleDataSource.save(Role(name = "ROLE_ADMIN"))
        roleDataSource.save(Role(name = "ROLE_SUPERADMIN"))
    }

    override fun getRole(name: String): Role? {
        return roleDataSource.findByName(name)
    }
}