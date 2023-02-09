package com.example.service;


import com.example.grpc.TransferServiceGrpc.TransferServiceBlockingStub;
import com.example.grpc.TransferServiceOuterClass.DecimalValue;
import com.example.grpc.TransferServiceOuterClass.Status;
import com.example.grpc.TransferServiceOuterClass.TransferRequest;
import com.example.grpc.TransferServiceOuterClass.TransferResponse;
import com.example.grpc.TransferServiceOuterClass.TypeOfChange;
import com.google.protobuf.ByteString;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.example.grpc.TransferServiceOuterClass.Status.FAILURE;
import static com.example.grpc.TransferServiceOuterClass.Status.SUCCESS;
import static com.example.grpc.TransferServiceOuterClass.TypeOfChange.CREDIT;
import static com.example.grpc.TransferServiceOuterClass.TypeOfChange.DEBIT;

@Service
public class PaymentService {

    @GrpcClient("accounts")
    private TransferServiceBlockingStub transferStub;

    public Status createPayment(final String debitAccountCode,
                                final String creditAccountCode,
                                final BigDecimal transferAmount) {

        DecimalValue decimalValue = getDecimalValue(transferAmount);

        TransferRequest debitRequest = getRequest(debitAccountCode, decimalValue, DEBIT);
        TransferResponse debitResponse = this.transferStub.changeAccountBalance(debitRequest);
        Status debitStatus = debitResponse.getStatus();

        if (debitStatus.equals(SUCCESS)) {
            TransferRequest creditRequest = getRequest(creditAccountCode, decimalValue, CREDIT);
            TransferResponse creditResponse = this.transferStub.changeAccountBalance(creditRequest);
            Status creditStatus = creditResponse.getStatus();

            if (creditStatus.equals(FAILURE)) {
                TransferRequest refundRequest = getRequest(debitAccountCode, decimalValue, CREDIT);
                TransferResponse refundResponse = this.transferStub.changeAccountBalance(refundRequest);
                return FAILURE;
            } else {
                return SUCCESS;
            }
        } else {
            return FAILURE;
        }
    }

    private DecimalValue getDecimalValue(final BigDecimal transferAmount) {
        return DecimalValue.newBuilder()
            .setScale(transferAmount.scale())
            .setPrecision(transferAmount.precision())
            .setValue(ByteString.copyFrom(transferAmount.unscaledValue().toByteArray()))
            .build();
    }

    private TransferRequest getRequest(final String account,
                                       final DecimalValue decimalValue,
                                       final TypeOfChange typeOfChange) {
        return TransferRequest.newBuilder()
            .setAccount(account)
            .setAmount(decimalValue)
            .setType(typeOfChange)
            .build();
    }
}
