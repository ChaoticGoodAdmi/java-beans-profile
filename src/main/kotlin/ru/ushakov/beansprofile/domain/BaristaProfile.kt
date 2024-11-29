package ru.ushakov.beansprofile.domain

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "barista_profile", uniqueConstraints = [UniqueConstraint(columnNames = ["email"])])
data class BaristaProfile(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column(nullable = false, unique = true)
    val email: String = "",
    @Column(nullable = false)
    val passwordHash: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val dateOfBirth: LocalDate = LocalDate.of(1970, 1, 1)
)