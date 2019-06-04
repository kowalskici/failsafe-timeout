package com.failsafenotgood;

import net.jodah.failsafe.Failsafe;
import net.jodah.failsafe.RetryPolicy;
import org.junit.Test;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static junit.framework.TestCase.assertTrue;


public class FailsafeTimeoutsTest {

    private static final int MAX_DURATION = 2;

    @Test
    public void failsafeSyncNotGreatTimeout() {
        RetryPolicy<Object> retryPolicy = new RetryPolicy<>()
                .handle(Exception.class)
                .withMaxDuration(Duration.ofSeconds(MAX_DURATION))
                .withDelay(Duration.ofSeconds(1))
                .withMaxRetries(3);


        long start = System.currentTimeMillis();
        String result = Failsafe.with(retryPolicy).get(() -> {
            Thread.sleep(10000);
            return "Done";
        });

        long end = System.currentTimeMillis();
        long durationInSeconds = (end - start) / 1000;
        System.out.println("Duration: " + durationInSeconds + "s");
        assertTrue("I'm passing because I have not a good timeout inside", durationInSeconds > 2);
    }

    @Test
    public void failsafeAsyncNotGreatTimeout() throws ExecutionException, InterruptedException {

        RetryPolicy<Object> retryPolicy = new RetryPolicy<>()
                .handle(Exception.class)
                .withMaxDuration(Duration.ofSeconds(MAX_DURATION))
                .withDelay(Duration.ofSeconds(1))
                .withMaxRetries(3);


        long start = System.currentTimeMillis();
        CompletableFuture<String> future = Failsafe.with(retryPolicy).getAsync(() -> {
            Thread.sleep(10000);
            return "Done";
        });
        String result = future.get();

        long end = System.currentTimeMillis();
        long durationInSeconds = (end - start) / 1000;
        System.out.println("Duration: " + durationInSeconds + "s");
        assertTrue("I'm passing because I have not a good timeout inside", durationInSeconds > 2);
    }

}
