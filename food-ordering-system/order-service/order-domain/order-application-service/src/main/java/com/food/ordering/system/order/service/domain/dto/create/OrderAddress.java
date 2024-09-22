package com.food.ordering.system.order.service.domain.dto.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Builder
@AllArgsConstructor
public class OrderAddress {

    @NotBlank
    @Size(max = 50)
    private final String street;

    @NotBlank
    @Size(max = 10)
    private final String postCode;

    @NotBlank
    @Size(max = 50)
    private final String city;
}
