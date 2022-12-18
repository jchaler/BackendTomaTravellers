package com.tomatravellers.Tomatravellers.presentation.controllers


import com.tomatravellers.Tomatravellers.domain.useCases.user.*
import com.tomatravellers.Tomatravellers.JWDUtils
import com.tomatravellers.Tomatravellers.presentation.DTO.APIUserDTO
import com.tomatravellers.Tomatravellers.presentation.respone.MetaResponse
import io.reactivex.Single
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("api/v1/")
@CrossOrigin(methods = [RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT])
class UserControllerAPI(
    private val registerUserUseCaseAPI: RegisterUserUseCaseAPI,
    private val getCurrentUserUseCaseAPI: GetCurrentUserUseCaseAPI
) {

    @Autowired
    lateinit var appProperties: com.tomatravellers.Tomatravellers.AppPropeties
    @Autowired
    private val jwtUtils = JWDUtils()

    @PostMapping("/user/register")
    fun register(@RequestBody body: APIUserDTO, request: HttpServletRequest): Single<ResponseEntity<Any>> {

        val params = RegisterUserUseCaseAPI.Params(body)
        return registerUserUseCaseAPI.build(params)
            .map {
                ResponseEntity.ok(MetaResponse(it,"Response ok",200))
            }
    }


    @GetMapping("/me")
    fun getMe(request: HttpServletRequest): Single<ResponseEntity<Any>> {

        val jwt = request.getHeader("Authorization")
        val userId = jwtUtils.getUser(jwt)

        val params = GetCurrentUserUseCaseAPI.Params(userId)
        return getCurrentUserUseCaseAPI.build(params)
            .map {
                ResponseEntity.ok(MetaResponse(it,"Response ok",200))
            }
    }


}