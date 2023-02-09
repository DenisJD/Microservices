package com.example.service;

import com.example.grpc.TransferServiceGrpc.TransferServiceImplBase;
import com.example.grpc.TransferServiceOuterClass.DecimalValue;
import com.example.grpc.TransferServiceOuterClass.TransferRequest;
import com.example.grpc.TransferServiceOuterClass.TransferResponse;
import com.example.grpc.TransferServiceOuterClass.TypeOfChange;
import com.example.repository.AccountRepository;
import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

import static com.example.grpc.TransferServiceOuterClass.Status.FAILURE;
import static com.example.grpc.TransferServiceOuterClass.Status.SUCCESS;
import static com.example.grpc.TransferServiceOuterClass.TypeOfChange.DEBIT;


@GrpcService
@AllArgsConstructor
public class TransferServiceImpl extends TransferServiceImplBase {

    private final AccountRepository accountRepository;

    private final AccountServiceImpl accountService;

    @Override
    public void changeAccountBalance(TransferRequest request,
                                     StreamObserver<TransferResponse> responseObserver) {

        String accountCode = request.getAccount();
        BigDecimal amount = getAmount(request);
        TypeOfChange type = request.getType();

        if (accountRepository.existsByCode(accountCode)) {
            BigDecimal currentBalance = accountRepository.findByCode(accountCode).get().getBalance();

            if (currentBalance.compareTo(amount) < 0 && type.equals(DEBIT)) {
                responseObserver.onNext(failure());
                responseObserver.onCompleted();
                return;
            }
            if (type.equals(DEBIT)) {
                accountService.debitBalance(accountCode, amount);
            } else {
                accountService.creditBalance(accountCode, amount);
            }
            responseObserver.onNext(success());
            responseObserver.onCompleted();
            return;
        }
        responseObserver.onNext(failure());
        responseObserver.onCompleted();
    }

    private BigDecimal getAmount(final TransferRequest request) {
        DecimalValue decimalValue = request.getAmount();
        return new BigDecimal(
            new BigInteger(decimalValue.getValue().toByteArray()),
            decimalValue.getScale(),
            new MathContext(decimalValue.getPrecision())
        );
    }

    private TransferResponse failure() {
        return TransferResponse.newBuilder()
            .setStatus(FAILURE)
            .build();
    }

    private TransferResponse success() {
        return TransferResponse.newBuilder()
            .setStatus(SUCCESS)
            .build();
    }
}
