package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferDto {

    @NotBlank
    private String debitAccountCode;

    @NotBlank
    private String creditAccountCode;

    @Positive
    @Digits(integer = 10, fraction = 2)
    private BigDecimal transferAmount;

    @AssertTrue(message = "Accounts must be different")
    private boolean isValidAccountsCode() {
        return !debitAccountCode.equals(creditAccountCode);
    }

}
