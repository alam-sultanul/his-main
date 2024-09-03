package com.bz.demo.repository;


import com.bz.demo.model.doc.Document;
import com.bz.demo.model.doc.DocumentSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
Optional<Document>findByDocumentSourceAndObjectRefIdAndActiveStatus(DocumentSource documentSource, Long objectRefId, Integer activeStatus);
}
