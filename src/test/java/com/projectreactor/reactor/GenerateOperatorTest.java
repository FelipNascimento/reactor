package com.projectreactor.reactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.io.BufferedReader;
import java.io.FileReader;

public class GenerateOperatorTest {

    @Test
    public void testFileGenerateOperator() {
        String filePath = "C:\\Users\\felip\\Downloads\\projectreactor\\demo\\src\\test\\resources\\example.txt";


        Flux fileFLux = Flux.generate(
                () -> {
                    System.out.println("Creating Buffered Reader");
                    try {
                        return new BufferedReader(new FileReader(filePath));
                    } catch (Exception exception) {
                        throw new RuntimeException(exception);
                    }
                },
                ((bufferedReader, synchronousSink) -> {
                    try {
                        String line = bufferedReader.readLine();
                        if (line != null) {
                            synchronousSink.next(line);
                        } else {
                            synchronousSink.complete();
                        }
                    } catch (Exception exception) {
                        synchronousSink.error(exception);
                    }
                    return bufferedReader;
                }),
                bufferedReader -> {
                    try {
                        System.out.println("End - Closing file");
                        bufferedReader.close();
                    } catch (Exception exception) {
                        throw new RuntimeException(exception);
                    }
                }
        ).log();
//line 1
//line 2
//line 3
        StepVerifier.create(fileFLux)
                .expectNext("line 1")
                .expectNext("line 2")
                .expectNext("line 3")
                .expectComplete().verify();


    }
}
