package com.example.jwt

import com.example.jwt.model.ResponsePublicKey
import com.example.jwt.model.ResponseWithDecryptedMessage
import com.example.jwt.model.ResponseWithEncryptedMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.security.*
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.Cipher

@Service
class KeyService {

    @Autowired
    val privateKeyBean: PrivateKeyBean? = null

    fun createJwtRSA(): ResponsePublicKey {
        val keyGenerator: KeyPairGenerator = KeyPairGenerator.getInstance("RSA")
        keyGenerator.initialize(1024)
        val keyPair: KeyPair = keyGenerator.genKeyPair()

        val publicKey: PublicKey = keyPair.public
        val privateKey: PrivateKey = keyPair.private

        val encodedPublicKey: String = Base64.getEncoder().encodeToString(publicKey.encoded)
        val encodedPrivateKey: String = Base64.getEncoder().encodeToString(privateKey.encoded)

        println(encodedPublicKey)
        println(encodedPrivateKey)


//        val jwt: String = JWT.generateJwtTokenRSA(privateKey)
//        println(jwt)

//        println(JWT.decodeJwtRSA(jwt, publicKey))
//        privateKeyBean?.privateKey = encodedPrivateKey
        privateKeyBean?.privateKey = privateKey
        return ResponsePublicKey(encodedPublicKey)
    }

    fun encryptMessage(message: String, encryptedPublicKey: String): ResponseWithEncryptedMessage {

//        val publicKeyBytes: ByteArray = DatatypeConverter.parseBase64Binary(encryptedPublicKey)
        val publicKeyBytes: ByteArray = Base64.getDecoder().decode(encryptedPublicKey)
        val keySpec: X509EncodedKeySpec = X509EncodedKeySpec(publicKeyBytes)
        val keyFactory: KeyFactory = KeyFactory.getInstance("RSA")
        val publicKey: PublicKey = keyFactory.generatePublic(keySpec)

        val cipher: Cipher = Cipher.getInstance("RSA")
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
//        val encryptedInByteArray: ByteArray = cipher.doFinal(message)
//        DatatypeConverter.parseBase64Binary(message)
        val encryptedInByteArray: ByteArray = cipher.doFinal(message.toByteArray(StandardCharsets.UTF_8))

        // val apiKeySecretBytes: ByteArray = DatatypeConverter.parseBase64Binary(SECRET_KEY)

        return ResponseWithEncryptedMessage(encryptedInByteArray)
    }

    //https://stackoverflow.com/questions/23831200/encryption-and-decryption-with-private-key-in-java

    fun decryptMessage(messageInByteArray: ByteArray): ResponseWithDecryptedMessage {

        val cipher: Cipher = Cipher.getInstance("RSA")
        cipher.init(Cipher.DECRYPT_MODE, privateKeyBean?.privateKey)
        val message: String = String(cipher.doFinal(messageInByteArray), StandardCharsets.UTF_8)
        return ResponseWithDecryptedMessage(message)
    }
}