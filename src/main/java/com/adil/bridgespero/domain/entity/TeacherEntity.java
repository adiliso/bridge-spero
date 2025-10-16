package com.adil.bridgespero.domain.entity;

import com.adil.bridgespero.domain.model.enums.Experience;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "teacher")
@EqualsAndHashCode(callSuper = true, exclude = "groups")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TeacherEntity extends UserEntity {

    @Column(length = 500)
    String bio;

    @Column(name = "profile_picture_url", nullable = false)
    String profilePictureUrl;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    Experience experience;

    @Column(name = "demo_video_url", nullable = false)
    String demoVideoUrl;

    @Column()
    Double rating = 4.0;

    @Column(name = "rating_count")
    Integer ratingCount = 1;

    @OneToMany(
            mappedBy = "teacher",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    List<GroupEntity> groups = new ArrayList<>();
}

