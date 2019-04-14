package com.example.jwt.controller

import com.example.jwt.KeyService
import com.example.jwt.model.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


//import org.springframework.web.bind.annotation.RestController

@RestController
class CryptoController(@Autowired val keyServide: KeyService) {


    @GetMapping("/init")
    fun getPublicKey(): ResponsePublicKey = keyServide.createJwtRSA()

    @PostMapping("/encrypt")
    fun encrypt(@RequestBody requestModel: RequestModel): ResponseWithEncryptedMessage = keyServide.encryptMessage(requestModel.message, requestModel.enctryptedPublicKey)

    @GetMapping("/getRequestJsonForEncrypt")
    fun getRequestJson() = RequestModel("message", "encryptedPublicKey")

    @PostMapping("/decrypt")
    fun decrypt(@RequestBody requestForDecryptModel: RequestForDecryptModel): ResponseWithDecryptedMessage = keyServide.decryptMessage(requestForDecryptModel.messageInByteArray)

    @GetMapping("/getRequestJsonForDecrypt")
    fun getRequestJsonForDecrypt() = RequestForDecryptModel(byteArrayOf())

}