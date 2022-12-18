package com.tomatravellers.Tomatravellers.data.models

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.hibernate.annotations.Type
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.time.LocalDateTime
import java.util.UUID
import javax.persistence.*


@Entity
@Table(name = "user", schema = "toma")
@JsonIgnoreProperties(value = ["hibernateLazyInitializer", "handler"])
class User (

        @Id @GeneratedValue(strategy = GenerationType.AUTO) @Column(columnDefinition = "CHAR(36)") @Type(type = "uuid-char")
        var id: UUID? = null,

        @Column(unique = true)
        var email: String,
        var avatar: String,
        var language: String,

        @ManyToMany(fetch = FetchType.LAZY)
        @JoinTable(name = "user_role", schema= "toma")
        var roles : MutableList<Role> = mutableListOf(),

        @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS")
        var createdAt: LocalDateTime = LocalDateTime.now(),
        @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS")
        var updatedAt: LocalDateTime = LocalDateTime.now()
) {

    @Column
    var password = ""
        @JsonIgnore
        get() = field
        set(value){
            val passwordEncoder = BCryptPasswordEncoder()
            field = passwordEncoder.encode(value)
        }
}