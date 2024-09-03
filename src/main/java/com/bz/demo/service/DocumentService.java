package com.bz.demo.service;


import com.bz.demo.model.doc.Document;
import com.bz.demo.model.doc.DocumentSource;

import java.util.Optional;


public interface DocumentService {
    Optional<Document> findDocumentById(Long documentId);
    Document saveDocument(Document document);
    Optional<Document> findDocumentBySourceAndObject(DocumentSource documentSource, Long objectRefId);
}
