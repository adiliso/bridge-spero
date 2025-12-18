package com.adil.bridgespero.domain.entity;

import com.adil.bridgespero.domain.model.enums.Role;
import com.adil.bridgespero.domain.model.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
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
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serial;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@EqualsAndHashCode(callSuper = true)
@SQLRestriction("status != 'DELETED'")
@FieldDefaults(level = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE users SET status = 'DELETED' WHERE id = ?")
public class UserEntity extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    String name;

    String surname;

    @Column(unique = true, nullable = false)
    String email;

    @JsonIgnore
    @Column(nullable = false)
    String password;

    @Column(name = "enabled", nullable = false)
    Boolean enabled;

    @Column(name = "agreed_to_terms", nullable = false)
    Boolean agreedToTerms;

    @Enumerated(EnumType.STRING)
    Role role;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    Status status = Status.ACTIVE;

    @Column(name = "profile_picture_url")
    String profilePictureUrl;

    @Column(name = "background_image_url")
    String backgroundImageUrl;

    @Column(length = 1000)
    String bio;

    @Column(name = "phone_code", length = 5, nullable = false)
    String phoneCode;

    @Column(name = "phone_number", length = 15, nullable = false)
    String phoneNumber;

    String phone;

    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    Set<UserInterestEntity> interests;

    @ManyToMany(mappedBy = "users", fetch = FetchType.LAZY)
    List<GroupEntity> groups;
}

