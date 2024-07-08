package org.smileksey.deal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SESCodeDto {
    @NotBlank(message = "'sesCode' is not specified")
    @Schema(description = "SES code",
            example = "3971e197-3bb6-4a9a-9266-ab396b8b1693")
    private String sesCode;
}
