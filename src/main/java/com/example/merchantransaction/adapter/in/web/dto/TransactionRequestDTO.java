package com.example.merchantransaction.adapter.in.web.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionRequestDTO {

    @NotNull
    @DecimalMin(value = "0.01", message = "O valor da transação deve ser maior que zero.")
    private String value;

    @NotBlank
    private String description;

    @NotBlank
    @Pattern(regexp = "credit_card|debit_card", message = "O método deve ser 'credit_card' ou 'debit_card'.")
    private String method;

    @NotBlank
    @Size(min = 16, max = 16, message = "O número do cartão deve ter 16 dígitos.")
    private String cardNumber;

    @NotBlank
    private String cardHolderName;

    @NotBlank
    @Pattern(regexp = "(0[1-9]|1[0-2])/\\d{2}", message = "A data de expiração deve estar no formato MM/YY.")
    private String cardExpirationDate;

    @NotBlank
    @Size(min = 3, max = 4, message = "O CVV deve ter 3 ou 4 dígitos.")
    private String cardCvv;
}
