package com.example.controller;

import com.example.dto.TransferDto;
import com.example.model.Transfer;
import com.example.repository.TransferRepository;
import com.example.service.TransferServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

import static com.example.controller.TransferController.TRANSFER_CONTROLLER_PATH;

@AllArgsConstructor
@RestController
@RequestMapping("${base-url}" + TRANSFER_CONTROLLER_PATH)
public class TransferController {

    public static final String TRANSFER_CONTROLLER_PATH = "/transfers";

    private final TransferServiceImpl transferService;

    private final TransferRepository transferRepository;

    @GetMapping
    public List<Transfer> getAllTransfers() {
        return transferRepository.findByOrderByIdDesc();
    }

    @PostMapping
    public Transfer createTransfer(@RequestBody @Valid final TransferDto transferDto) throws ResponseStatusException {
        return transferService.createTransfer(transferDto);
    }

}
