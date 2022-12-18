package com.tomatravellers.Tomatravellers.domain.useCases.auth

import com.tomatravellers.Tomatravellers.NotFoundException
import com.tomatravellers.Tomatravellers.WrongPasswordException
import com.tomatravellers.Tomatravellers.domain.useCases.common.SingleUseCaseAPI
import com.tomatravellers.Tomatravellers.domain.useCases.common.UseCaseAPI
import com.tomatravellers.Tomatravellers.domain.utils.SchedulerProviderAPI
import com.tomatravellers.Tomatravellers.presentation.respone.TokenResponse
import com.tomatravellers.Tomatravellers.data.models.User
import com.tomatravellers.Tomatravellers.data.repositories.TokenRepository
import com.tomatravellers.Tomatravellers.data.repositories.UsersRepositoryImpl
import io.reactivex.Single
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.lang.Exception

@Service
class AuthUserByPasswordUseCaseAPI(
        private val schedulerProviderAPI: SchedulerProviderAPI,
        private val userRepository: UsersRepositoryImpl,
        private val tokenRepository: TokenRepository
) : SingleUseCaseAPI<TokenResponse, AuthUserByPasswordUseCaseAPI.Params>(schedulerProviderAPI) {

    override fun build(params: Params?): Single<TokenResponse> {
        if (params != null) {
            val user: User?
            try {
                user = userRepository.getUser(params.email)!!
            } catch (e: Exception) {
                return Single.error(NotFoundException("user not found"))
            }

            if (!BCryptPasswordEncoder().matches(params.password, user.password)) {
                return Single.error(WrongPasswordException("error auth"))
            }

            val tokenResponse = tokenRepository.getToken(user)
            return Single.just(tokenResponse)
        } else {
            return Single.error(RuntimeException(""))
        }
    }

    data class Params(val email: String,
                      val password: String) : UseCaseAPI.Params
}
