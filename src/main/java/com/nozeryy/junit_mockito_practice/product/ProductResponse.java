package com.nozeryy.junit_mockito_practice.product;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProductResponse(
       Long id,
       String name,
       String description,
       BigDecimal price,
       String category
) {
}
