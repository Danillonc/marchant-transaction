package com.example.merchantransaction.adapter.in.web;

import com.example.merchantransaction.adapter.in.web.dto.TransactionRequestDTO;
import com.example.merchantransaction.adapter.in.web.dto.TransactionResponseDTO;
import com.example.merchantransaction.adapter.in.web.exception.TransactionException;
import com.example.merchantransaction.application.port.in.TransactionUseCase;
import com.example.merchantransaction.infrastructure.util.TransactionConverter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/merchant")
@AllArgsConstructor
public class TransactionController {

    private final TransactionUseCase transactionUseCase;

    @Operation(summary = "Create a transaction", description = "Create a transaction with request body parameters")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", description = "Transactions created",
                    content = @Content(
                            schema = @Schema(implementation = TransactionRequestDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Transaction not created"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Transaction request not proccessable"
            )
    })
    @PostMapping("/transaction/create")
    public ResponseEntity<Void> createTransaction(@Valid @RequestBody TransactionRequestDTO requestDTO) throws TransactionException {
        this.transactionUseCase.createTransaction(TransactionConverter.convertToDomain(requestDTO));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Get all transactions", description = "Recover all transactions executed.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Transactions found",
                    content = @Content(
                            schema = @Schema(implementation = TransactionResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Transactions not found"
            )
    })
    @GetMapping
    public ResponseEntity<List<TransactionResponseDTO>> getAllTransactions() {
        List<TransactionResponseDTO> transactions = TransactionConverter.convertToDtoList(transactionUseCase.getAllTransactions());
        return ResponseEntity.ok(transactions);
    }

    @Operation(summary = "Get the transaction by ID", description = "Recover the transaction by provided id.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Transaction found",
                    content = @Content(
                            schema = @Schema(implementation = TransactionResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Transaction not found"
            )
    })
    @GetMapping("/transaction/{id}")
    public ResponseEntity<TransactionResponseDTO> getTransaction(@PathVariable String id) {
        return ResponseEntity.ok(TransactionConverter.convertToDto(transactionUseCase.findById(id)));
    }

}
