package com.cmdelivery.dto;

import com.cmdelivery.model.Category;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Min;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryDto {
    int id;
    String nameEn;
    String nameFr;
}
