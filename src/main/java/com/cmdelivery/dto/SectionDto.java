package com.cmdelivery.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SectionDto {
    long id;
    String name;
    String description;
    List<ProductDto> products;
}
