package br.com.order.ms_order.domain.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderCreatedDTO {
    private String id;
    private String code;
    private String customerEmail;
    private String customerTelephoneNumber;
    private String status;
    private LocalDateTime createdAt;
    private String orderLink;
}
