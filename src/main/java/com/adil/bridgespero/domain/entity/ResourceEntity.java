package com.adil.bridgespero.domain.entity;

import com.adil.bridgespero.domain.model.enums.ResourceType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "resources")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResourceEntity extends BaseEntity {

    String name;

    @Column(nullable = false)
    String path;

    @Enumerated(EnumType.STRING)
    ResourceType type;

    @Column(name = "content_type")
    String contentType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    GroupEntity group;
}
