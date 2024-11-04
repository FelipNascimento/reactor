package com.projectreactor.reactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.test.StepVerifier;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.concurrent.CompletableFuture;

public class CreateOperatorTest {

    @Test
    public void testFileReader() {
        final var pathFile = "C:\\Users\\felip\\Downloads\\projectreactor\\reactor\\src\\test\\resources\\example.txt";
        final var pathFile2 = "C:\\Users\\felip\\Downloads\\projectreactor\\reactor\\src\\test\\resources\\example2.txt";

        Flux fileFlux = Flux.create(emitter -> {
            CompletableFuture<Void> task1 = CompletableFuture.runAsync(() -> readFileFluxSync(emitter, pathFile));
            CompletableFuture<Void> task2 = CompletableFuture.runAsync(() -> readFileFluxSync(emitter, pathFile2));

            CompletableFuture.allOf(task1, task2).join();
            emitter.complete();
        }).log();

        StepVerifier.create(fileFlux)
                .expectNextCount(6)
                .expectComplete()
                .verify();
    }

    private static void readFileFluxSync(FluxSink<Object> emitter, String pathFile) {
        System.out.println("filePath:" + pathFile + " | " + "Thread: " + Thread.currentThread().getName());
        try (BufferedReader reader = new BufferedReader(new FileReader(pathFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                emitter.next(line);
            }
        } catch (Exception ex) {
            emitter.error(ex);
        }
    }
}
