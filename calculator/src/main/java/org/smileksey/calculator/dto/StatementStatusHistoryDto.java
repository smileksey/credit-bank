package org.smileksey.calculator.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class StatementStatusHistoryDto {
    private Status status;
    private LocalDateTime time;
    private ChangeType changeType;
}
