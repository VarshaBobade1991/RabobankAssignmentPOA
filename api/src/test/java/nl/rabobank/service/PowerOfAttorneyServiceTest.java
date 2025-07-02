package nl.rabobank.service;

import nl.rabobank.authorizations.PowerOfAttorney;
import nl.rabobank.document.PowerOfAttorneyDocument;
import nl.rabobank.Mapper.PoaMapper;
import nl.rabobank.dto.PoaRequest;
import nl.rabobank.exception.DuplicateAccountTypeMappingException;
import nl.rabobank.exception.DuplicatePoaException;
import nl.rabobank.exception.PoaNotFoundException;
import nl.rabobank.repository.PowerOfAttorneyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static nl.rabobank.constants.PoaConstants.READ;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PowerOfAttorneyServiceTest {

    @Mock
    private PowerOfAttorneyRepository repository;

    @InjectMocks
    private PowerOfAttorneyService service;

    @Mock
    private PoaMapper mapper;

    @Test
    void shouldGrantAccessAndSavePoa() throws DuplicatePoaException {
        String grantee = "Alice";
        String accountNumber = "1234567890";
        String accessType = READ;
        PoaRequest request = new PoaRequest();
        request.setGrantee(grantee);
        request.setAccountNumber(accountNumber);
        request.setAccessType(accessType);
        PowerOfAttorney powerOfAttorney = PowerOfAttorney.builder().build();
        PowerOfAttorneyDocument document = new PowerOfAttorneyDocument(); // Mongo document
        when(repository.existsByGranteeAndAccountNumberAndAccessType(grantee, accountNumber, accessType)).thenReturn(false);
        when(repository.findByAccountNumber(accountNumber)).thenReturn(Optional.empty());
        when(mapper.toDocument(powerOfAttorney)).thenReturn(document);
        when(mapper.toDomain(request)).thenReturn(powerOfAttorney);

        service.grantAccess(request);

        verify(mapper).toDomain(request);
        verify(mapper).toDocument(powerOfAttorney);
        verify(repository).save(document);
    }

    @Test
    void shouldReturnMappedPoaForGrantee() {
        String grantee = "Alice";
        PowerOfAttorneyDocument doc1 = new PowerOfAttorneyDocument();
        PowerOfAttorneyDocument doc2 = new PowerOfAttorneyDocument();
        List<PowerOfAttorneyDocument> docs = List.of(doc1, doc2);

        PowerOfAttorney poa1 = PowerOfAttorney.builder().build();
        PowerOfAttorney poa2 = PowerOfAttorney.builder().build();
        when(repository.findAllByGranteeIgnoreCase(grantee)).thenReturn(docs);
        when(mapper.docToDomain(doc1)).thenReturn(poa1);
        when(mapper.docToDomain(doc2)).thenReturn(poa2);
        List<PowerOfAttorney> result = service.getAccessForGrantee(grantee);

        assertEquals(2, result.size());
        assertTrue(result.containsAll(List.of(poa1, poa2)));
        verify(repository).findAllByGranteeIgnoreCase(grantee);
    }

    @Test
    void shouldThrowExceptionWhenDuplicateAccessIsDetected() {
        String grantee = "Bob";
        String accountNumber = "123456789";
        String accessType = READ;

        PoaRequest request = new PoaRequest();
        request.setGrantee(grantee);
        request.setAccountNumber(accountNumber);
        request.setAccessType(accessType);

        when(repository.existsByGranteeAndAccountNumberAndAccessType(grantee, accountNumber, accessType)).thenReturn(true);
        assertThrows(DuplicatePoaException.class, () -> service.grantAccess(request));

        verifyNoMoreInteractions(mapper, repository);
    }

    @Test
    void shouldThrowPoaNotFoundExceptionWhenNoPoaRecord() {
        String grantee = "Alice";

        when(repository.findAllByGranteeIgnoreCase(grantee)).thenReturn(Collections.emptyList());

        assertThrows(PoaNotFoundException.class, () -> service.getAccessForGrantee(grantee));
        verify(repository).findAllByGranteeIgnoreCase(grantee);
        verifyNoInteractions(mapper);
    }

    @Test
    void shouldThrowExceptionWhenDuplicateAccountTypeMappingOccur() {
        String accountNumber = "1234567890";
        String originalType = "PAYMENT";
        String conflictingType = "SAVINGS";

        PowerOfAttorneyDocument existing = new PowerOfAttorneyDocument();
        existing.setAccountNumber(accountNumber);
        existing.setAccountType(originalType);

        PoaRequest request = new PoaRequest();
        request.setAccountNumber(accountNumber);
        request.setAccountType(conflictingType);

        when(repository.findByAccountNumber(accountNumber))
                .thenReturn(Optional.of(existing));

        assertThrows(DuplicateAccountTypeMappingException.class, () -> {
            service.validateAccountUniqueness(request.getAccountNumber(), request.getAccountType());
        });
    }
}
