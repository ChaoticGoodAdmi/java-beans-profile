package ru.ushakov.beansprofile.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.ushakov.beansprofile.controller.RegisterRequest
import ru.ushakov.beansprofile.domain.BaristaProfile
import ru.ushakov.beansprofile.domain.GuestProfile
import ru.ushakov.beansprofile.domain.TransactionOutbox
import ru.ushakov.beansprofile.kafka.ProfileEventProducer
import ru.ushakov.beansprofile.repository.*

@Service
class ProfileService(
    private val guestRepo: GuestProfileRepository,
    private val baristaRepo: BaristaProfileRepository,
    private val outboxRepo: TransactionOutboxRepository,
    private val profileEventProducer: ProfileEventProducer,
    private val objectMapper: ObjectMapper,
    private val passwordEncoder: PasswordEncoder
) {

    @Transactional
    fun registerGuest(request: RegisterRequest): Long {
        require(!guestRepo.existsByEmail(request.email)) { "Email already in use" }

        val guest = guestRepo.save(
            GuestProfile(
                email = request.email,
                passwordHash = passwordEncoder.encode(request.password),
                firstName = request.firstName,
                lastName = request.lastName,
                dateOfBirth = request.dateOfBirth
            )
        )

        val eventPayload = objectMapper.writeValueAsString(guest)
        outboxRepo.save(TransactionOutbox(eventKey = guest.id.toString(), eventType = "UserCreated", payload = eventPayload))

        profileEventProducer.sendGuestProfileCreatedEvent(guest.id)
        profileEventProducer.sendLoyaltyCabinetCreationRequiredEvent(guest.id)
        return guest.id
    }

    fun registerBarista(request: RegisterRequest): Long {
        require(!baristaRepo.existsByEmail(request.email)) { "Email already in use" }

        val barista = baristaRepo.save(
            BaristaProfile(
                email = request.email,
                passwordHash = passwordEncoder.encode(request.password),
                firstName = request.firstName,
                lastName = request.lastName,
                dateOfBirth = request.dateOfBirth
            )
        )
        return barista.id
    }

    @Transactional
    fun attachToCoffeeShop(userId: Long, coffeeShopId: Int): Boolean {
        val authentication = SecurityContextHolder.getContext().authentication
        val currentEmail = authentication.name
        val guest = guestRepo.findByEmail(currentEmail)
            ?: throw Exception("Authenticated user not found")
        require(guest.id == userId) { "You are not authorized to modify this user" }
        guest.coffeeShopId = coffeeShopId
        guestRepo.save(guest)

        return true
    }
}