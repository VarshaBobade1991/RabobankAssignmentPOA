package nl.rabobank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.rabobank.validator.ValidAccessType;
import nl.rabobank.validator.ValidAccountType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PoaRequest {

    @NotBlank(message = "Grantor Name is required")
    private String grantor;

    @NotBlank(message = "Grantee name is required")
    private String grantee;

    @NotBlank(message = "Account number is required")
    @Pattern(regexp = "\\d{10}", message = "Account Number must be exactly 10 digits")
    private String accountNumber;

    @NotNull(message = "Account type is required")
    @ValidAccountType
    private String accountType;

    @NotNull(message = "Access type is required")
    @ValidAccessType
    private String accessType;
}

