package br.com.order.ms_order.domain.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class OrderDTO {
    private String code;
    private String customerEmail;
    private String customerTelephoneNumber;
    private String status;
    private LocalDateTime createdAt;
    
    @JsonInclude(Include.NON_NULL)
    private String uuid;
}
