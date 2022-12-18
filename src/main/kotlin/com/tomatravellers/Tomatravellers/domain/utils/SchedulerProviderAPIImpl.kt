package com.tomatravellers.Tomatravellers.domain.utils

import com.tomatravellers.Tomatravellers.domain.utils.SchedulerProviderAPI
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import org.springframework.stereotype.Service

@Service
class SchedulerProviderAPIImpl: SchedulerProviderAPI {
    override fun computation(): Scheduler = Schedulers.computation()

    override fun main(): Scheduler = Schedulers.single()

    override fun background(): Scheduler = Schedulers.io()
}