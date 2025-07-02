package nl.rabobank.service;

import nl.rabobank.dto.PoaRequest;
import nl.rabobank.dto.PoaResponse;
import org.springframework.stereotype.Service;

@Service
public interface AuthorizationService {
    PoaResponse grantAccess(PoaRequest poa);
}
