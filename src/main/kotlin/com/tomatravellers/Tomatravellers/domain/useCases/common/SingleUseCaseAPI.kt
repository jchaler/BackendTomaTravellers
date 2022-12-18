package com.tomatravellers.Tomatravellers.domain.useCases.common



import com.tomatravellers.Tomatravellers.domain.utils.SchedulerProviderAPI
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import java.util.concurrent.TimeUnit


abstract class SingleUseCaseAPI<T, P : UseCaseAPI.Params>(
    private val schedulerProvider: SchedulerProviderAPI
) : UseCaseAPI<Single<T>, P>() {

    fun execute(
        params: P? = null,
        delayInMillis: Long? = null,
        onSubscribe: (Disposable) -> Unit = {},
        onError: (Throwable) -> Unit = {},
        onSuccess: (T) -> Unit = {}
    ) {

        var observable = this.build(params)
        if (delayInMillis != null) {
            observable = observable.delay(delayInMillis, TimeUnit.MILLISECONDS)
        }

        val disposable = observable
            .observeOn(schedulerProvider.background())
            .subscribeOn(schedulerProvider.background())
            .observeOn(schedulerProvider.background())
            .doOnSubscribe(Consumer(onSubscribe))
            .subscribe(Consumer(onSuccess), Consumer(onError))

        disposables.add(disposable)
    }

    @Suppress("UNCHECKED_CAST")
    fun asBaseUseCase(): SingleUseCaseAPI<T, Params> {
        return this as SingleUseCaseAPI<T, Params>
    }
}