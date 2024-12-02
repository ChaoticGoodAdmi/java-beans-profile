package ru.ushakov.beansprofile.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import ru.ushakov.beansprofile.domain.Profile

@Service
class ProfileEventProducer(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    val objectMapper: ObjectMapper
) {

    fun sendProfileCreatedEvent(profile: Profile) {
        val event = objectMapper.writeValueAsString(ProfileCreatedEvent(profile))
        kafkaTemplate.send("ProfileCreated", event)
        println("ProfileCreatedEvent sent to Kafka: $event")
    }
}

data class ProfileCreatedEvent(
    val profile: Profile
)