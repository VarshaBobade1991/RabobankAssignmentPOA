package nl.rabobank.Mapper;

import nl.rabobank.account.Account;
import nl.rabobank.account.PaymentAccount;
import nl.rabobank.account.SavingsAccount;
import nl.rabobank.authorizations.Authorization;
import nl.rabobank.authorizations.PowerOfAttorney;
import nl.rabobank.document.PowerOfAttorneyDocument;
import nl.rabobank.dto.*;
import org.springframework.stereotype.Component;

import static nl.rabobank.constants.PoaConstants.PAYMENTS;
import static nl.rabobank.constants.PoaConstants.SAVINGS;

@Component
public class PoaMapper {
    public PowerOfAttorney toDomain(PoaRequest request) {
        return toDomain(request.getGrantee(), request.getGrantor(), request.getAccountType().toUpperCase(), request.getAccountNumber(), request.getAccessType().toUpperCase());
    }

    public PoaAccountAccess toPoaAccountAccessResponse(PowerOfAttorney poa) {
        return new PoaAccountAccess(poa.getAccount().getAccountNumber(), mapToAccountType(poa.getAccount()), poa.getAuthorization().name(), poa.getGrantorName());
    }

    public PoaResponse toResponse(PowerOfAttorneyDocument doc) {
        return new PoaResponse(doc.getGrantee(), doc.getAccountNumber(), doc.getAccountType(), doc.getAccessType());
    }

    public PowerOfAttorneyDocument toDocument(PowerOfAttorney poa) {
        PowerOfAttorneyDocument doc = new PowerOfAttorneyDocument();
        doc.setGrantor(poa.getGrantorName());
        doc.setGrantee(poa.getGranteeName());
        doc.setAccountNumber(poa.getAccount().getAccountNumber());
        doc.setAccountType(poa.getAccount() instanceof SavingsAccount ? SAVINGS : PAYMENTS);
        doc.setAccessType(poa.getAuthorization().name());
        return doc;
    }

    public PowerOfAttorney docToDomain(PowerOfAttorneyDocument doc) {
        return toDomain(doc.getGrantee(), doc.getGrantor(), doc.getAccountType(), doc.getAccountNumber(), doc.getAccessType());
    }

    private PowerOfAttorney toDomain(String grantee, String grantor, String accountType, String accountNumber, String accessType) {
        return PowerOfAttorney.builder()
                .granteeName(grantee)
                .grantorName(grantor)
                .account(getAccount(accountType, accountNumber, grantor))
                .authorization(Authorization.valueOf(accessType.toUpperCase()))
                .build();
    }

    private Account getAccount(String accountType, String accountNumber, String accountHolderName) {
        if (accountType.equals(SAVINGS)) {
            return SavingsAccount.builder()
                    .accountNumber(accountNumber)
                    .accountHolderName(accountHolderName)
                    .balance(0.0)
                    .build();
        } else
            return PaymentAccount.builder()
                    .accountNumber(accountNumber)
                    .accountHolderName(accountHolderName)
                    .balance(0.0)
                    .build();
    }

    private String mapToAccountType(Account account) {
        if (account instanceof SavingsAccount) return SAVINGS;
        else if (account instanceof PaymentAccount) return PAYMENTS;
        throw new IllegalArgumentException("Unknown account subtype");
    }
}
