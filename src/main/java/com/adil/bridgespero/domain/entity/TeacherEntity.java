package com.adil.bridgespero.domain.entity;

import com.adil.bridgespero.domain.model.enums.Experience;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "teacher")
@EqualsAndHashCode(exclude = "groups")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TeacherEntity {

    @Id
    Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    UserEntity user;

    @Column(length = 500)
    String bio;

    @Column(name = "profile_picture_url", nullable = false)
    String profilePictureUrl;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    Experience experience;

    @Column(name = "demo_video_url", nullable = false)
    String demoVideoUrl;

    @Builder.Default
    Double rating = 4.0;

    @Builder.Default
    @Column(name = "rating_count")
    Integer ratingCount = 1;

    @ElementCollection
    @Builder.Default
    Set<String> subjects = new HashSet<>();

    @Builder.Default
    @OneToMany(
            mappedBy = "teacher",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    List<GroupEntity> groups = new ArrayList<>();
}

