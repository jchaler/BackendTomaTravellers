package com.tomatravellers.Tomatravellers.presentation.respone

class TokenResponse {
    var access_token = ""
    var access_token_ttl = ""
    var refresh_token = ""
    var refresh_token_ttl = ""

    constructor(accessToken: String,
                accessTokenTtl: String,
                refreshToken: String,
                refreshTokenTtl: String) {
        this.access_token = accessToken
        this.access_token_ttl = accessTokenTtl
        this.refresh_token = refreshToken
        this.refresh_token_ttl = refreshTokenTtl
    }
}

