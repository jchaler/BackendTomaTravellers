package com.tomatravellers.Tomatravellers.data.repositories

import com.tomatravellers.Tomatravellers.AppPropeties
import com.tomatravellers.Tomatravellers.presentation.DTO.RoleDTO
import com.tomatravellers.Tomatravellers.data.models.User
import com.tomatravellers.Tomatravellers.presentation.respone.TokenResponse
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import java.util.*

@Repository
interface TokenRepository {
    fun getToken(user: User): TokenResponse
}

@Service
class TokenRepositoryImpl(
    @Autowired private val  appProperties: AppPropeties
) : TokenRepository {

    override fun getToken(user: User): TokenResponse {

        val currentDate : Date = Date.from(ZonedDateTime.now().toInstant())

        // DEV
        val accessTokenExpirationDate: Date = Date.from(ZonedDateTime.now().plusMinutes(5).toInstant())
        //PRO
        //        val expirationDate: Date = Date.from(ZonedDateTime.now().plusMinutes(60).toInstant())

        val refreshTokenExpirationDate: Date = Date.from(ZonedDateTime.now().plusMonths(1).toInstant())

        val accessToken = generateAccessToken(user, currentDate, accessTokenExpirationDate)
        val refreshToken = generateRefreshToken(user, currentDate, refreshTokenExpirationDate)

        val tokenResponse = TokenResponse(
            accessToken,
            accessTokenExpirationDate.toInstant().toEpochMilli().toString(),
            refreshToken,
            refreshTokenExpirationDate.toInstant().toEpochMilli().toString()
        )
        return tokenResponse
    }

    private fun generateAccessToken(user: User,
                                    currentDate: Date,
                                    expirationDate: Date): String {
        val roles = user.roles.map {
            RoleDTO(it.id.toString(), it.name)
        }

        val claims = Jwts.claims()
        claims["roles"] = roles
        claims["userId"] = user.id.toString()

        val jwt = Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(currentDate)
            .setExpiration(expirationDate)
            .signWith(SignatureAlgorithm.HS512, this.appProperties.tokenSigningKey).compact() // make secret complex and store on secrets

        return jwt
    }

    private fun generateRefreshToken(user: User,
                                     currentDate: Date,
                                     expirationDate: Date): String {
        val roles = user.roles.map {
            RoleDTO(it.id.toString(), it.name)
        }

        val claims = Jwts.claims()
        claims["roles"] = roles
        claims["userId"] = user.id.toString()

        val randomUuid = UUID.randomUUID().toString()

        val jwt = Jwts.builder()
            .setClaims(claims)
            .setId(randomUuid)
            .setIssuedAt(currentDate)
            .setExpiration(expirationDate)
            .signWith(SignatureAlgorithm.HS512, this.appProperties.tokenRefreshSigningKey).compact() // make secret complex and store on secrets

        return jwt
    }

}