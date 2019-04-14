package com.example.jwt

import org.springframework.stereotype.Component
import java.security.PrivateKey

@Component
class PrivateKeyBean {

    var privateKey: PrivateKey? = null
}