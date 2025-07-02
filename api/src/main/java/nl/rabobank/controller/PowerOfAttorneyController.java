package nl.rabobank.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import nl.rabobank.dto.PoaRequest;
import nl.rabobank.dto.PoaAccountAccess;
import nl.rabobank.Mapper.PoaMapper;
import nl.rabobank.dto.PoaResponse;
import nl.rabobank.exception.DuplicatePoaException;
import nl.rabobank.service.AccessQueryService;
import nl.rabobank.service.AuthorizationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/poa")
@RequiredArgsConstructor
public class PowerOfAttorneyController {

    private final AuthorizationService authorizationService;

    private final AccessQueryService accessQueryService;

    private final PoaMapper mapper;

    // Grant Access
    @Operation(summary = "Create a new Power of Attorney by granting access to an account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "POA created"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PoaResponse> grant(@Valid @RequestBody PoaRequest request) throws DuplicatePoaException {
        PoaResponse powerOfAttorney = authorizationService.grantAccess(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(powerOfAttorney);
    }

    // Get access granted to a grantee
    @Operation(summary = "Retrieve list of accounts allocated to a grantee", description = "Returns all POA records for the given grantee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of Accounts"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Grantee not found")
    })
    @GetMapping("/grantee/{grantee}")
    public List<PoaAccountAccess> getAccessForGrantee(@PathVariable String grantee) {
        return accessQueryService.getAccessForGrantee(grantee)
                .stream().map(mapper::toPoaAccountAccessResponse).collect(Collectors.toList());
    }


}
