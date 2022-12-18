package com.tomatravellers.Tomatravellers

import com.tomatravellers.Tomatravellers.presentation.respone.MetaResponse
import org.springframework.http.HttpStatus

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException

import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import javax.servlet.http.HttpServletRequest


class NotFoundException: Exception {

    constructor() : super()
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
    constructor(cause: Throwable) : super(cause)

}

class MalformedException: Exception {

    constructor() : super()
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
    constructor(cause: Throwable) : super(cause)

}

class NoValidTokenException: Exception {

    constructor() : super()
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
    constructor(cause: Throwable) : super(cause)

}

class WrongPasswordException: Exception {

    constructor() : super()
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
    constructor(cause: Throwable) : super(cause)

}

class UserEventLimitReached: Exception {

    constructor() : super()
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
    constructor(cause: Throwable) : super(cause)

}

@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException::class)
    fun handlyMyCustomException(e: NotFoundException): ResponseEntity<MetaResponse<String>> {
        return ResponseEntity.status(400).body(MetaResponse(e.message,"Error not found in database",400))
    }
    @ExceptionHandler(MalformedException::class)
    fun handleMalformedException(e: MalformedException): ResponseEntity<MetaResponse<String>>{
        return ResponseEntity.status(400).body(MetaResponse(e.message,"Error with uuid",400))
    }
    @ExceptionHandler(NoValidTokenException::class)
    fun handleNoValidTokenException(e: NoValidTokenException): ResponseEntity<MetaResponse<String>>{
        return ResponseEntity.status(401).body(MetaResponse(e.message,"Error with Json autentification, not valid token",401))
    }
    @ExceptionHandler(WrongPasswordException::class)
    fun handleWrongPasswordException(e: WrongPasswordException): ResponseEntity<MetaResponse<String>>{
        return ResponseEntity.status(401).body(MetaResponse(e.message,"Wrong password",401))
    }
    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    fun handleMethodArgumentNotValidException(ex: MethodArgumentNotValidException, request: HttpServletRequest?): ResponseEntity<Any> {
        return ResponseEntity.status(400).body(MetaResponse(ex.message,"Not valid type or status syntax",400))
    }
    @ExceptionHandler(UserEventLimitReached::class)
    fun handleUserEventLimitReachedException(e: UserEventLimitReached): ResponseEntity<MetaResponse<String>>{
        return ResponseEntity.status(410).body(MetaResponse(e.message,"User Event Limit Reached",410))
    }
}