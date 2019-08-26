package com.cmdelivery.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"products"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long sectionId;

    @NonNull
    @NotEmpty
    String name;

    String description;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="contractor_fk")
    Contractor contractor;

    @ToString.Exclude
    @OneToMany(fetch=FetchType.LAZY, mappedBy="section")
    Set<Product> products = new HashSet<>();
}
