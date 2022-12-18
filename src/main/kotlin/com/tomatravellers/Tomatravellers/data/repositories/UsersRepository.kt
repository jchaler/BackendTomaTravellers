package com.tomatravellers.Tomatravellers.data.repositories

import com.tomatravellers.Tomatravellers.data.dataSources.RoleDataSource
import com.tomatravellers.Tomatravellers.data.dataSources.UserDataSource
import com.tomatravellers.Tomatravellers.data.models.Role
import com.tomatravellers.Tomatravellers.data.models.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

interface UsersRepository {
    fun createUser(
                   email: String,
                   password: String): User

    fun createAdmin(): User

    fun getUser(email: String): User?
    fun getUser(id: UUID): User?
    fun getUsers(role: Role): MutableList<User>

    fun getUsers(ids: MutableList<String>): MutableList<User>

    fun save(user: User): User?

    fun getAll(): MutableList<User>

    fun delete(user: User)
}

@Service
class UsersRepositoryImpl(
        @Autowired private val userDataSource: UserDataSource,
        @Autowired private val roleDataSource: RoleDataSource
) : UsersRepository {

    override fun createUser(email: String,
                            password: String): User {
        val user = User(email = email,
            avatar = "users/image.png",
            language = "")
        user.password = password

        this.addRoleUser(user)
        return userDataSource.save(user)
    }

    override fun createAdmin(): User {
        val user = User(email = "tech@tomaapp.com",
            avatar = "user/image.jpeg",
            language = "es")
        user.password = "Toma"
        val savedUser = userDataSource.save(user)
        addRoleToUser("tech@tomaapp.com","ROLE_SUPERADMIN")
        return savedUser
    }

    private fun addRoleToUser(email: String,
                              roleName: String) {
        val user = userDataSource.findByEmail(email)
        val role = roleDataSource.findByName(roleName)
        if (user != null) {
            if (role != null) {
                //user.roles.add(role)
                save(user)
            }
        }
    }

    private fun addRoleUser(user: User) {
        roleDataSource.findByName("ROLE_USER")?.let { user.roles.add(it) }
    }

    override fun getUser(email: String): User? {
        return userDataSource.findByEmail(email)
    }

    override fun getUser(id: UUID): User? {
        return userDataSource.getById(id)
    }

    override fun getUsers(role: Role): MutableList<User> {
        return userDataSource.findByRoles(role)
    }

    override fun getUsers(ids: MutableList<String>): MutableList<User> {
        val uuids = ids.map { UUID.fromString(it) }
        return userDataSource.findAllById(uuids)
    }

    override fun save(user: User): User? {
        return userDataSource.save(user)
    }

    override fun getAll(): MutableList<User> {
        return userDataSource.findAll()
    }

    override fun delete(user: User) {
        return userDataSource.delete(user)
    }
}