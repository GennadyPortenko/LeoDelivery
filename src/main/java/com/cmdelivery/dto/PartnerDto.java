package com.cmdelivery.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Min;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PartnerDto {
    long id;
    String name;
    String email;
    String image;
    String logo;
    List<SectionDto> sections;
    @Min(value=0, message="The value must be positive")
    int minTime;
    @Min(value=0, message="The value must be positive")
    int maxTime;
    @Min(value=0, message="The value must be positive")
    int minPrice;
    CategoryDto mainCategory;
}
