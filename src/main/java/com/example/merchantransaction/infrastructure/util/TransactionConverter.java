package com.example.merchantransaction.infrastructure.util;

import com.example.merchantransaction.adapter.in.web.dto.TransactionRequestDTO;
import com.example.merchantransaction.adapter.in.web.dto.TransactionResponseDTO;
import com.example.merchantransaction.domain.model.Transaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TransactionConverter {
    private TransactionConverter() {}

    public static Transaction convertToDomain(TransactionRequestDTO requestDTO) {
        return Transaction.builder()
                .value(new BigDecimal(requestDTO.getValue()))
                .description(requestDTO.getDescription())
                .method(requestDTO.getMethod())
                .cardNumber(requestDTO.getCardNumber())
                .cardHolderName(requestDTO.getCardHolderName())
                .cardExpirationDate(requestDTO.getCardExpirationDate())
                .cardCvv(requestDTO.getCardCvv())
                .build();
    }

    public static List<TransactionResponseDTO> convertToDtoList(List<Transaction> allTransactions) {

        return allTransactions.stream()
                .map(tx -> new TransactionResponseDTO(
                        tx.getId(),
                        tx.getValue().toString(),
                        tx.getDescription(),
                        tx.getMethod(),
                        tx.getCardHolderName(),
                        tx.getCardExpirationDate()
                ))
                .collect(Collectors.toList());
    }

    public static TransactionResponseDTO convertToDto(Optional<Transaction> transaction) {
        return new TransactionResponseDTO(
                transaction.get().getId(),
                transaction.get().getValue().toString(),
                transaction.get().getDescription(),
                transaction.get().getMethod(),
                transaction.get().getCardHolderName(),
                transaction.get().getCardExpirationDate());
    }
}
