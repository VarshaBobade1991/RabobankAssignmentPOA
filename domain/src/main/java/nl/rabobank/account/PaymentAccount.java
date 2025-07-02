package nl.rabobank.account;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PaymentAccount implements Account
{
    String accountNumber;
    String accountHolderName;
    Double balance;

    @Override
    public String getAccountNumber() {
        return accountNumber;
    }

    @Override
    public String getAccountHolderName() {
        return accountHolderName;
    }

    @Override
    public Double getBalance() {
        return balance;
    }
}
