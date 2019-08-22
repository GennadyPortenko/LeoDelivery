package com.cmdelivery.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int sectionId;

    @NonNull
    @NotEmpty
    String name;

    String description;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="contractor_fk")
    Contractor contractor;
}
