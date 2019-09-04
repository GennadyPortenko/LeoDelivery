package com.cmdelivery.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MoveProductToSectionDto {
    boolean succeed;
    String errorMessage = "Failed";
    long sectionId;
}
