package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class TransportWithCompanyDto {
    private long transportId;
    private String startPoint;
    private String endPoint;
    private LocalDateTime departureDate;
    private LocalDateTime arrivalDate;
    private BigDecimal price;
    private long companyId;
    private String companyName;
    private String vehicleType;
    private String driverName;
}
