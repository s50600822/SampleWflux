package com.example.webfluxk8s

import io.micrometer.core.instrument.Metrics
import org.springframework.boot.actuate.info.GitInfoContributor
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WebfluxK8sApplication()

fun main(args: Array<String>) {

    Metrics.globalRegistry.config()
    runApplication<WebfluxK8sApplication>(*args)
}


