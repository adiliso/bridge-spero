package com.adil.bridgespero.domain.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "teacher")
@EqualsAndHashCode(callSuper = true, exclude = {"groups"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentEntity extends UserEntity {

    @Column(name = "profile_picture_url")
    String profilePictureUrl;

    @Column(length = 500)
    String bio;

    @ElementCollection
    @CollectionTable(
            name = "student_interest",
            joinColumns = @JoinColumn(name = "student_id")
    )
    @Column(name = "subject")
    List<String> interests;

    @ManyToMany(mappedBy = "students")
    List<GroupEntity> groups;
}
