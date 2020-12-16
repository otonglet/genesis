package be.genesis.rest;

import be.genesis.dto.Company;
import be.genesis.dto.CompanyAddress;
import be.genesis.dto.Contact;
import be.genesis.persistence.CompanyEntity;
import be.genesis.persistence.CompanyToContact;
import be.genesis.persistence.ContactEntity;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ContactControllerEndToEndTest extends MockMvcBase {

    @Test
    public void bigFatTestForLazyGuy() throws Exception {
        // create

        Contact contactToCreate = Contact.builder()
                .firstName("john")
                .lastName("doe")
                .address("address1")
                .vatNr(null)
                .companies(Sets.newHashSet(Lists.newArrayList(Company.builder()
                        .vatNr("1234567890")
                        .build())))
                .build();

        mockMvc.perform(post("/contact")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(contactToCreate)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isNumber());

        List<ContactEntity> createdContacts = contactRepository.findAll();
        assertThat(createdContacts).hasSize(1);
        assertThat(createdContacts.get(0).getFirstName()).isEqualTo("john");

        List<CompanyEntity> createdCompanies = companyRepository.findAll();
        assertThat(createdCompanies).hasSize(1);
        assertThat(createdCompanies.get(0).getVatNr()).isEqualTo("1234567890");

        List<CompanyToContact> createdC2c = companyToContactRepository.findAll();
        assertThat(createdC2c).hasSize(1);
        assertThat(createdC2c.iterator().next().getCompany().getVatNr()).isEqualTo("1234567890");

        // update - different company

        Contact contactToUpdate = Contact.builder()
                .firstName("johnny")
                .lastName("dolle")
                .address("address2")
                .vatNr(null)
                .companies(Sets.newHashSet(Lists.newArrayList(Company.builder()
                        .vatNr("0123456789")
                        .build())))
                .build();

        mockMvc.perform(put("/contact/" + createdContacts.get(0).getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(contactToUpdate)))
                .andExpect(status().isOk());

        List<ContactEntity> updatedContacts = contactRepository.findAll();
        assertThat(updatedContacts).hasSize(1);
        assertThat(updatedContacts.get(0).getFirstName()).isEqualTo("johnny");

        List<CompanyEntity> updatedCompanies = companyRepository.findAll();
        assertThat(updatedCompanies).hasSize(2);
        assertThat(updatedCompanies).extracting("vatNr").contains("1234567890", "0123456789");

        List<CompanyToContact> updatedC2c = companyToContactRepository.findAll();
        assertThat(updatedC2c).hasSize(1);
        assertThat(updatedC2c.iterator().next().getCompany().getVatNr()).isEqualTo("0123456789");

        // delete

        mockMvc.perform(delete("/contact/" + createdContacts.get(0).getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        List<ContactEntity> deletedContacts = contactRepository.findAll();
        assertThat(deletedContacts).isEmpty();

        List<CompanyEntity> deletedCompanies = companyRepository.findAll();
        assertThat(deletedCompanies).hasSize(2);
        assertThat(deletedCompanies).extracting("vatNr").contains("1234567890", "0123456789");

        List<CompanyToContact> deletedC2c = companyToContactRepository.findAll();
        assertThat(deletedC2c).isEmpty();
    }
}
