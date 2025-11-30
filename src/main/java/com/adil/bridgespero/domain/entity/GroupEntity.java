package com.adil.bridgespero.domain.entity;

import com.adil.bridgespero.domain.model.enums.GroupStatus;
import com.adil.bridgespero.domain.model.enums.Language;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true,
        exclude = {"lessonSchedules", "teacher", "category", "users", "recordings"})
@Table(name = "groups")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GroupEntity extends BaseEntity {

    @Column(nullable = false)
    String name;

    String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    CategoryEntity category;

    @Column(name = "start_date", nullable = false)
    LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    LocalDate endDate;

    @Column(name = "max_students", nullable = false)
    Integer maxStudents;

    @Column(name = "min_students")
    Integer minStudents;

    @Column(nullable = false)
    Double price;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    Language language;

    String description;

    String syllabus;

    @Column(nullable = false)
    GroupStatus status;

    @Builder.Default
    @OneToMany(mappedBy = "group",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    List<LessonScheduleEntity> lessonSchedules = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    TeacherDetailEntity teacher;

    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "group_user",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    List<UserEntity> users = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "group",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    List<RecordingEntity> recordings = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "group",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    List<ResourceEntity> resources = new ArrayList<>();
}
