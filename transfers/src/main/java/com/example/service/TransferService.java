package com.example.service;

import com.example.dto.TransferDto;
import com.example.model.Transfer;

public interface TransferService {

    Transfer createTransfer(TransferDto transferDto);

}
