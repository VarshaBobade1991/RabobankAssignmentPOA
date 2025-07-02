package nl.rabobank.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
@CompoundIndexes({
        @CompoundIndex(name = "unique_grantee_account_access", def = "{'grantee': 1, 'accountNumber': 1, 'accessType': 1}", unique = true)
})
public class PowerOfAttorneyDocument {
    @Id
    private String id;
    private String grantor;
    private String grantee;
    private String accountNumber;
    private String accountType;
    private String accessType;
}
