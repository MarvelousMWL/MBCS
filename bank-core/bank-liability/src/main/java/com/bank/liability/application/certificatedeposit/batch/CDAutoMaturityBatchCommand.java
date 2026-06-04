package com.bank.liability.application.certificatedeposit.batch;

import java.time.LocalDate;
import lombok.Data;

@Data
public class CDAutoMaturityBatchCommand {
    private LocalDate processingDate;
}