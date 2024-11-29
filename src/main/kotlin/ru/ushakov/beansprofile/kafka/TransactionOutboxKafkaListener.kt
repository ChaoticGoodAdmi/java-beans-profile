package ru.ushakov.beansprofile.kafka

import org.springframework.kafka.annotation.KafkaListener
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import ru.ushakov.beansprofile.domain.TransactionStatus
import ru.ushakov.beansprofile.repository.TransactionOutboxRepository

@Service
class TransactionOutboxKafkaListener(
    private val outboxRepo: TransactionOutboxRepository,
    private val producer: ProfileEventProducer
) {

    @KafkaListener(topics = ["LoyaltyAccountCreated"], groupId = "beans-profile-service-group")
    fun onLoyaltyAccountCreated(event: String) {
        val transaction = outboxRepo.findById(event.toLong()).orElseThrow { Exception("Transaction not found") }
        transaction.status = TransactionStatus.COMMITTED
        outboxRepo.save(transaction)
    }

    @Scheduled(fixedRateString = "\${application.outbox.retry-period}")
    fun retryPendingMessages() {
        val pendingTransactions = outboxRepo.findAllByStatus(TransactionStatus.PENDING)
        pendingTransactions.forEach {
            producer.sendLoyaltyCabinetCreationRequiredEvent(it.id)
        }
    }
}