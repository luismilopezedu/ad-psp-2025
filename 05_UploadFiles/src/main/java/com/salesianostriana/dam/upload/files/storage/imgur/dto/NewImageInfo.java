package com.salesianostriana.dam.upload.files.storage.imgur.dto;

public record NewImageInfo(
        String link,
        String id,
        String deletehash
) {
}
