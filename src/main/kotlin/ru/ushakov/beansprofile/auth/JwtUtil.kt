package ru.ushakov.beansprofile.auth

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import ru.ushakov.beansprofile.domain.Role
import java.util.*
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

@Component
class JwtUtil(
    @Value("\${jwt.secret-key}")
    private val secretKeyParam: String
) {

    private val secretKey: SecretKey by lazy {
        SecretKeySpec(secretKeyParam.toByteArray(), SignatureAlgorithm.HS256.jcaName)
    }

    fun generateToken(userId: Long, email: String, role: Role, coffeeShopId: String?): String {
        return Jwts.builder()
            .setSubject(email)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 60))
            .claim("userId", userId)
            .claim("role", role.name)
            .claim("coffeeShopId", coffeeShopId)
            .signWith(secretKey)
            .compact()
    }

    fun validateToken(token: String): String? {
        return try {
            Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .body
                .subject
        } catch (e: Exception) {
            null
        }
    }
}