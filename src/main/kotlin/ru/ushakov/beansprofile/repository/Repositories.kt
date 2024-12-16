package ru.ushakov.beansprofile.repository

import org.springframework.cache.annotation.Cacheable
import org.springframework.data.jpa.repository.JpaRepository
import ru.ushakov.beansprofile.domain.*

interface ProfileRepository : JpaRepository<Profile, Long> {
    fun existsByEmail(email: String): Boolean

    @Cacheable("profiles")
    fun findByEmail(email: String): Profile?
}