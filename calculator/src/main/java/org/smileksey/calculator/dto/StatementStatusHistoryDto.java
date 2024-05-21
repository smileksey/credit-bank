package org.smileksey.calculator.dto;

import java.time.LocalDateTime;

public class StatementStatusHistoryDto {
    private Status status;
    private LocalDateTime time;
    private ChangeType changeType;
}
