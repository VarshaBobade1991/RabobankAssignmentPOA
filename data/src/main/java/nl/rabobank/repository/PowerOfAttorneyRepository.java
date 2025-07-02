package nl.rabobank.repository;

import nl.rabobank.document.PowerOfAttorneyDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PowerOfAttorneyRepository  extends MongoRepository<PowerOfAttorneyDocument,String> {

    List<PowerOfAttorneyDocument> findAllByGranteeIgnoreCase(String grantee);

    boolean existsByGranteeAndAccountNumberAndAccessType(String grantee, String accountNumber, String accessType);

    Optional<PowerOfAttorneyDocument> findByAccountNumber(String accountNumber);
}
