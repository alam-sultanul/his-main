package com.bz.demo.controller;

import com.bz.demo.exception.FileUploadFailedException;
import com.bz.demo.facade.SessionManagementService;
import com.bz.demo.model.doc.Document;
import com.bz.demo.model.doc.DocumentSource;
import com.bz.demo.model.security.User;
import com.bz.demo.service.DocumentService;
import com.bz.demo.util.Constants;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component
public class FileUploadSupport {

    @NonNull private final SessionManagementService sessionManagementService;
    @NonNull private final DocumentService documentService;

    @Value("${app.tmp.upload.dir:${user.home}}")
    public String tmpUploadDir;

    @Value("${app.data.upload.dir:${user.home}}")
    public String dataUploadDir;

    private static final String VALID_IMAGE_EXTENSIONS = "gif|bmp|png|jpe?g";
    private static final String VALID_DOCUMENT_EXTENSIONS = "pdf|ps|docx?|pptx?|pps|xlsx?|odt|txt|rtf|html";
    private static final String VALID_EXTENSIONS_REGEX = ".*\\.(?i)(csv|swf|midi?|webp|zip"
            + "|" + VALID_IMAGE_EXTENSIONS
            + "|" + VALID_DOCUMENT_EXTENSIONS + ")$";


    private String uploadFile(MultipartFile file, DocumentSource documentSource, Long admissionId) {

        String uploadedFilename = StringUtils.trimToNull(StringUtils.trimToNull(cleanUploadFilename(file.getOriginalFilename() + "")));
        if (!isValidFileName(uploadedFilename)) {
            log.error("failed to upload file " + uploadedFilename);
            throw new FileUploadFailedException();
        }
        String filePath = getDocumentDirectory(documentSource, admissionId);
        Path path = Paths.get(filePath);
        createDirectoryIfNotExists(path);
        String fileName = addSuffixToFileName(uploadedFilename, UUID.randomUUID().toString().substring(0, 6));
        try {
            Path copyLocation = Paths.get(filePath + File.separator + fileName);
           /* File dest = new File(copyLocation+ "");
            Thumbnails.of(file.getInputStream())
                    .size(150, 200)
                    .toFile(dest);*/

            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            log.error("failed to upload file " + uploadedFilename + " exception: " + e.getMessage());
            throw new FileUploadFailedException();
        }
        return fileName;
    }

    public String getDownloadUrl(Document document) {
        String data = getDocumentDirectory(document.getDocumentSource(), document.getObjectRefId());
        try {
            Path file = Paths.get(data).resolve(document.getFilename());
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource.getURL().toString();
            } else {
                throw new RuntimeException("Could not read file: ");
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not read file: ", e);
        }
    }

    public File getFile(Document document) {
        String data = getDocumentDirectory(document.getDocumentSource(), document.getObjectRefId());
        try {
            Path file = Paths.get(data).resolve(document.getFilename());
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource.getFile();
            } else {
                throw new RuntimeException("Could not read file: ");
            }
        } catch (Exception e) {
            log.error("Could not read file: " + document.getFilename());
            throw new RuntimeException("Could not read file: ", e);
        }
    }

    private String getDocumentDirectory(DocumentSource documentSource, Long admissionId) {
        return dataUploadDir + File.separator + documentSource.getDirectoryName() + File.separator + admissionId;
    }

    public void createDirectoryIfNotExists(Path path) {
        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
        } catch (IOException e) {
            log.error("Failed to create directory. Please check if the root directories are available");
        }
    }

    public String addSuffixToFileName(String fileName, String suffix) {
        int extensionIndex = fileName.lastIndexOf(".");
        return extensionIndex == -1
                ? fileName + "-" + suffix
                : fileName.substring(0, extensionIndex) + "-" + suffix + fileName.substring(extensionIndex);
    }

    public String cleanUploadFilename(String uploadFileName) {
        if (uploadFileName.contains("\\")) {
            uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf("\\") + 1);
        }
        return uploadFileName.replaceAll(" [ ]+", " ").replaceAll(" ", "-").replaceAll("[^0-9a-zA-Z.\\-_]", "");
    }

    public boolean isValidFileName(String fileName) {
        return !StringUtils.isEmpty(fileName)
                && fileName.toLowerCase().matches(VALID_EXTENSIONS_REGEX);
    }

    public Document uploadUserProfilePicture(User user, MultipartFile file, Long hasUserPhotoId) {
        Document document = null;
        if (file.getSize() > 0) {
            try {
                String uploadedFilename = uploadFile(file, DocumentSource.USER_PHOTO, user.getId());
                if (hasUserPhotoId !=null) {
                    document = documentService.findDocumentById(hasUserPhotoId).orElse(null);
                }

                if (document != null) {
                    document.setFilename(uploadedFilename);
                    document.setContentType(file.getContentType());
                    document.setFileSize(file.getSize());
                    document.setModifiedBy(sessionManagementService.getAuthenticatedUser().getId());
                } else {
                    document = Document.builder()
                            .documentSource(DocumentSource.USER_PHOTO)
                            .objectRefId(user.getId())
                            .filename(uploadedFilename)
                            .contentType(file.getContentType())
                            .fileSize(file.getSize())
                            .activeStatus(Constants.ACTIVE_STATUS)
                            .createdBy(sessionManagementService.getAuthenticatedUser().getId())
                            .modifiedBy(sessionManagementService.getAuthenticatedUser().getId())
                            .build();
                }
            } catch (Exception e) {
                log.error("failed to upload file " + file.getOriginalFilename() + " exception: " + e.getMessage());
            }
        }
        return document;
    }

}
