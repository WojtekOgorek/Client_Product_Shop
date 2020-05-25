package ogorek.wojciech.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Statistic {
   private BigDecimal min;
   private BigDecimal max;
   private BigDecimal avg;
}
