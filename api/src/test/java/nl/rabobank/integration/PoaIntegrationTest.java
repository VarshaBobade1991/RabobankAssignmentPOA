package nl.rabobank.integration;

import nl.rabobank.config.NoSecurityConfig;
import nl.rabobank.document.PowerOfAttorneyDocument;
import nl.rabobank.repository.PowerOfAttorneyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static nl.rabobank.constants.PoaConstants.READ;
import static nl.rabobank.constants.PoaConstants.SAVINGS;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(NoSecurityConfig.class)
public class PoaIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PowerOfAttorneyRepository repository;

    @BeforeEach
    void setup() {
        repository.deleteAll();
        repository.save(new PowerOfAttorneyDocument("1", "Bob Smith", "john.doe", "123", SAVINGS, READ));
    }

    @Test
    void shouldReturnPoaForGrantee() throws Exception {
        mockMvc.perform(get("/api/v1/poa/grantee/john.doe")).andExpect(status().isOk()).andExpect(jsonPath("$[0].accessType").value(READ));
    }

    @Test
    void shouldCreatePOASuccessfully() throws Exception {
        String validJson = "{\n" + "  \"grantor\": \"Mary\",\n" + "  \"grantee\": \"john.doe\",\n" + "  \"accountNumber\": \"1231231231\",\n" + "  \"accessType\": \"READ\",\n" + "  \"accountType\": \"SAVINGS\"\n" + "}";
        mockMvc.perform(post("/api/v1/poa").contentType(MediaType.APPLICATION_JSON).content(validJson)).andExpect(status().isCreated());
    }

    @Test
    void shouldReturnEmptyListForUnknownGrantee() throws Exception {
        mockMvc.perform(get("/api/v1/poa/grantee/doe")).andExpect(status().isNotFound()).andExpect(jsonPath("$.error").value("No Power of attorney records found for grantee: doe"));
    }

    @Test
    void shouldRejectInvalidAccessType() throws Exception {
        String invalidJson = "{\n" + "  \"grantee\": \"john.doe\",\n" + "  \"accessType\": \"InvalidAccess\",\n" + "  \"accountType\": \"SAVINGS\"\n" + "}";
        mockMvc.perform(post("/api/v1/poa").contentType(MediaType.APPLICATION_JSON).content(invalidJson)).andExpect(status().isBadRequest()).andExpect(jsonPath("$.accessType").value("Access type allowed values are: READ, WRITE"));
    }

    @Test
    void shouldRejectInvalidAccountType() throws Exception {
        String invalidJson = "{\n" + "  \"grantee\": \"john.doe\",\n" + "  \"accessType\": \"READ\",\n" + "  \"accountType\": \"InvalidAccount\"\n" + "}";
        mockMvc.perform(post("/api/v1/poa").contentType(MediaType.APPLICATION_JSON).content(invalidJson)).andExpect(status().isBadRequest()).andExpect(jsonPath("$.accountType").value("Account type allowed values are: SAVINGS, PAYMENTS"));
    }

    @Test
    void shouldNotCreateDuplicatePoaForSameGrantee() throws Exception {
        String validJson = "{\n" + "  \"grantor\": \"Mary\",\n" + "  \"grantee\": \"john.doe\",\n" + "  \"accountNumber\": \"1231231231\",\n" + "  \"accessType\": \"READ\",\n" + "  \"accountType\": \"SAVINGS\"\n" + "}";

        // First attempt - should succeed
        mockMvc.perform(post("/api/v1/poa").contentType(MediaType.APPLICATION_JSON).content(validJson))
                .andExpect(status().isCreated());

        // Second attempt with same data - should fail
        mockMvc.perform(post("/api/v1/poa").contentType(MediaType.APPLICATION_JSON).content(validJson))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.error").value("Access already granted for this grantee, account, and access type."));
    }

    @Test
    void shouldAllowCaseInsensitiveAccessType() throws Exception {
        String validJson = "{\n" + "  \"grantor\": \"Mary\",\n" + "  \"grantee\": \"john.doe\",\n" + "  \"accountNumber\": \"1231231231\",\n"
                + "  \"accessType\": \"read\",\n"
                + "  \"accountType\": \"savings\"\n" + "}";

        mockMvc.perform(post("/api/v1/poa").contentType(MediaType.APPLICATION_JSON).content(validJson))
                .andExpect(status().isCreated());
    }
}
