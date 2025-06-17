package br.com.order.ms_order.infrastructure.adapters.out.bd.dto;


import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Document
public class OrderEntityDTO {
    @Id
    private String id;
    private String uuid;

    @Indexed(unique = true)
    private String code;

    private String customerEmail;
    private String customerTelephoneNumber;
    private String status;
    private LocalDateTime createdAt;
}

