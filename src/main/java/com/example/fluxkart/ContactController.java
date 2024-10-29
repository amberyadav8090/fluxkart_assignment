package com.example.fluxkart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/identify")
public class ContactController {


    private final  ContactService contactService ;


    @Autowired
    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping
    public ResponseEntity<?> identifyContact(@RequestBody ContactRequest contactRequest) {
        Contact contact = contactService.consolidateContact(contactRequest.getEmail(), contactRequest.getPhoneNumber());
        return ResponseEntity.ok().body(contact);
    }
}
