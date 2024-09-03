package com.bz.demo.model.security;


import com.bz.demo.model.BaseEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderClassName = "Builder")
@Entity
@Table(name = "BG_ROLE")
public class Role extends BaseEntity {
    @Column(name = "ROLE")
    private String role;

}
