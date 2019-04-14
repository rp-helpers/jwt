package com.example.jwt

import io.jsonwebtoken.*
import java.security.*
import java.util.*
import javax.crypto.spec.SecretKeySpec
import javax.xml.bind.DatatypeConverter


fun main(args: Array<String>) {
    println("hello")
    val jwtString = JWT.createJwt_HMAC_SHA256("1234", "Me", "text", 100000)
    println(jwtString)

    print(JWT.decodeJwt_HMAC_SHA256(jwtString))

    println()
    println()
    println()

    JWT.createJwtRSA()


}

class JWT {

    companion object {

        val SECRET_KEY = "klucz"

        val firstPK = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCj2LkiP63XqF9AmGKLm0UV7PZy+cIS6DGsDZVqU2hk7bTk3AyWcz3UCjrBJUEXG/sX0G6ptcvjZ35zj/zvBVI9EOhpVzOaOyYpm9okRPHPXdOtCehqNZnnz0cH4uQEl8WqjVW2Ua/52KOlSbJ7cMPNclud3v7TWQr9z7EE3WyL7QIDAQAB"
        val secondPK = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCWstkwXgBdGPGUOcuq5r5M/k5tmaevdwcKzLIt+qB4uWC0F2qt9d2U85SJXG8VL+SMwA3vPSNv0rfTtAzmHskrU1Cc3NCUsiYMezwI3xSAyOgHA41cx53G16hbE22BntlATVFiJnLIsoFEiwHlt9zIH+7IXTZgi20Vt5iHp4v4gQIDAQAB"
        val thirdPK = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCvzZX4PZc32gPzQGZfXdDIr2/Uis/SEfzKqWwZukjjwJSY+yY0Xlo83j1nkIV84qyYkbp2eT+q+NdEbR8994uxGmgNmiRts6rutLz4bhh7OXCDhgLtbhgOaE7WBp+qEq4laIpYxa+Y7Cszd4TTn7iIa8yqlT4Z+VMx4dZ7+feBwwIDAQAB"


        fun createJwtSha(){
            val keyGenerator: KeyPairGenerator = KeyPairGenerator.getInstance("SHA")
            keyGenerator.initialize(1024)

        }

        fun createJwtRSA() {
            val keyGenerator: KeyPairGenerator = KeyPairGenerator.getInstance("RSA")
            keyGenerator.initialize(2048)
            val keyPair: KeyPair = keyGenerator.genKeyPair()

            val publicKey: PublicKey = keyPair.public
            val privateKey: PrivateKey = keyPair.private

            val encodedPublicKey: String = Base64.getEncoder().encodeToString(publicKey.encoded)
            val encodedPrivateKey: String = Base64.getEncoder().encodeToString(privateKey.encoded)

            println(encodedPublicKey)
            println(encodedPrivateKey)
            println()
//            println(publicKey)

            val jwt: String = generateJwtTokenRSA(privateKey)
            println(jwt)

            println(decodeJwtRSA(jwt, publicKey))

        }

        @SuppressWarnings("deprecation")
        fun generateJwtTokenRSA(privateKey: PrivateKey) = Jwts.builder().setSubject("something")
                .setExpiration(Date(2019,4,4))
                .setIssuer("info@gmail.com")
                .claim("groups", arrayOf("ADMIN", "USER"))
                .signWith(SignatureAlgorithm.RS256, privateKey)
                .compact()

        fun decodeJwtRSA(jwt: String, publicKey: PublicKey): Claims = Jwts.parser()
                .setSigningKey(publicKey)
                .parseClaimsJws(jwt)
                .body


        fun createJwt_HMAC_SHA256(id: String, issuer: String, subject: String, ttlMillis: Long): String {

            val signatureAlgorithms: SignatureAlgorithm = SignatureAlgorithm.HS256
            val nowMillis = System.currentTimeMillis()
            val now: Date = Date(nowMillis)

            val apiKeySecretBytes: ByteArray = DatatypeConverter.parseBase64Binary(SECRET_KEY)

            val signingKey: Key = SecretKeySpec(apiKeySecretBytes, signatureAlgorithms.jcaName)

            val builder = Jwts.builder().setId(id)
                    .setIssuedAt(now)
                    .setSubject(subject)
                    .setIssuer(issuer)
//                    .signWith(signatureAlgorithms, signingKey)
                    .signWith(SignatureAlgorithm.HS256, signingKey)

            if (ttlMillis > 0) {
                val expMillis = nowMillis + ttlMillis
                val exp = Date(expMillis)
                builder.setExpiration(exp)
            }

            return builder.compact()
        }

        fun decodeJwt_HMAC_SHA256(jwt: String): Claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .parseClaimsJws(jwt)
                .body


    }

    //https://jwt.io/introduction/

}