package nl.rabobank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PoaAccountAccess {
    private String accountNumber;
    private String accountType;
    private String accessType;
    private String grantor;
}

