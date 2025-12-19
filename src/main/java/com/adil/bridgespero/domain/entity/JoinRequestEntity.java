package com.adil.bridgespero.domain.entity;

import com.adil.bridgespero.domain.model.enums.JoinRequestStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Table(name = "join_request")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JoinRequestEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "student_id")
    UserEntity student;

    @ManyToOne
    @JoinColumn(name = "group_id")
    GroupEntity group;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    JoinRequestStatus status;
}
