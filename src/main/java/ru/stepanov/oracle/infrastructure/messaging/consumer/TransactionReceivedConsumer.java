package ru.stepanov.oracle.infrastructure.messaging.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.stepanov.oracle.acl.dto.OpenBankingTransactionDto;
import ru.stepanov.oracle.acl.mapper.OpenBankingTransactionMapper;
import ru.stepanov.oracle.application.usecase.transaction.HandleIncomingTransactionUseCase;

@Component
public class TransactionReceivedConsumer {
    private final OpenBankingTransactionMapper mapper;
    private final HandleIncomingTransactionUseCase useCase;

    public TransactionReceivedConsumer(OpenBankingTransactionMapper mapper, HandleIncomingTransactionUseCase useCase) {
        this.mapper = mapper;
        this.useCase = useCase;
    }

    @RabbitListener(queues = "oracle.inbox")
    public void consume(OpenBankingTransactionDto dto) {
        useCase.execute(mapper.toIncomingTransactionCommand(dto));
    }
}
