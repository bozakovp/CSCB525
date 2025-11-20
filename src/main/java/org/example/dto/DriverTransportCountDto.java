package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class DriverTransportCountDto {
    private Long driverId;
    private String driverName;
    private Long transportCount;
}
