package com.cmdelivery.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(exclude = {"sections"})
public class Partner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long partnerId;

    @NonNull
    @NotEmpty
    @Email
    String email;

    @NonNull
    @NotEmpty
    String name;

    String image;

    @NonNull
    int minTime;
    @NonNull
    int maxTime;

    @NonNull
    int minPrice;

    @NonNull
    @NotEmpty
    String password;
    int active;

    @NonNull
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="main_category_fk")
    Category mainCategory;

    @ToString.Exclude
    @ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(name = "partner_role", joinColumns = @JoinColumn(name = "partner_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    Set<Role> roles = new HashSet<>();

    @ToString.Exclude
    @OneToMany(fetch=FetchType.LAZY, mappedBy="partner")
    Set<Section> sections = new HashSet<>();
}
