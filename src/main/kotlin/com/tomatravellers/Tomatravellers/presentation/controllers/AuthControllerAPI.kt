package com.tomatravellers.Tomatravellers.presentation.controllers

import com.tomatravellers.Tomatravellers.JWDUtils
import com.tomatravellers.Tomatravellers.presentation.DTO.APILoginDTO
import com.tomatravellers.Tomatravellers.presentation.respone.MetaResponse
import com.tomatravellers.Tomatravellers.domain.useCases.auth.AuthUserByPasswordUseCaseAPI
import io.reactivex.Single
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(methods = [RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT])
class AuthControllerAPI (
        private val authUserByPasswordUseCaseAPI: AuthUserByPasswordUseCaseAPI,
        //private val forgotPasswordUseCaseAPI: ForgotPasswordUseCaseAPI,
        //private val recoveryPasswordUseCaseAPI: RecoveryPasswordUseCaseAPI,
        //private val authUserByRefreshTokenUseCaseAPI: AuthUserByRefreshTokenUseCaseAPI,
        //private val contactUseCaseAPI: ContactUseCaseAPI
){
    @Autowired
    private val jwtUtils = JWDUtils()

    @PostMapping("oauth/token")
    fun login(@RequestBody body: APILoginDTO, response: HttpServletResponse): Single<ResponseEntity<Any>>{

        val params = AuthUserByPasswordUseCaseAPI.Params(body.email, body.password)
        return authUserByPasswordUseCaseAPI.build(params)
            .map {
                ResponseEntity.ok(MetaResponse(it, "Welcome to our platform", 200))
            }
    }




}