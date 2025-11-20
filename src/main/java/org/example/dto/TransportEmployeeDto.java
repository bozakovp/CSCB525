package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class TransportEmployeeDto {
    private long id;
    private String name;
    private long companyId;
    private String companyName;
}
