package com.tomatravellers.Tomatravellers.domain.utils

import io.reactivex.Scheduler

interface SchedulerProviderAPI {

    fun main(): Scheduler

    fun computation(): Scheduler

    fun background(): Scheduler
}