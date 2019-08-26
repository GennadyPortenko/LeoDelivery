package com.cmdelivery.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContractorDto {
    long id;
    String name;
    String email;
    Set<SectionDto> sections;
}
