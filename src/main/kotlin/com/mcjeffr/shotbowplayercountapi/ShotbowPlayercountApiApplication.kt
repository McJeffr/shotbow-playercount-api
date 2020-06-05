package com.mcjeffr.shotbowplayercountapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling
import java.util.*
import javax.annotation.PostConstruct

@SpringBootApplication
@EnableScheduling
class ShotbowPlayercountApiApplication

fun main(args: Array<String>) {
	runApplication<ShotbowPlayercountApiApplication>(*args)
}
