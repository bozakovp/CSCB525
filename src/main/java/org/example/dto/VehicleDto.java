package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class VehicleDto {
    private long id;
    private String type;
    private long companyId;
    private String companyName;
}
