package com.bz.demo.controller;


import com.bz.demo.exception.NotFoundException;
import com.bz.demo.service.DocumentService;
import com.bz.demo.model.doc.Document;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
@RequestMapping("/document")
@Controller
public class DocumentController {

    @NonNull private final FileUploadSupport fileUploadSupport;
    @NonNull private final DocumentService documentService;

    @GetMapping(value = "/get/{id}")
    public void getFile(HttpServletResponse response, @PathVariable Long id) {
        Document document = documentService.findDocumentById(id).orElseThrow(NotFoundException::new);
        File file = fileUploadSupport.getFile(document);
        response.setContentType(document.getContentType());
        response.setContentLength(document.getFileSize().intValue());
        try {
            InputStream is = new FileInputStream(file);
            IOUtils.copy(is, response.getOutputStream());
        } catch(IOException e) {
            log.error("Could not show thumbnail "+id, e);
        }
    }

    @RequestMapping(value = "/image/{documentId}")
    @ResponseBody
    public byte[] getImage(@PathVariable(value = "documentId") Long documentId) throws IOException {
        Document document = documentService.findDocumentById(documentId).orElseThrow(NotFoundException::new);
        if (document.isImageFile()) {
            File file = fileUploadSupport.getFile(document);
            return Files.readAllBytes(file.toPath());
        }
        return new byte[0];
    }
}
