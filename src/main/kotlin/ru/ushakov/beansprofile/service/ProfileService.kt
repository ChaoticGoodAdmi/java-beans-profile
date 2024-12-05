package ru.ushakov.beansprofile.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.ushakov.beansprofile.controller.RegisterRequest
import ru.ushakov.beansprofile.domain.Profile
import ru.ushakov.beansprofile.domain.Role
import ru.ushakov.beansprofile.domain.UserIdentity
import ru.ushakov.beansprofile.kafka.ProfileEventProducer
import ru.ushakov.beansprofile.repository.*

@Service
class ProfileService(
    private val profileRepository: ProfileRepository,
    private val profileEventProducer: ProfileEventProducer,
    private val passwordEncoder: PasswordEncoder,
    @Value("\${barista.registration.key}")
    private val baristaRegistrationKey: String
) {

    @Transactional
    fun register(request: RegisterRequest): Long {
        check(!(request.role == Role.BARISTA && baristaRegistrationKey != request.baristaSecretKey))
        { "User does not have permission to register as barista" }
        require(!profileRepository.existsByEmail(request.email))
        { "Email already in use" }

        val profile = profileRepository.save(
            Profile(
                email = request.email,
                passwordHash = passwordEncoder.encode(request.password),
                firstName = request.firstName,
                lastName = request.lastName,
                dateOfBirth = request.dateOfBirth,
                role = request.role,
                coffeeShopId = request.coffeeShopId
            )
        )

        if (profile.role == Role.GUEST) {
            profileEventProducer.sendProfileCreatedEvent(profile)
        }
        return profile.id
    }

    fun getUserIdentityByEmail(email: String): UserIdentity {
        val profile = profileRepository.findByEmail(email)
        return when {
            profile != null -> UserIdentity(profile.id, profile.role, profile.coffeeShopId)
            else -> throw IllegalArgumentException("User not found")
        }
    }

    @Transactional
    fun attachToCoffeeShop(userId: Long, coffeeShopId: String): Boolean {
        val authentication = SecurityContextHolder.getContext().authentication
        val cu
        rrentEmail = authentication.name
        val profile = profileRepository.findByEmail(currentEmail)
            ?: throw Exception("Authenticated user not found")
        require(profile.id == userId) { "You are not authorized to modify this user" }
        profile.coffeeShopId = coffeeShopId
        profileRepository.save(profile)

        return true
    }
}