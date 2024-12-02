package ru.ushakov.beansprofile.auth

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import ru.ushakov.beansprofile.repository.ProfileRepository

class CustomUserDetails(
    private val email: String,
    private val password: String
) : UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = mutableListOf()

    override fun getPassword(): String = password

    override fun getUsername(): String = email

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}

@Service
class CustomUserDetailsService(
    private val profileRepository: ProfileRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val profile = profileRepository.findByEmail(username)
        return when {
            profile != null -> CustomUserDetails(profile.email, profile.passwordHash)
            else -> throw IllegalArgumentException("User not found")
        }
    }
}