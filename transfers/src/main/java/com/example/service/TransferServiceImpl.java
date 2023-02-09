package com.example.service;

import com.example.dto.TransferDto;
import com.example.grpc.TransferServiceOuterClass.Status;
import com.example.model.Transfer;
import com.example.repository.TransferRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

import static com.example.grpc.TransferServiceOuterClass.Status.SUCCESS;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Service
@AllArgsConstructor
public class TransferServiceImpl implements TransferService {

    private final TransferRepository transferRepository;

    private final PaymentService paymentService;


    @Override
    public Transfer createTransfer(final TransferDto transferDto) {
        String debitAccountCode = transferDto.getDebitAccountCode();
        String creditAccountCode = transferDto.getCreditAccountCode();
        BigDecimal amount = transferDto.getTransferAmount();
        Status status = paymentService.createPayment(debitAccountCode, creditAccountCode, amount);
        if (status.equals(SUCCESS)) {
            Transfer transfer = new Transfer();
            transfer.setDebitAccountCode(transferDto.getDebitAccountCode());
            transfer.setCreditAccountCode(transferDto.getCreditAccountCode());
            transfer.setTransferAmount(transferDto.getTransferAmount());
            return transferRepository.save(transfer);
        }
        throw new ResponseStatusException(UNPROCESSABLE_ENTITY, "Operation failed");
    }
}
