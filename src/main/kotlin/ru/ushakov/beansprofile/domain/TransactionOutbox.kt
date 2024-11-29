package ru.ushakov.beansprofile.domain

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "transaction_outbox")
data class TransactionOutbox(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val eventKey: String = "",
    val eventType: String = "",
    val payload: String = "",
    var status: TransactionStatus = TransactionStatus.PENDING,
    val createdAt: LocalDateTime = LocalDateTime.now()
)

enum class TransactionStatus {
    PENDING, COMMITTED
}
