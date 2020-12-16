package be.genesis.rest;

import be.genesis.dto.Company;
import be.genesis.dto.CompanyAddress;
import be.genesis.dto.Contact;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.assertj.core.util.Lists;
import org.assertj.core.util.Sets;
import org.junit.jupiter.api.Test;

public class CurlSandbox extends MockMvcBase {

    //{"vatNr":"1234567890","contacts":[{"firstName":"john","lastName":"doe","address":"address1","vatNr":null,"companies":[]}],"addresses":[{"hq":true,"address":"companyAddress1"}]}
    //curl -X POST http://localhost:8080/company -H "content-type: application/json" --data '{"vatNr":"1234567890","contacts":[{"firstName":"john","lastName":"doe","address":"address1","vatNr":null,"companies":[]}],"addresses":[{"hq":true,"address":"companyAddress1"}]}'

    //{"vatNr":"0123456789","contacts":[{"firstName":"johnny","lastName":"dolle","address":"address2","vatNr":null,"companies":[]}],"addresses":[{"hq":true,"address":"companyAddress2"}]}
    //curl -X PUT http://localhost:8080/company/1 -H "content-type: application/json" --data '{"vatNr":"0123456789","contacts":[{"firstName":"johnny","lastName":"dolle","address":"address2","vatNr":null,"companies":[]}],"addresses":[{"hq":true,"address":"companyAddress2"}]}'

    //[{"hq":true,"address":"companyAddress3"}]
    //curl -X POST http://localhost:8080/company/1/addresses -H "content-type: application/json" --data '[{"hq":true,"address":"companyAddress3"}]'

    //[{"hq":false,"address":"companyAddress4"}]
    //curl -X POST http://localhost:8080/company/1/addresses/add -H "content-type: application/json" --data '[{"hq":true,"address":"companyAddress4"}]'

    //curl -X POST http://localhost:8080/company/1/addresses/set-hq "content-type: application/json" --data '[{"hq":true,"address":"companyAddress4"}]'

    //{"firstName":"john","lastName":"doe","address":"address1","vatNr":null,"companies":[{"vatNr":"1234567890","contacts":[],"addresses":[]}]}
    //curl -X POST http://localhost:8080/contact -H "content-type: application/json" --data '{"firstName":"john","lastName":"doe","address":"address1","vatNr":null,"companies":[{"vatNr":"1234567890","contacts":[],"addresses":[]}]}'

    //{"firstName":"johnny","lastName":"dolle","address":"address2","vatNr":null,"companies":[{"vatNr":"0123456789","contacts":[],"addresses":[]}]}
    //curl -X PUT http://localhost:8080/contact/10 -H "content-type: application/json" --data '{"firstName":"johnny","lastName":"dolle","address":"address2","vatNr":null,"companies":[{"vatNr":"0123456789","contacts":[],"addresses":[]}]}'

    //curl -X DELETE http://localhost:8080/contact/10

    @Test
    public void createJson() throws JsonProcessingException {
        System.err.println(objectMapper.writeValueAsString(Company.builder()
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
                .build()));

        System.err.println(objectMapper.writeValueAsString(Company.builder()
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
                .build()));

        System.err.println(objectMapper.writeValueAsString(Lists.newArrayList(CompanyAddress.builder()
                .hq(true)
                .address("companyAddress3")
                .build())));

        System.err.println(objectMapper.writeValueAsString(Lists.newArrayList(CompanyAddress.builder()
                .hq(false)
                .address("companyAddress4")
                .build())));

        System.err.println(objectMapper.writeValueAsString(Contact.builder()
                .firstName("john")
                .lastName("doe")
                .address("address1")
                .vatNr(null)
                .companies(com.google.common.collect.Sets.newHashSet(com.google.common.collect.Lists.newArrayList(Company.builder()
                        .vatNr("1234567890")
                        .build())))
                .build()));

        System.err.println(objectMapper.writeValueAsString(Contact.builder()
                .firstName("johnny")
                .lastName("dolle")
                .address("address2")
                .vatNr(null)
                .companies(com.google.common.collect.Sets.newHashSet(com.google.common.collect.Lists.newArrayList(Company.builder()
                        .vatNr("0123456789")
                        .build())))
                .build()));
    }
}
