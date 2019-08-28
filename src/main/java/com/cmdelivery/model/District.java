package com.cmdelivery.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class District {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int districtId;

    @NonNull
    @NotEmpty
    String name;

    @NonNull
    @NotEmpty
    String frenchName;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="city_fk")
    City city;
}
