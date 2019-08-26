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
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long productId;

    @NonNull
    @NotEmpty
    String name;

    @NonNull
    @NotEmpty
    String description;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="section_fk")
    Section section;
}
