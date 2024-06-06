package org.smileksey.deal.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.smileksey.deal.dto.enums.Gender;
import org.smileksey.deal.dto.enums.MaritalStatus;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FinishRegistrationRequestDto {

    @NotNull(message = "'gender' is not specified")
    @Schema(
            description = "Gender",
            example = "MALE")
    private Gender gender;

    @NotNull(message = "'maritalStatus' is not specified")
    @Schema(
            description = "Martial status",
            example = "MARRIED")
    private MaritalStatus maritalStatus;

    @NotNull(message = "'dependentAmount' is not specified")
    @Schema(
            description = "Amount of dependents",
            example = "0")
    private Integer dependentAmount;

    @NotNull(message = "'passportIssueDate' is not specified")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(
            description = "Passport issue date",
            example = "2020-01-10")
    private LocalDate passportIssueDate;

    @NotBlank(message = "'passportIssueBranch' is not specified")
    @Size(min = 10, max = 100, message = "'passportIssueBranch' must contain 10 to 100 symbols")
    @Schema(
            description = "Branch issued the passport",
            example = "УВД Советского р-на гор. Нижнего Новгорода")
    private String passportIssueBranch;

    @NotNull(message = "'employment' is not specified")
    @Valid
    private EmploymentDto employment;

    @NotBlank(message = "'accountNumber' is not specified")
    @Schema(
            description = "Account number",
            example = "123456789")
    private String accountNumber;

}
