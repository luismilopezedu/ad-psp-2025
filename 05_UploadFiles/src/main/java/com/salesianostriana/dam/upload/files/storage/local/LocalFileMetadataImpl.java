package com.salesianostriana.dam.upload.files.storage.local;

import com.salesianostriana.dam.upload.files.shared.model.AbstractFileMetadata;
import com.salesianostriana.dam.upload.files.shared.model.FileMetadata;
import lombok.experimental.SuperBuilder;


@SuperBuilder
public class LocalFileMetadataImpl extends AbstractFileMetadata {

    public static FileMetadata of(String filename) {
        return LocalFileMetadataImpl.builder()
                .id(filename)
                .filename(filename)
                .build();
    }

}
