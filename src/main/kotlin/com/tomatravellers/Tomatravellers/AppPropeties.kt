package com.tomatravellers.Tomatravellers

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class AppPropeties {

    @Value("360")
    lateinit var tokenExpirationTime: String
    @Value("60")
    lateinit var refreshTokenExpTime: String
    @Value("xm8EV6Hy5RMFK4EEACIDAwQus")
    lateinit var tokenSigningKey: String
    @Value("bbbb")
    lateinit var tokenRefreshSigningKey: String
    @Value("aaa")
    lateinit var Imageurl: String

}