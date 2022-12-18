package com.tomatravellers.Tomatravellers.data.models
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.hibernate.annotations.Type
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "role", schema = "toma")
@JsonIgnoreProperties(value = ["hibernateLazyInitializer", "handler"])
class Role(

        @Id @GeneratedValue(strategy = GenerationType.AUTO) @Column(columnDefinition = "CHAR(36)") @Type(type = "uuid-char")
        var id: UUID? = null,
        var name: String,

        @ManyToMany(mappedBy = "roles", cascade = [CascadeType.PERSIST])
        @JsonIgnore
        var users: MutableList<User> = mutableListOf(),

        @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS")
        var createdAt: LocalDateTime = LocalDateTime.now(),
        @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS")
        var updatedAt: LocalDateTime = LocalDateTime.now()
)