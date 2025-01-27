package com.reftech.backend.trackingbackend.repository;


import com.reftech.backend.trackingbackend.model.BarcodeMetaData;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface BarcodeRepository extends ReactiveCrudRepository<BarcodeMetaData,String> {
}
