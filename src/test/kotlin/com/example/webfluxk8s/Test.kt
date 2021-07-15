package com.example.webfluxk8s

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import reactor.core.publisher.Flux
import reactor.core.scheduler.Schedulers
import reactor.util.retry.RetrySpec
import java.lang.IllegalArgumentException
import java.time.Duration
import java.util.function.Predicate


class Test {
    val log = LoggerFactory.getLogger(Test::class.java)
    @Test
    fun check() {
        Flux.range(1, 20)
            .map { x: Int ->
                if (x == 1) {
                    throw RuntimeException()
                }
                x * 2
            }
            .onErrorContinue { e: Throwable?, i: Any -> println("Error For Item +$i") } // 4 6 8 10
            //.onErrorResume { e: Throwable? -> Flux.empty() } // Nothing
            //.onErrorMap { t-> t } //Nothing
            .subscribe { x: Int? -> println(x) }
        assertTrue(true)
    }

    @Test
    fun checkp() {
        Flux.range(1, 20)
            //.parallel()
            .flatMap { double(it) }
            .subscribeOn(Schedulers.parallel())
            .subscribe { x: Int? -> println(Thread.currentThread().name) }
        assertTrue(true)
    }


    @Test
    fun checkp2() {
        Flux.range(1, 10)
            .parallel()
            .runOn(Schedulers.parallel())
            .map { it * 2 }
            //.doOnEach { println("${Thread.currentThread().name} $it") }
            //.doOnNext { println("${Thread.currentThread().name} $it") }
            .subscribe { println("${Thread.currentThread().name} $it") }
        assertTrue(true)
    }


    private fun double(x: Int): Flux<Int> {
        //println("${Thread.currentThread().name} double")
        return Flux.range(1, 100).map { x + it }.delayElements(Duration.ofMillis(10000))
        //return Flux.range(1,10).map { x + it }
    }


    @Test
    fun checkRetry() {
      Flux.range(1, 10)
            .map { x: Int ->
                if (x == 1) {
                    throw RuntimeException()
                }
                x * 2
            }
          .retryWhen(
              RetrySpec.max(10).filter { t -> t is java.lang.RuntimeException }
                  .doBeforeRetry {log.info("BEFORE")}
                  .doAfterRetry {log.info("AFTER")}
          )//onErrorContinue will neutralized retryWhen like it doesn't exist https://github.com/reactor/reactor-addons/issues/210
          .onErrorContinue { e: Throwable?, i: Any -> println("Error For Item $i") } // comment this line for retry to work
          .subscribe { x: Int? -> println(x) }
        assertTrue(true)
    }

    fun blow(counter: Int){
        if(counter == 3 ) throw IllegalArgumentException("nanananana")
    }
}