package com.cmdelivery.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Min;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PartnerSettingsDto {
    @Min(value=0, message="The value must be positive")
    int minTime;
    @Min(value=0, message="The value must be positive")
    int maxTime;
    @Min(value=0, message="The value must be positive")
    int minPrice;
}
