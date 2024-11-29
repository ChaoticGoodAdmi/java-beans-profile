package ru.ushakov.beansprofile

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BeansProfileApplication

fun main(args: Array<String>) {
    runApplication<BeansProfileApplication>(*args)
}
