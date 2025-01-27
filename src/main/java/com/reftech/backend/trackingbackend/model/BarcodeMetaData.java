package com.reftech.backend.trackingbackend.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Builder
@Data
@Table("barcodes")
public class BarcodeMetaData {
    @Id
    private String id; // Unique ID for the barcode.

    private String name; // Name of the barcode.

    private String description; // Description of the barcode.

    private byte[] image; // Byte array to store the barcode image.
}
