package com.tomatravellers.Tomatravellers.presentation.respone

class MetaResponse<T>(meta:T?,message: String,code: Int) {

    var meta = meta
    var message: String = message
    var status_code: Int= code


}