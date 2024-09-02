package com.tracker.adp.document;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DocumentStorageService implements DocumentService{

    private final DocumentRepository documentRepository;

    @Value("${application.base.folder}")
    private String BASE_DIR;

    @Value("${document.upload.directory}")
    private String UPLOAD_DIR;

    private Path root=null;

    @PostConstruct
    public void init() {
        try {
            root = Paths.get(BASE_DIR+"/"+UPLOAD_DIR);
            Files.createDirectories(root);
        } catch (IOException e) {
            //throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public Optional<DocumentEntity> findById(String id){
        if(id==null)
            return Optional.empty();
        return documentRepository.findById(id);
    }

    @Override
    public Set<DocumentEntity> upload(MultipartFile[] files, Long row_id, int entity_id) throws IOException{

        return Arrays.stream(files).filter(file->!file.isEmpty() && file.getSize()>0).map(file->{
            String extension="";
            int index = Objects.requireNonNull(file.getOriginalFilename()).lastIndexOf('.');
            if (index > 0) {
                extension = file.getOriginalFilename().substring(index+1);
            }
            String saveFileName= System.currentTimeMillis()+"."+extension;

            String absPath= this.root.resolve(saveFileName).toAbsolutePath().toString();
            log.info("absPath:"+absPath);
            try {
                Files.copy(file.getInputStream(), Path.of(absPath), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


            DocumentEntity doc= DocumentEntity.builder()
                    .saveFileName(saveFileName)
                    .originalFileName(file.getOriginalFilename())
                    .rowID(row_id)
                    .entityID(entity_id)
                    .type(file.getContentType())
                    .fileLocation(absPath).build();


            return documentRepository.save(doc);
        }).collect(Collectors.toSet());

    }
}
