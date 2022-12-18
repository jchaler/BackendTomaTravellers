package com.tomatravellers.Tomatravellers

import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class JWDUtils() {

    @Autowired
    lateinit var appProperties: com.tomatravellers.Tomatravellers.AppPropeties
    lateinit var jwt: String

    fun getUser(jwt: String): String{
        try {
            val body = Jwts.parser()
                .setSigningKey(this.appProperties.tokenSigningKey)
                .parseClaimsJws(jwt.replace("Bearer", ""))
                .body
            val userId = body.get("userId") as String
            return userId
        } catch (e: Exception){
            throw NotFoundException("wrong identification token")
        }
    }

    fun getUserFromrefreshToken(jwt: String): String{
        try {
            val body = Jwts.parser()
                .setSigningKey(this.appProperties.tokenRefreshSigningKey)
                .parseClaimsJws(jwt.replace("Bearer", ""))
                .body
            val userId = body.get("userId") as String
            return userId
        } catch (e: Exception){
            throw NotFoundException("wrong identification token")
        }
    }
}

