package com.example.merchantransaction.adapter.in.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TransactionResponseDTO {
    private String id;
    private String value;
    private String description;
    private String method;
    private String cardHolderName;
    private String cardExpirationDate;
}
