package ru.ushakov.beansprofile.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class ProfileEventProducer(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    val objectMapper: ObjectMapper
) {

    fun sendGuestProfileCreatedEvent(profileId: Long) {
        val event = objectMapper.writeValueAsString(GuestProfileCreatedEvent(profileId))
        kafkaTemplate.send("GuestProfileCreated", event)
        println("OrderCreatedEvent sent to Kafka: $event")
    }

    fun sendLoyaltyCabinetCreationRequiredEvent(profileId: Long) {
        val event = objectMapper.writeValueAsString(LoyaltyCabinetCreationRequiredEvent(profileId))
        kafkaTemplate.send("LoyaltyCabinetCreationRequiredEvent", event)
        println("LoyaltyCabinetCreationRequiredEvent sent to Kafka: $event")
    }
}

data class GuestProfileCreatedEvent(
    val profileId: Long
)

data class LoyaltyCabinetCreationRequiredEvent(
    val profileId: Long
)