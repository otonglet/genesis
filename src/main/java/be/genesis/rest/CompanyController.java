package be.genesis.rest;

import be.genesis.dto.Company;
import be.genesis.dto.CompanyAddress;
import be.genesis.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping("/company")
public class CompanyController {
    @Autowired
    private CompanyService companyService;

    @PostMapping
    public ResponseEntity create(@Valid @RequestBody Company company) {
        Long id = companyService.create(company);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable long id, @Valid @RequestBody Company company) {
        companyService.update(id, company);
    }

    @PostMapping("/{id}/addresses")
    public void updateAddresses(@PathVariable long id, @Valid @RequestBody Set<CompanyAddress> addresses) {
        companyService.updateAddresses(id, addresses);
    }

    @PostMapping("/{id}/addresses/add")
    public void update(@PathVariable long id, @Valid @RequestBody Set<CompanyAddress> addresses) {
        companyService.addAddresses(id, addresses);
    }

    @PostMapping("/{id}/addresses/set-hq")
    public void setHq(@PathVariable long id, @RequestBody String address) {
        companyService.setHq(id, address);
    }
}
