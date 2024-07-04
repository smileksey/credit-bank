package org.smileksey.dossier.dto;

import lombok.*;
import org.smileksey.dossier.dto.enums.Theme;

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
