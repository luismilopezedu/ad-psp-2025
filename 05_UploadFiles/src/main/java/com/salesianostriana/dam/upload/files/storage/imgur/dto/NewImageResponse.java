package com.salesianostriana.dam.upload.files.storage.imgur.dto;

public record NewImageResponse(
        String success,
        int status,
        NewImageInfo data
) {
}
