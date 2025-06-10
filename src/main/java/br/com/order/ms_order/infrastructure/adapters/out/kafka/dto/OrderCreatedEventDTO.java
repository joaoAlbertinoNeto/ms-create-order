package br.com.order.ms_order.infrastructure.adapters.out.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class OrderCreatedEventDTO {
    private String uuid;
    private String code;
    private String customerEmail;
    private String customerTelephoneNumber;
    private String status;
    private LocalDateTime createdAt;
}
