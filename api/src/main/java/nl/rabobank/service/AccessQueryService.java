package nl.rabobank.service;

import nl.rabobank.authorizations.PowerOfAttorney;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AccessQueryService {
    List<PowerOfAttorney> getAccessForGrantee(String grantee);
}
