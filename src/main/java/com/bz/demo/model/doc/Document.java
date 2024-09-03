package com.bz.demo.model.doc;

import com.bz.demo.model.Auditable;
import com.bz.demo.model.BaseEntity;
import com.bz.demo.util.FileConstants;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderClassName = "Builder")
@Entity
@Table(name = "DOCUMENTS")
public class Document extends BaseEntity implements Auditable {

    @Column(name = "DOCUMENT_SOURCE")
    private DocumentSource documentSource;

    @Column(name = "OBJECT_REF_ID")
    private Long objectRefId;

    @Column(name = "FILE_NAME")
    private String filename;

    @Column(name = "CONTENT_TYPE")
    private String contentType;

    @Column(name = "FILE_SIZE")
    private Long fileSize;

    @Column(name = "REMARKS")
    private String remarks;

    @Column(name = "ACTIVE_STATUS")
    private int activeStatus;

    @Column(name = "ENTERED_BY")
    private Long createdBy;

    @Column(name = "UPDATED_BY")
    private Long modifiedBy;

    @Column(name = "ENTRY_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "UPDATE_TIMESTAMP")
    private LocalDateTime modifiedAt;


    public boolean isImageFile() {
        return StringUtils.isNotBlank(filename) && filename.matches(FileConstants.VALID_IMAGE_EXTENSIONS_REGEX);
    }

    public String getImageUrl() {
        return isImageFile() ? "/document/image/" + getId() : StringUtils.EMPTY;
    }
}
