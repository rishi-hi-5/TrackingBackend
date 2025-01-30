package com.reftech.backend.barcodebackend.configuration;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.ReactiveHealthIndicator;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ActuatorReactiveConfiguration implements ReactiveHealthIndicator {
    @Override
    public Mono<Health> health() {
        // Return a custom health status
        return Mono.just(Health.up().build());
    }
}