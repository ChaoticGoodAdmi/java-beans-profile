package ru.ushakov.beansprofile.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.ushakov.beansprofile.domain.*

interface GuestProfileRepository : JpaRepository<GuestProfile, Long> {
    fun existsByEmail(email: String): Boolean
    fun findByEmail(email: String): GuestProfile?
}

interface BaristaProfileRepository : JpaRepository<BaristaProfile, Long> {
    fun existsByEmail(email: String): Boolean
    fun findByEmail(email: String): BaristaProfile?
}

interface TransactionOutboxRepository : JpaRepository<TransactionOutbox, Long> {
    fun findAllByStatus(status: TransactionStatus): List<TransactionOutbox>
}