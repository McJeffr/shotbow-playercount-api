package com.mcjeffr.shotbowplayercountapi.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/")
class HealthController {

    @GetMapping
    fun getHealth() {

    }

}