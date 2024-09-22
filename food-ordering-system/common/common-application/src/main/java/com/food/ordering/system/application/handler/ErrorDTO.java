package com.food.ordering.system.application.handler;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class ErrorDTO {
    String code;
    String message;
}
