package com.example.webfluxk8s

import io.micrometer.core.instrument.Metrics.globalRegistry
import io.micrometer.core.instrument.config.MeterFilter.*
import org.springframework.boot.actuate.info.GitInfoContributor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import java.util.function.Function

@Configuration
class Configs {
    @Bean
    fun webClient(
        webClientBuilder: WebClient.Builder,
    ): WebClient {
        val httpClient: HttpClient = HttpClient.create()
            .baseUrl("https://dev-2155582.okta.com")
            .compress(true)
            //https://projectreactor.io/docs/netty/release/reference/index.html#_metrics_6
            .metrics(true, Function.identity())
            //.metrics(true, Supplier { reactor.netty.channel.MicrometerChannelMetricsRecorder(TCP_CLIENT_PREFIX, "tcp") })
            //httpClient.warmup().block();
        globalRegistry
            .config()
            //.meterFilter(maximumAllowableTags(HTTP_CLIENT_PREFIX, URI, 1, ignoreTags("id")))
            //.meterFilter(maximumAllowableTags(TCP_CLIENT_PREFIX, URI, 1, ignoreTags("id")))
            .meterFilter(denyNameStartsWith("jvm"))
            .meterFilter(denyNameStartsWith("logbackEvents"))
            .meterFilter(denyNameStartsWith("system"))
       return webClientBuilder.clientConnector(ReactorClientHttpConnector(httpClient)).build()
    }
}
