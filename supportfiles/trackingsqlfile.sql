CREATE TABLE barcodes (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    image BYTEA -- Store binary data in this column
);