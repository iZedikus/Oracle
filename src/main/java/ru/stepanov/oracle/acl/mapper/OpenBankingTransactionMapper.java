package ru.stepanov.oracle.acl.mapper;

import org.springframework.stereotype.Component;
import ru.stepanov.oracle.acl.dto.OpenBankingTransactionDto;
import ru.stepanov.oracle.application.usecase.transaction.HandleIncomingTransactionUseCase;
import ru.stepanov.oracle.domain.model.incomingtransaction.CreditDebitCode;

import java.math.BigDecimal;
import java.time.Instant;

@Component
public class OpenBankingTransactionMapper {
    public HandleIncomingTransactionUseCase.IncomingTransactionCommand toIncomingTransactionCommand(OpenBankingTransactionDto dto) {
        return new HandleIncomingTransactionUseCase.IncomingTransactionCommand(
                dto.transactionId,
                dto.accountId,
                dto.paymentToken,
                new BigDecimal(dto.amount),
                dto.currency,
                CreditDebitCode.valueOf(dto.creditDebitIndicator),
                dto.merchantName,
                dto.merchantId,
                String.valueOf(dto.mccCode),
                Instant.parse(dto.bookingDateTime),
                Instant.parse(dto.valueDateTime),
                dto.debtorName,
                dto.creditorName
        );
    }
}
