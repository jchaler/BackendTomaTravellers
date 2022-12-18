package com.tomatravellers.Tomatravellers.domain.useCases.user

import com.tomatravellers.Tomatravellers.NotFoundException
import com.tomatravellers.Tomatravellers.data.repositories.UsersRepository
import com.tomatravellers.Tomatravellers.domain.useCases.common.SingleUseCaseAPI
import com.tomatravellers.Tomatravellers.domain.useCases.common.UseCaseAPI
import com.tomatravellers.Tomatravellers.domain.utils.SchedulerProviderAPI
import com.tomatravellers.Tomatravellers.presentation.respone.UserMeResponseAPI
import io.reactivex.Single
import org.springframework.stereotype.Service
import java.util.*

@Service
class GetCurrentUserUseCaseAPI(
        private val schedulerProvider: SchedulerProviderAPI,
        private val userRepository: UsersRepository,
        private var appProperties: com.tomatravellers.Tomatravellers.AppPropeties
    ): SingleUseCaseAPI<UserMeResponseAPI, GetCurrentUserUseCaseAPI.Params>(schedulerProvider) {

    override fun build(params: Params?): Single<UserMeResponseAPI> {

        if (params != null) {

            val user = userRepository.getUser(UUID.fromString(params.userId))
            if (user != null) {
                val response = UserMeResponseAPI()

                response.id = user.id!!
                response.email = user.email
                response.avatar = appProperties.Imageurl + user.avatar

                return Single.just(response)
            } else {
                return Single.error(NotFoundException("User not found"))
            }
        }
        return Single.error(RuntimeException("Not params"))
    }

    data class Params(val userId: String): UseCaseAPI.Params
}