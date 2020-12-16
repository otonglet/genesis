package be.genesis.rest;

import be.genesis.dto.Company;
import be.genesis.dto.CompanyAddress;
import be.genesis.dto.Contact;
import be.genesis.persistence.CompanyAddressEntity;
import be.genesis.persistence.CompanyEntity;
import be.genesis.persistence.CompanyToContact;
import be.genesis.persistence.ContactEntity;
import org.assertj.core.util.Lists;
import org.assertj.core.util.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CompanyControllerEndToEndTest extends MockMvcBase {

    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Test
    public void bigFatTestForLazyGuy() throws Exception {
        // create

        Company companyToCreate = Company.builder()
                .vatNr("1234567890")
                .contacts(Sets.newHashSet(Lists.newArrayList(Contact.builder()
                        .firstName("john")
                        .lastName("doe")
                        .address("address1")
                        .vatNr(null)
                        .build())))
                .addresses(Sets.newHashSet(Lists.newArrayList(CompanyAddress.builder()
                        .hq(true)
                        .address("companyAddress1")
                        .build())))
                .build();

        mockMvc.perform(post("/company")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(companyToCreate)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isNumber());

        List<CompanyEntity> createdCompanies = companyRepository.findAll();
        assertThat(createdCompanies).hasSize(1);
        assertThat(createdCompanies.get(0).getVatNr()).isEqualTo("1234567890");
        assertThat(createdCompanies.get(0).getAddresses()).hasSize(1);
        assertThat(createdCompanies.get(0).getAddresses().iterator().next().getAddress()).isEqualTo("companyAddress1");

        List<ContactEntity> createdContacts = contactRepository.findAll();
        assertThat(createdContacts).hasSize(1);
        assertThat(createdContacts.get(0).getFirstName()).isEqualTo("john");

        List<CompanyToContact> createdC2c = companyToContactRepository.findAll();
        assertThat(createdC2c).hasSize(1);
        assertThat(createdC2c.iterator().next().getContact().getFirstName()).isEqualTo("john");

        // update - different contact address

        Company companyToUpdate = Company.builder()
                .vatNr("0123456789")
                .contacts(Sets.newHashSet(Lists.newArrayList(Contact.builder()
                        .firstName("johnny")
                        .lastName("dolle")
                        .address("address2")
                        .vatNr(null)
                        .build())))
                .addresses(Sets.newHashSet(Lists.newArrayList(CompanyAddress.builder()
                        .hq(true)
                        .address("companyAddress2")
                        .build())))
                .build();

        mockMvc.perform(put("/company/" + createdCompanies.get(0).getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(companyToUpdate)))
                .andExpect(status().isOk());

        List<CompanyEntity> updatedCompanies = companyRepository.findAll();
        assertThat(updatedCompanies).hasSize(1);
        assertThat(updatedCompanies.get(0).getVatNr()).isEqualTo("0123456789");
        assertThat(updatedCompanies.get(0).getAddresses()).hasSize(1);
        assertThat(updatedCompanies.get(0).getAddresses().iterator().next().getAddress()).isEqualTo("companyAddress2");

        List<ContactEntity> updatedContacts = contactRepository.findAll();
        assertThat(updatedContacts).hasSize(1);
        assertThat(updatedContacts.get(0).getFirstName()).isEqualTo("johnny");

        List<CompanyToContact> updatedC2c = companyToContactRepository.findAll();
        assertThat(updatedC2c).hasSize(1);
        assertThat(updatedC2c.iterator().next().getContact().getFirstName()).isEqualTo("johnny");

        // change addresses

        List<CompanyAddress> changedAddresses = Lists.newArrayList(CompanyAddress.builder()
                .hq(true)
                .address("companyAddress3")
                .build());

        mockMvc.perform(post("/company/" + createdCompanies.get(0).getId() + "/addresses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(changedAddresses)))
                .andExpect(status().isOk());

        List<CompanyEntity> companiesWithChangedAddresses = companyRepository.findAll();
        assertThat(companiesWithChangedAddresses).hasSize(1);
        assertThat(companiesWithChangedAddresses.get(0).getAddresses().iterator().next().getAddress()).isEqualTo("companyAddress3");

        // add addresses

        List<CompanyAddress> addedAddresses = Lists.newArrayList(CompanyAddress.builder()
                .hq(false)
                .address("companyAddress4")
                .build());

        mockMvc.perform(post("/company/" + createdCompanies.get(0).getId() + "/addresses/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addedAddresses)))
                .andExpect(status().isOk());

        List<CompanyEntity> companiesWithAddedAddresses = companyRepository.findAll();
        assertThat(companiesWithAddedAddresses).hasSize(1);
        assertThat(companiesWithAddedAddresses.get(0).getAddresses()).extracting("address").containsExactly("companyAddress3", "companyAddress4");

        // change HQ

        mockMvc.perform(post("/company/" + createdCompanies.get(0).getId() + "/addresses/set-hq")
                .contentType(MediaType.APPLICATION_JSON)
                .content("companyAddress4"))
                .andExpect(status().isOk());

        List<CompanyEntity> companiesWithChangedHq = companyRepository.findAll();
        assertThat(companiesWithChangedHq).hasSize(1);
        List<CompanyAddressEntity> addresses = Lists.newArrayList(companiesWithChangedHq.get(0).getAddresses());
        assertThat(addresses).hasSize(2);
        checkHqChanged(addresses.get(0));
        checkHqChanged(addresses.get(1));
    }

    private void checkHqChanged(CompanyAddressEntity address) {
        assertThat(address.getAddress().equals("companyAddress3") || address.isHq() == true).isTrue();
        assertThat(address.getAddress().equals("companyAddress4") || address.isHq() == false).isTrue();
    }
}
