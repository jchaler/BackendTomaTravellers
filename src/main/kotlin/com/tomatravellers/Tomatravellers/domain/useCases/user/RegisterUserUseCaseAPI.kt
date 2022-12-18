package com.tomatravellers.Tomatravellers.domain.useCases.user


import com.tomatravellers.Tomatravellers.data.repositories.TokenRepository
import com.tomatravellers.Tomatravellers.data.repositories.UsersRepository
import com.tomatravellers.Tomatravellers.domain.useCases.common.SingleUseCaseAPI
import com.tomatravellers.Tomatravellers.domain.useCases.common.UseCaseAPI
import com.tomatravellers.Tomatravellers.domain.utils.SchedulerProviderAPI
import com.tomatravellers.Tomatravellers.presentation.DTO.APIUserDTO
import com.tomatravellers.Tomatravellers.presentation.respone.TokenResponse
import io.reactivex.Single
import org.springframework.stereotype.Service
import java.util.*

@Service
class RegisterUserUseCaseAPI(
        private val schedulerProvider: SchedulerProviderAPI,
        private val usersRepository: UsersRepository,
        private val tokenRepository: TokenRepository
) : SingleUseCaseAPI<TokenResponse, RegisterUserUseCaseAPI.Params>(schedulerProvider) {

    override fun build(params: Params?): Single<TokenResponse> {

        if (params != null) {
            val userData = params.dto

            val user = usersRepository.createUser(
                    userData.email,
                    userData.password
            )
            val tokenResponse = tokenRepository.getToken(user)
            return Single.just(tokenResponse)
        }
        return Single.error(RuntimeException("Not params"))
    }

    data class Params(val dto: APIUserDTO) : UseCaseAPI.Params
}