package com.salesianostriana.dam.upload.files.service.s3;

import com.salesianostriana.dam.upload.files.model.AbstractFileMetadata;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
//@AllArgsConstructor
@SuperBuilder
public class S3FileMetadataImpl extends AbstractFileMetadata {

    public static S3FileMetadataImpl of(String key, String filename, String url, String deleteId, String deleteUrl) {
        return S3FileMetadataImpl.builder()
                .id(key)
                .filename(filename)
                .URL(url)
                .deleteId(deleteId)
                .deleteURL(deleteUrl)
                .build();
    }

    public static S3FileMetadataImpl of(String key, String filename, String url) {
        return S3FileMetadataImpl.builder()
                .id(key)
                .filename(filename)
                .URL(url)
                .deleteId(key)
                .deleteURL(null)
                .build();
    }
}

