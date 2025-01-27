package com.reftech.backend.barcodebackend.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Objects;
import java.util.UUID;

@Builder
@Data
@Table("barcodes")
public class BarcodeMetaData implements Persistable<UUID> {
    @Id
    private UUID id; // Unique ID for the barcode.

    private String name; // Name of the barcode.

    private String description; // Description of the barcode.

    private byte[] image; // Byte array to store the barcode image.

    @Override
    public boolean isNew() {
        boolean result = Objects.isNull(id);
        this.id = result ? UUID.nameUUIDFromBytes(name.getBytes()) : this.id;
        return result;
    }
}
