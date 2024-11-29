package ru.ushakov.beansprofile.controller

import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Past
import jakarta.validation.constraints.Size
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.ushakov.beansprofile.config.GlobalExceptionHandler
import ru.ushakov.beansprofile.service.ProfileService
import java.time.LocalDate

@RestController
@RequestMapping("/profile")
class ProfileController(private val profileService: ProfileService) {

    @PostMapping("/guest")
    fun registerGuest(@Valid @RequestBody request: RegisterRequest): ResponseEntity<Map<String, Long>> {
        val guestId = profileService.registerGuest(request)
        return ResponseEntity.ok(mapOf("userId" to guestId))
    }

    @PostMapping("/barista")
    fun registerBarista(@Valid @RequestBody request: RegisterRequest): ResponseEntity<Map<String, Long>> {
        val baristaId = profileService.registerBarista(request)
        return ResponseEntity.ok(mapOf("userId" to baristaId))
    }

    @PutMapping("/{userId}")
    fun attachToCoffeeShop(
        @PathVariable userId: Long,
        @RequestParam coffeeShopId: Int
    ): ResponseEntity<Map<String, String>> {
        return if (profileService.attachToCoffeeShop(userId, coffeeShopId)) {
            ResponseEntity.ok(mapOf("message" to "User successfully attached to a coffee shop"))
        } else {
            ResponseEntity.badRequest().body(mapOf("message" to "User cannot be attached to a coffee shop"))
        }
    }
}

data class RegisterRequest(
    @field:Email(message = "Invalid email format")
    @field:NotBlank(message = "Email is required")
    val email: String,

    @field:Size(min = 6, message = "Password must be at least 6 characters")
    val password: String,

    @field:NotBlank(message = "First name is required")
    val firstName: String,

    @field:NotBlank(message = "Last name is required")
    val lastName: String,

    @field:Past(message = "Date of birth must be in the past")
    val dateOfBirth: LocalDate
)
