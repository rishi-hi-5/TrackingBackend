package com.reftech.backend.barcodebackend.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ShutdownListener implements ApplicationListener<ApplicationFailedEvent> {


    @Override
    public void onApplicationEvent(ApplicationFailedEvent event) {
        log.debug("Application is shutting down due to event: {}", event.getClass().getName());
    }
}