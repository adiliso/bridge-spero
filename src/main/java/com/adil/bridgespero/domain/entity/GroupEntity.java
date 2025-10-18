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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"lessonSchedules", "teacher", "subjectCategory", "students"})
@Table(name = "group")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GroupEntity extends BaseEntity {

    @Column(nullable = false)
    String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_category_id")
    SubjectCategoryEntity subjectCategory;

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

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    List<LessonScheduleEntity> lessonSchedules = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    TeacherEntity teacher;

    @ManyToMany
    @JoinTable(
            name = "group_student",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<StudentEntity> students;
}
