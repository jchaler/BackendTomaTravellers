package com.tomatravellers.Tomatravellers.domain.useCases.common

import com.tomatravellers.Tomatravellers.domain.utils.SchedulerProviderAPI
import io.reactivex.Completable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import java.util.concurrent.TimeUnit

abstract class CompletableUseCaseAPI<in P : UseCaseAPI.Params>(
    private val schedulerProvider: SchedulerProviderAPI
) : UseCaseAPI<Completable, P>() {

    fun execute(
        params: P? = null,
        delayInMillis: Long? = null,
        onSubscribe: (Disposable) -> Unit = {},
        onError: (Throwable) -> Unit = {},
        onComplete: () -> Unit = {}
    ) {

        var observable = this.build(params)
        if(delayInMillis != null ){
            observable = observable.delay(delayInMillis, TimeUnit.MILLISECONDS)
        }

        val disposable = observable
            .subscribeOn(schedulerProvider.background())
            .observeOn(schedulerProvider.main())
            .doOnSubscribe(Consumer(onSubscribe))
            .subscribe(Action(onComplete), Consumer(onError))

        disposables.add(disposable)
    }
}