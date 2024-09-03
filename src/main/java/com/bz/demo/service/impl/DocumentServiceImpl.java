package com.bz.demo.service.impl;

import com.bz.demo.repository.DocumentRepository;
import com.bz.demo.model.doc.Document;
import com.bz.demo.model.doc.DocumentSource;
import com.bz.demo.service.DocumentService;
import com.bz.demo.util.Constants;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class DocumentServiceImpl implements DocumentService {


    @NonNull private final DocumentRepository documentRepository;

    @Override
    public Optional<Document> findDocumentById(Long documentId) {
        return documentRepository.findById(documentId);
    }

    @Override
    public Document saveDocument(Document document) {
        return documentRepository.save(document);
    }

    @Override
    public Optional<Document> findDocumentBySourceAndObject(DocumentSource documentSource, Long objectRefId) {
        return documentRepository.findByDocumentSourceAndObjectRefIdAndActiveStatus(documentSource,objectRefId, Constants.ACTIVE_STATUS);
    }
}
