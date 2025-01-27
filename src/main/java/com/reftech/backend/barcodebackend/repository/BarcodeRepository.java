package com.reftech.backend.barcodebackend.repository;


import com.reftech.backend.barcodebackend.model.BarcodeMetaData;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface BarcodeRepository extends ReactiveCrudRepository<BarcodeMetaData, UUID> {
    Mono<BarcodeMetaData> findByName(String name);

    Mono<Void> deleteByName(String name);
}
