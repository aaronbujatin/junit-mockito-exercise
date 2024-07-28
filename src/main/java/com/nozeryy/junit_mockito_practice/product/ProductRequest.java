package com.nozeryy.junit_mockito_practice.product;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductRequest(@NotNull(message = "Id cannot be null") Long id,
                             @NotNull(message = "Name cannot be null") String name,
                             @NotNull(message = "Description cannot be null") String description,
                             @NotNull(message = "Price cannot be null") BigDecimal price,
                             @NotNull(message = "Category cannot be null") String category) {
}
