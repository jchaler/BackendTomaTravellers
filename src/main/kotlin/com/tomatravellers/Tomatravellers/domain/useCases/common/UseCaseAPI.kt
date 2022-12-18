package com.tomatravellers.Tomatravellers.domain.useCases.common

import io.reactivex.disposables.CompositeDisposable


abstract class UseCaseAPI<out T, in P : UseCaseAPI.Params> {
        interface Params
        object NotUseCaseParams : Params
        protected var disposables = CompositeDisposable()
        abstract fun build(params: P? = null): T
    }

