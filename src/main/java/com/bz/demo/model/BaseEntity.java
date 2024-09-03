package com.bz.demo.model;


import javax.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public boolean isPersisted() {
        return getId() != null;
    }

    @PrePersist
    private void __prePersist() {
        if (this instanceof Auditable) {
            Auditable auditable = (Auditable) this;
            LocalDateTime now = LocalDateTime.now();
            if (auditable.getModifiedAt() == null) {
                auditable.setModifiedAt(now);
            }
            if (auditable.getCreatedAt() == null) {
                auditable.setCreatedAt(now);
            }
        }
    }

    @PreUpdate
    private void __preUpdate() {
        if (this instanceof Auditable) {
            Auditable auditable = (Auditable) this;
            auditable.setModifiedAt(LocalDateTime.now());
        }
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if ((o == null) || (getClass() != o.getClass())) return false;

        BaseEntity that = (BaseEntity) o;

        return (id != null) && (id.equals(that.id));
    }

    @Override public int hashCode() {
        int hashCode = 11;
        hashCode += null == id ? 0 : id.hashCode() * 19;
        return hashCode;

    }

    @Override public String toString() {
        return "Entity " + getClass().getSimpleName() + '[' + "id=" + id + ']';
    }

}
