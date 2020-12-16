package be.genesis.service;

import be.genesis.dto.Company;
import be.genesis.dto.CompanyAddress;
import be.genesis.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class CompanyService {
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    private CompanyToContactService companyToContactService;

    public long create(Company company) {
        CompanyEntity companyEntity = CompanyEntity.from(company);
        companyRepository.save(companyEntity);

        Set<ContactEntity> contactEntities = getOrCreateContactEntities(company);
        contactEntities.forEach(contactEntity -> companyToContactService.create(companyEntity, contactEntity));

        return companyEntity.getId();
    }

    private CompanyEntity getCompany(Long id) {
        Optional<CompanyEntity> companyOpt = companyRepository.findById(id);
        if (!companyOpt.isPresent())
            throw new NoSuchElementException();
        return companyOpt.get();
    }

    public void update(Long id, Company company) {
        CompanyEntity companyEntity = getCompany(id);
        companyEntity.setVatNr(company.getVatNr());
        companyEntity.setAddresses(mapCompanyAddresses(company.getAddresses()));
        companyRepository.save(companyEntity);

        Set<ContactEntity> contactEntities = getOrCreateContactEntities(company);
        contactEntities.forEach(contactEntity -> companyToContactService.create(companyEntity, contactEntity));
        companyToContactService.deleteCompanyOrphans(companyEntity.getId(), contactEntities);
    }

    public void updateAddresses(Long id, Set<CompanyAddress> addresses) {
        CompanyEntity entity = getCompany(id);
        entity.setAddresses(mapCompanyAddresses(addresses));
        companyRepository.save(entity);
    }

    public void addAddresses(Long id, Set<CompanyAddress> addresses) {
        CompanyEntity entity = getCompany(id);
        entity.addAddresses(mapCompanyAddresses(addresses));
        companyRepository.save(entity);
    }

    public void setHq(Long id, String hqAddress) {
        CompanyEntity entity = getCompany(id);

        entity.getAddresses().forEach(address -> address.setHq(address.getAddress().equalsIgnoreCase(hqAddress)));

        if (!entity.getAddresses().stream().filter(CompanyAddressEntity::isHq).findFirst().isPresent())
            entity.addAddress(CompanyAddressEntity.builder()
                    .hq(true)
                    .address(hqAddress)
                    .build());

        companyRepository.save(entity);
    }

    private Set<CompanyAddressEntity> mapCompanyAddresses(Set<CompanyAddress> addresses) {
        return addresses.stream().map(CompanyAddressEntity::from).collect(Collectors.toSet());
    }

    private Set<ContactEntity> getOrCreateContactEntities(Company company) {
        return company.getContacts().stream().map(contact ->
        {
            ContactEntity contactEntity;
            Optional<ContactEntity> contactOpt = contactRepository.findByFirstNameAndLastNameAndAddress(
                    contact.getFirstName(), contact.getLastName(), contact.getAddress());
            if (contactOpt.isPresent())
                contactEntity = contactOpt.get();
            else {
                contactEntity = ContactEntity.from(contact);
                contactRepository.save(contactEntity);
            }
            return contactEntity;
        }).collect(Collectors.toSet());
    }
}
