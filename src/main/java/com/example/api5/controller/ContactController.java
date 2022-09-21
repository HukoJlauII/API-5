package com.example.api5.controller;

import com.example.api5.entity.Contact;
import com.example.api5.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ContactController {
    @Autowired
    private ContactRepository contactRepository;

    @GetMapping("/contacts")
    public ResponseEntity<List<Contact>> getContacts() {
        try {
            List<Contact> list = contactRepository.findAll(Sort.by("id"));
            if (list.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/contacts")
    public ResponseEntity<Contact> save(@RequestBody Contact Contact) {
        try {
            return new ResponseEntity<>(contactRepository.save(Contact), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/contacts/{id}")
    public Contact getContact(@PathVariable Optional<Long> id) {
        return id.map(aLong -> contactRepository.findContactById(aLong)).orElse(null);
    }

    @PutMapping("/contacts/{id}")
    public ResponseEntity<Contact> putContact(@RequestBody Contact contact) {
        try {
            return new ResponseEntity<>(contactRepository.save(contact), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/contacts")
    public ResponseEntity<HttpStatus> deleteContacts() {
        try {
            contactRepository.deleteAll();
            return new ResponseEntity<>((HttpStatus.NO_CONTENT));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/contacts/{id}")
    public ResponseEntity<HttpStatus> deleteContact(@PathVariable Long id) {
        try {
            Optional<Contact> contact = contactRepository.findById(id);
            contact.ifPresent(value -> contactRepository.delete(value));
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
