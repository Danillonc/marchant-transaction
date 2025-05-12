package com.example.merchantransaction.adapter.in.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReceivableResponseDTO {
    private String id;
    private String status;
    private String createDate;
    private String subtotal;
    private String discount;
    private String total;
}
