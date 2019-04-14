package com.example.jwt.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class CustomController {

    @GetMapping("/filter")
    fun methodFilter(){
        println("controller class")
    }
}