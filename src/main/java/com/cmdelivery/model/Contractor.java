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
public class Contractor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long contractorId;

    @NonNull
    @NotEmpty
    @Email
    String email;

    @NonNull
    @NotEmpty
    String name;

    String image;

    @NonNull
    int min_time;
    @NonNull
    int max_time;

    @NonNull
    int min_price;

    @NonNull
    @NotEmpty
    String password;
    int active;

    @ToString.Exclude
    @ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(name = "contractor_role", joinColumns = @JoinColumn(name = "contractor_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    Set<Role> roles = new HashSet<>();

    @ToString.Exclude
    @OneToMany(fetch=FetchType.LAZY, mappedBy="contractor")
    Set<Section> sections = new HashSet<>();
}
