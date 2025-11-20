package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class TransportTypeWithQualificationsDto {
    private long transportTypeId;
    private String transportTypeName;
    private String transportTypeDescription;
    private long qualificationId;
    private String qualificationName;
}
