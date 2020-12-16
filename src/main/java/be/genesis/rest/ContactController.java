package be.genesis.rest;

import be.genesis.dto.Contact;
import be.genesis.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/contact")
public class ContactController {
    @Autowired
    private ContactService contactService;

    @PostMapping
    public ResponseEntity create(@Valid @RequestBody Contact contact) {
        Long id = contactService.create(contact);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable long id, @Valid @RequestBody Contact contact) {
        contactService.update(id, contact);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        contactService.delete(id);
    }
}
