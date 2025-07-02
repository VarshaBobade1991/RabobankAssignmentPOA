package nl.rabobank.service;

import com.mongodb.DuplicateKeyException;
import lombok.RequiredArgsConstructor;
import nl.rabobank.authorizations.PowerOfAttorney;
import nl.rabobank.document.PowerOfAttorneyDocument;
import nl.rabobank.Mapper.PoaMapper;
import nl.rabobank.dto.PoaRequest;
import nl.rabobank.dto.PoaResponse;
import nl.rabobank.exception.DuplicateAccountTypeMappingException;
import nl.rabobank.exception.DuplicatePoaException;
import nl.rabobank.exception.PoaNotFoundException;
import nl.rabobank.repository.PowerOfAttorneyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PowerOfAttorneyService implements AuthorizationService, AccessQueryService {
    private final PowerOfAttorneyRepository repository;
    private final PoaMapper mapper;

    private static final Logger log = LoggerFactory.getLogger(PowerOfAttorneyService.class);

    @Override
    public PoaResponse grantAccess(PoaRequest request) throws DuplicatePoaException {
        String grantee = request.getGrantee();
        String accountNumber = request.getAccountNumber();
        String accountType = request.getAccountType();
        log.info("Attempting to grant POA to grantee: {}", grantee);
        checkIfAccessPresent(grantee, accountNumber, request.getAccessType());
        validateAccountUniqueness(accountNumber,accountType);
        try {
            log.info("Granting Power of Attorney to grantee.");
            PowerOfAttorney poa = mapper.toDomain(request);
            PowerOfAttorneyDocument poaDocument = repository.save(mapper.toDocument(poa));
            log.info("Successfully granted POA to grantee: {}", grantee);
            return mapper.toResponse(poaDocument);
        } catch (DuplicateKeyException e) {
            throw new DuplicatePoaException("Access already granted for this grantee, account, and access type.");
        }
    }

    @Override
    public List<PowerOfAttorney> getAccessForGrantee(String grantee) {
        log.info("Retrieve list of access for Grantee ");

        List<PowerOfAttorneyDocument> documents = repository.findAllByGranteeIgnoreCase(grantee);
        if (documents.isEmpty()) {
            throw new PoaNotFoundException("No Power of attorney records found for grantee: " + grantee);
        }
        return documents.stream().map(mapper::docToDomain).collect(Collectors.toList());
    }

    private void checkIfAccessPresent(String grantee, String accountNumber, String accessType) throws DuplicatePoaException {
        boolean alreadyExists = repository.existsByGranteeAndAccountNumberAndAccessType(grantee, accountNumber, accessType);
        if (alreadyExists) {
            log.warn("Duplicate POA detected for grantee: {}, account: {}, accessType: {}", grantee, accountNumber, accessType);
            throw new DuplicatePoaException("Access already granted for this grantee, account, and access type.");
        }
    }

    void validateAccountUniqueness(String accountNumber, String accountType) {
        Optional<PowerOfAttorneyDocument> existing = repository.findByAccountNumber(accountNumber);
        if (existing.isPresent()) {
            String existingType = existing.get().getAccountType();
            if (!existingType.equalsIgnoreCase(accountType)) {
                throw new DuplicateAccountTypeMappingException(
                        String.format("Account %s is already registered as type '%s'",
                                accountNumber, existingType));
            }
        }
    }

}
