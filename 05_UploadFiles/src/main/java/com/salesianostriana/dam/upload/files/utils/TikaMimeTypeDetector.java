package com.salesianostriana.dam.upload.files.utils;

import com.salesianostriana.dam.upload.files.exception.StorageException;
import com.salesianostriana.dam.upload.files.service.s3.S3ObjectResource;
import org.apache.tika.Tika;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;

@Component
public class TikaMimeTypeDetector implements MimeTypeDetector{

    private Tika tika;

    public TikaMimeTypeDetector() {
        tika = new Tika();
    }

    @Override
    public String getMimeType(Resource resource) {
        try {
            // 1) Si viene de S3/MinIO y trae contentType, úsalo
            if (resource instanceof S3ObjectResource s3res && StringUtils.hasText(s3res.getContentType())) {
                return s3res.getContentType();
            }

            // 2) Si es un fichero local, Tika con File (seguro)
            if (resource.isFile()) {
                return tika.detect(resource.getFile());
            }

            // 3) Fallback SIN leer el stream: por nombre/extensión
            String filename = resource.getFilename();
            if (StringUtils.hasText(filename)) {
                String guessed = URLConnection.guessContentTypeFromName(filename);
                if (StringUtils.hasText(guessed)) return guessed;
            }

            // 4) Último recurso
            return "application/octet-stream";

        } catch (IOException ex) {
            throw new StorageException("Error trying to get the MIME type", ex);
        }
    }
}
