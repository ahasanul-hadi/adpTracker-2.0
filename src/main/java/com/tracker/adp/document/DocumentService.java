package com.tracker.adp.document;

import com.tracker.adp.utility.ImageUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Set;


public interface DocumentService {
    public Optional<DocumentEntity> findById(String id);
    public Set<DocumentEntity> upload(MultipartFile[] files, Long row_id, int entity_id) throws IOException;

    public default byte[] getImageBytes(DocumentEntity document) throws IOException {
        if(document.getFileLocation()!=null && !document.getFileLocation().isEmpty())
            return Files.readAllBytes(Paths.get(document.getFileLocation()));
        else
            return ImageUtil.decompressImage(document.getImageData());
    }
}
