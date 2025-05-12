package com.example.merchantransaction.adapter.in.web;

import com.example.merchantransaction.adapter.in.web.dto.ReceivableResponseDTO;
import com.example.merchantransaction.adapter.in.web.dto.ReceivablesSummaryDTO;
import com.example.merchantransaction.adapter.in.web.dto.TransactionResponseDTO;
import com.example.merchantransaction.application.port.in.ReceivableUseCase;
import com.example.merchantransaction.infrastructure.util.ReceivableConverter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/merchant")
@AllArgsConstructor
public class ReceivableController {

    private final ReceivableUseCase receivableUseCase;

    @Operation(summary = "Get all receivables", description = "Recover all receivables executed.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Receivables found",
                    content = @Content(
                            schema = @Schema(implementation = TransactionResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Receivables not found"
            )
    })
    @GetMapping("/receivables")
    public ResponseEntity<ReceivablesSummaryDTO> getSummary(
            @RequestParam String start,
            @RequestParam String end) {
        return ResponseEntity.ok(ReceivableConverter.convertToSummaryDto(receivableUseCase.findByDateRange(start, end)));
    }

    @GetMapping("/receivables/all")
    public ResponseEntity<List<ReceivableResponseDTO>> getAll() {
        return ResponseEntity.ok(ReceivableConverter.convertToDto(receivableUseCase.findAll()));
    }
}
