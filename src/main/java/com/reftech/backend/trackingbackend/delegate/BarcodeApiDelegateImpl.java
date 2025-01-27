package com.reftech.backend.trackingbackend.delegate;

import com.reftech.backend.trackingbackend.api.BarcodesApiDelegate;
import com.reftech.backend.trackingbackend.api.GenerateUniqueBarcodeRequest;
import com.reftech.backend.trackingbackend.api.SaveBarcodeRequest;
import com.reftech.backend.trackingbackend.service.BarcodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class BarcodeApiDelegateImpl implements BarcodesApiDelegate {

    @Autowired
    private BarcodeService barcodeService;
    @Override
    public Mono<ResponseEntity<Resource>> generateUniqueBarcode(Mono<GenerateUniqueBarcodeRequest> generateUniqueBarcodeRequest,
                                                                 ServerWebExchange exchange) {
        return barcodeService.generateBarcode(generateUniqueBarcodeRequest)
                .flatMap(byteArray->Mono.just(new ByteArrayResource(byteArray)))
                .map(byteArrayResource -> ResponseEntity.ok().contentType(MediaType.IMAGE_PNG)
                        .body(byteArrayResource));
    }


    @Override
    public Mono<ResponseEntity<Void>> saveBarcode(Mono<SaveBarcodeRequest> saveBarcodeRequest,
                                                   ServerWebExchange exchange) {

        return barcodeService.saveOrUpdateBarcode(saveBarcodeRequest)
                .map(ResponseEntity::ok);
    }
}