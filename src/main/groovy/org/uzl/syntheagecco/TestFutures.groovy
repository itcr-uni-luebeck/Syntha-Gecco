package org.uzl.syntheagecco

import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors

class TestFutures {

    static void main(String[] args){

        def request = {new URL("https://442920f2-ab87-481b-b599-e252dae4dd45.mock.pstmn.io/hello").text}
        def operation = {Math.pow(4,4)}

        def start = System.currentTimeMillis()
        loop(operation, 1000000)
        def end = System.currentTimeMillis()
        println "[Loop] Time elapsed: ${end - start} ms"

        start = System.currentTimeMillis()
        loopFutures(operation, 1000000)
        end = System.currentTimeMillis()
        println "[Futures] Time elapsed: ${end - start} ms"

        start = System.currentTimeMillis()
        loopFuturesParallel(operation, 1000000)
        end = System.currentTimeMillis()
        println "[Parallel] Time elapsed: ${end - start} ms"

    }

    static void loop(Closure closure, int cnt){
        while(cnt-- > 0) /*print*/ closure.call()
        println ""
    }

    static void loopFutures(Closure closure, int cnt){
        def futures = []
        while(cnt-- > 0) futures << CompletableFuture.supplyAsync(closure)//.thenApply(System.out.&print)
        def future = CompletableFuture.allOf(futures as CompletableFuture<?>[])
        future.get()
        println ""
    }

    static void loopFuturesParallel(Closure closure, int cnt){
        def pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
        try{
            def futures = []
            while(cnt-- > 0) futures << CompletableFuture.supplyAsync(closure, pool)//.thenApply(System.out.&print)
            def future = CompletableFuture.allOf(futures as CompletableFuture<?>[])
            future.get()
            println ""
        }
        finally {
            pool.shutdown()
        }
    }

}
