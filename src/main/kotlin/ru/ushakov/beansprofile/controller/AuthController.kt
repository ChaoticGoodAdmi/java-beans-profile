package ru.ushakov.beansprofile.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.ushakov.beansprofile.auth.CustomUserDetailsService
import ru.ushakov.beansprofile.auth.JwtUtil
import ru.ushakov.beansprofile.service.ProfileService

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val jwtUtil: JwtUtil,
    private val userDetailsService: CustomUserDetailsService,
    private val profileService: ProfileService
) {

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<Map<String, String>> {
        val authenticationToken = UsernamePasswordAuthenticationToken(request.email, request.password)
        authenticationManager.authenticate(authenticationToken)

        val userDetails: UserDetails = userDetailsService.loadUserByUsername(request.email)
        val userIdentity = profileService.getUserIdentityByEmail(request.email)
        val token = jwtUtil.generateToken(userIdentity.userId, userDetails.username, userIdentity.role, userIdentity.coffeeShopId)

        return ResponseEntity.ok(mapOf("jwtToken" to token))
    }
}

data class LoginRequest(
    val email: String,
    val password: String
)