package com.example.webfluxk8s

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux.interval
import reactor.core.scheduler.Schedulers
import java.time.Duration.ofMillis
import javax.annotation.PostConstruct

@Service
class Pinger(val webClient: WebClient) {
    private val log = LoggerFactory.getLogger(Pinger::class.java)

    @PostConstruct
    fun doShit() {
        ping(webClient)
    }

    fun ping(client: WebClient){
        interval(ofMillis(20000))
            .take(1)
            .parallel()
            .runOn(Schedulers.boundedElastic())// REQUIRED FOR PARALLEL CALL
            .flatMap {
                client.get()
                .uri("/oauth2/default/.well-known/oauth-authorization-server")
                .retrieve()
                .bodyToFlux(OktaResponse::class.java)
                .onErrorContinue {t,v -> log.error("WTF", t) }
            }
            .subscribe { log.debug("{}", "<<<<< check thread name") }
    }
}
