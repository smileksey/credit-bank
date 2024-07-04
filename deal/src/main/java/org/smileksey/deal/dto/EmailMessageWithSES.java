package org.smileksey.deal.dto;

import lombok.*;
import org.smileksey.deal.dto.enums.Theme;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailMessageWithSES {
    private String address;
    private Theme theme;
    private Long statementId;
    private String sesCode;
}
