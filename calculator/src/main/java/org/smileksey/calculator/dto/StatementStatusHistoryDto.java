package org.smileksey.calculator.dto;

import lombok.Getter;
import lombok.Setter;
import org.smileksey.calculator.dto.enums.ChangeType;
import org.smileksey.calculator.dto.enums.Status;

import java.time.LocalDateTime;

@Getter
@Setter
public class StatementStatusHistoryDto {
    private Status status;
    private LocalDateTime time;
    private ChangeType changeType;
}
