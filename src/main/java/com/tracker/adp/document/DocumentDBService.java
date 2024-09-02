package com.tracker.adp.document;

import com.tracker.adp.utility.ImageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Primary
public class DocumentDBService implements DocumentService{
    private final DocumentRepository documentRepository;

    @Override
    public Optional<DocumentEntity> findById(String id) {
        return documentRepository.findById(id);
    }

    @Override
    public Set<DocumentEntity> upload(MultipartFile[] files, Long row_id, int entity_id) throws IOException {
        return Arrays.stream(files).filter(file->!file.isEmpty() && file.getSize()>0).map(file->{
            String extension="";
            int index = Objects.requireNonNull(file.getOriginalFilename()).lastIndexOf('.');
            if (index > 0) {
                extension = file.getOriginalFilename().substring(index+1);
            }
            String saveFileName= System.currentTimeMillis()+"."+extension;

            DocumentEntity doc= null;
            try {
                doc = DocumentEntity.builder()
                        .saveFileName(saveFileName)
                        .originalFileName(file.getOriginalFilename())
                        .rowID(row_id)
                        .entityID(entity_id)
                        .fileLocation(null)
                        .type(file.getContentType())
                        .imageData(ImageUtil.compressImage(file.getBytes())).build();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


            return documentRepository.save(doc);
        }).collect(Collectors.toSet());
    }
}
