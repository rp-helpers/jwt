package com.example.jwt.filter

import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.util.*
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

//@Component
class CustomFilter : Filter {

    override fun doFilter(req: ServletRequest, resp: ServletResponse, chain: FilterChain) {

        println("custom filter start")
        val encodedClaims = getHeader("Authorization", req, resp)
        println("encoded claims: $encodedClaims")
        val decodedClaims = decodeHeader(encodedClaims)
        println("decoded claims: $decodedClaims")

        val login: String = decodedClaims.split(":").component1()
        val password: String = decodedClaims.split(":").component2()

        println("login: $login")
        println("password: $password")

        println(getHeader("MyHeaderCustom", req, resp))

        chain.doFilter(req, resp)
        println("custom filter end")
        println()
    }

//    private fun readHeader(@RequestHeader("Authorization") claim: String): String {
//        val data: ByteArray = Base64.getDecoder().decode(claim)
//        val dataString = String(data, StandardCharsets.UTF_8)
//        println(dataString)
//        return dataString
//    }

    private fun getHeader(headerName: String, req: ServletRequest, resp: ServletResponse): String {
        val requestHttp = req as HttpServletRequest
        val responseHttp = resp as HttpServletResponse
        return requestHttp.getHeader(headerName)
    }

    private fun decodeHeader(encodedHeaderValue: String): String {
        val data: ByteArray = Base64.getDecoder().decode(encodedHeaderValue)
        val dataString = String(data, StandardCharsets.UTF_8)
        return dataString
    }
}