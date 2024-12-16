package ru.ushakov.beansprofile

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
@EnableCaching
class BeansProfileApplication

fun main(args: Array<String>) {
    runApplication<BeansProfileApplication>(*args)
}
