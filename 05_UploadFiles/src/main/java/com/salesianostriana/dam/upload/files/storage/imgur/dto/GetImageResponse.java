package com.salesianostriana.dam.upload.files.storage.imgur.dto;

public record GetImageResponse(
        String success,
        int status,
        GetImageInfo data
) {
}
