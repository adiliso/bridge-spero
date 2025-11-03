package com.adil.bridgespero.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "recording")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RecordingEntity extends BaseEntity{

    @Builder.Default
    @Column(updatable = false)
    UUID uuid = UUID.randomUUID();

    @Column(nullable = false)
    String name;
//
//    @Column(nullable = false)
//    Integer fileSize;
//
//    @Column(name = "start_time", nullable = false)
//    LocalDateTime startTime;
//
//    @Column(name = "end_time", nullable = false)
//    LocalDateTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    GroupEntity group;
}
