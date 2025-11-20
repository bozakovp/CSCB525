package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class CompanyRevenueDto {
    private Long companyId;
    private String companyName;
    private Long transportCount;
    private BigDecimal totalRevenue;
}
