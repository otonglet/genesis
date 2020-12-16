package be.genesis.rest;

import be.genesis.persistence.CompanyRepository;
import be.genesis.persistence.CompanyToContactRepository;
import be.genesis.persistence.ContactRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MockMvcBase {
    @Autowired
    protected MockMvc mockMvc;
    protected ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    protected CompanyRepository companyRepository;
    @Autowired
    protected ContactRepository contactRepository;
    @Autowired
    protected CompanyToContactRepository companyToContactRepository;

    // TODO should do separate tests. Test validation, exceptions. Update & complete assertions.
    public void setUp() {
        companyRepository.deleteAll();
        contactRepository.deleteAll();
    }
}
