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
public class TransportDto {
    private long id;
    private long vehicleId;
    private String vehicleType;
    private long driverId;
    private String driverName;
    private long transportTypeId;
    private String transportTypeName;
    private String startPoint;
    private String endPoint;
    private LocalDateTime departureDate;
    private LocalDateTime arrivalDate;
    private Float cargoWeight;
    private BigDecimal price;
}
