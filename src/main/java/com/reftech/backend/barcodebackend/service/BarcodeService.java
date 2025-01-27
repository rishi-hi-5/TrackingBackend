package com.reftech.backend.barcodebackend.service;

import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.reftech.backend.barcodebackend.api.BarcodeMetaDataResponse;
import com.reftech.backend.barcodebackend.api.GenerateUniqueBarcodeRequest;
import com.reftech.backend.barcodebackend.api.SaveBarcodeRequest;
import com.reftech.backend.barcodebackend.model.BarcodeMetaData;
import com.reftech.backend.barcodebackend.model.Constants;
import com.reftech.backend.barcodebackend.repository.BarcodeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static com.reftech.backend.barcodebackend.model.Constants.*;

@Service
@Slf4j
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

            try {
                BitMatrix bitMatrix = new MultiFormatWriter().encode(barcodeData, BARCODE_FORMAT, BARCODE_WIDTH, BARCODE_HEIGHT);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                MatrixToImageWriter.writeToStream(bitMatrix, Constants.BARCODE_IMAGE_FORMAT, byteArrayOutputStream);
                return Mono.just(byteArrayOutputStream.toByteArray());
            } catch (WriterException | IOException e) {
                throw new RuntimeException(e);
            }
        };
    }

    public Mono<Void> saveOrUpdateBarcode(Mono<SaveBarcodeRequest> saveBarcodeRequestMono) {
        return saveBarcodeRequestMono.flatMap(saveBarcodeRequest -> {
            Map<String,String> map = (Map<String, String>) saveBarcodeRequest.getMetadata();
            String name = map.get("name");
            String description = map.get("description");

            BarcodeMetaData barcodeMetaData = BarcodeMetaData
                    .builder()
                    .name(name)
                    .description(description)
                    .image(saveBarcodeRequest.getImage())
                    .build();

            return barcodeRepository.save(barcodeMetaData)
                    .flatMap(data->{
                        log.info("Data saved"+data);
                        return Mono.empty();
                    });
        });
    }

    public Mono<BarcodeMetaDataResponse> findBarcodeByName(String name) {

        Mono<BarcodeMetaData> barcodeDataFromDb = barcodeRepository.findByName(name);
        BarcodeMetaDataResponse barcodeMetaData = new BarcodeMetaDataResponse();
        return barcodeDataFromDb.flatMap(data->{
            barcodeMetaData.setId(data.getId());
            barcodeMetaData.setName(data.getName());

            Map<String,String> map = new HashMap<>();
            map.put("description",data.getDescription());
            barcodeMetaData.setMetadata(map);
            barcodeMetaData.setImage(data.getImage());
            return Mono.just(barcodeMetaData);
        });
    }

    public Mono<Void> deleteBarcodeByName(String name) {
        return barcodeRepository.deleteByName(name);
    }

}
