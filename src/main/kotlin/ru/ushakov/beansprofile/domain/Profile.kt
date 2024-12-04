package ru.ushakov.beansprofile.domain

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "profiles", uniqueConstraints = [UniqueConstraint(columnNames = ["email"])])
data class Profile(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column(nullable = false, unique = true)
    val email: String = "",
    @Column(nullable = false)
    val passwordHash: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val dateOfBirth: LocalDate = LocalDate.of(1970, 1, 1),
    var coffeeShopId: String? = null,
    val role: Role = Role.GUEST
)

enum class Role {
    GUEST, BARISTA
}

data class UserIdentity(
    val userId: Long,
    val role: Role,
    val coffeeShopId: String?
)