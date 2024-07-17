package org.smileksey.dossier.dto;

import lombok.*;
import org.smileksey.dossier.dto.enums.Theme;

import java.util.UUID;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailMessage {
    private String address;
    private Theme theme;
    private UUID statementId;
}
