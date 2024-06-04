package org.smileksey.deal.models;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Passport {
    private UUID passportId;
    private String series;
    private String number;
    private String issueBranch;
    private LocalDate issueDate;
}
