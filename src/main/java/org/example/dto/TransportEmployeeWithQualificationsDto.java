package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class TransportEmployeeWithQualificationsDto {
    private long employeeId;
    private String employeeName;
    private long companyId;
    private String companyName;
    private long qualificationId;
    private String qualificationName;
}
