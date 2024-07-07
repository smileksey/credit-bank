package org.smileksey.deal.dto;

import lombok.*;
import org.smileksey.deal.dto.enums.Theme;

import java.util.UUID;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailMessageWithSES {
    private String address;
    private Theme theme;
    private UUID statementId;
    private String sesCode;
}
