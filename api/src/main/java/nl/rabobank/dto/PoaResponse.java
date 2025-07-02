package nl.rabobank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PoaResponse {
    private String grantee;
    private String accountNumber;
    private String accountType;
    private String accessType;
}
