package com.cmdelivery.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(exclude = {"districts"})
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int cityId;

    @NonNull
    @NotEmpty
    String nameEn;

    @NonNull
    @NotEmpty
    String nameFr;

    @ToString.Exclude
    @OneToMany(fetch=FetchType.LAZY, mappedBy="city")
    Set<District> districts = new HashSet<>();
}
