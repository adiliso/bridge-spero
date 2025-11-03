package com.adil.bridgespero.domain.entity;

import com.adil.bridgespero.domain.model.enums.Role;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
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

import java.io.Serial;
import java.util.List;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserEntity extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    String surname;

    @Column(unique = true, nullable = false)
    String email;

    @Column(nullable = false)
    String password;

    @Enumerated(EnumType.STRING)
    Role role;

    @Column(name = "profile_picture_url")
    String profilePictureUrl;

    @Column(length = 500)
    String bio;

    @ElementCollection
    @CollectionTable(
            name = "user_interest",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Column(name = "subject")
    List<String> interests;

    @ManyToMany(mappedBy = "students", fetch = FetchType.LAZY)
    List<GroupEntity> groups;
}

