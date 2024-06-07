package org.smileksey.deal.models;

import lombok.*;
import org.smileksey.deal.dto.enums.ApplicationStatus;
import org.smileksey.deal.dto.enums.ChangeType;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class StatusHistory {
    private ApplicationStatus status;
    private LocalDateTime time;
    private ChangeType changeType;
}
