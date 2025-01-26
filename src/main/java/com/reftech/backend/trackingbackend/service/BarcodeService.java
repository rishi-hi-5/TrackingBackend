package com.reftech.backend.trackingbackend.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.reftech.backend.trackingbackend.api.GenerateUniqueBarcodeRequest;
import com.reftech.backend.trackingbackend.model.Constants;
import com.reftech.backend.trackingbackend.repository.BarcodeRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.function.Function;

import static com.reftech.backend.trackingbackend.model.Constants.BARCODE_HEIGHT;
import static com.reftech.backend.trackingbackend.model.Constants.BARCODE_WIDTH;

@Service
public class BarcodeService {
    @Autowired
    private BarcodeRepository barcodeRepository;

    public Mono<byte[]> generateBarcode(Mono<GenerateUniqueBarcodeRequest> generateUniqueBarcodeRequestMono){
        return generateUniqueBarcodeRequestMono.flatMap(mapToBarcode())
        .subscribeOn(Schedulers.boundedElastic())
        .publishOn(Schedulers.parallel());
    }

    private static Function<GenerateUniqueBarcodeRequest, Mono<byte[]>> mapToBarcode() {
        return (generateUniqueBarcodeRequest) -> {
            String barcodeData = generateUniqueBarcodeRequest.getBarcodeData();
            BarcodeFormat barcodeFormat = BarcodeFormat.CODE_128;

            try {
                BitMatrix bitMatrix = new MultiFormatWriter().encode(barcodeData, barcodeFormat, BARCODE_WIDTH, BARCODE_HEIGHT);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                MatrixToImageWriter.writeToStream(bitMatrix, Constants.BARCODE_FORMAT, byteArrayOutputStream);
                return Mono.just(byteArrayOutputStream.toByteArray());
            } catch (WriterException | IOException e) {
                throw new RuntimeException(e);
            }
        };
    }
}
