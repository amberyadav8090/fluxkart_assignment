package com.example.fluxkart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ContactService {

    private final ContactRepository contactRepository;

    @Autowired
    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public Contact consolidateContact(String email, String phoneNumber) {
        Contact primaryContact = null;
        List<Integer> secondaryContactIds = new ArrayList<>();

        if (email != null) {
            primaryContact = contactRepository.findByEmail(email).orElse(null);
        }

        if (phoneNumber != null) {
            Contact phoneContact = contactRepository.findByPhoneNumber(phoneNumber).orElse(null);
            if (phoneContact != null) {
                if (primaryContact == null) {
                    primaryContact = phoneContact;
                } else if (!primaryContact.getId().equals(phoneContact.getId())) {
                    secondaryContactIds.add(phoneContact.getId());
                }
            }
        }

        if (primaryContact == null) {
            primaryContact = new Contact();
            primaryContact.setEmail(email);
            primaryContact.setPhoneNumber(phoneNumber);
            primaryContact.setLinkPrecedence("primary");
            primaryContact.setCreatedAt(LocalDateTime.now());
            primaryContact.setUpdatedAt(LocalDateTime.now());
            contactRepository.save(primaryContact);
            secondaryContactIds = new ArrayList<>();
        } else {
            primaryContact.setUpdatedAt(LocalDateTime.now());
            contactRepository.save(primaryContact);

            if (email != null && !primaryContact.getEmail().equals(email)) {
                Contact secondaryContact = new Contact();
                secondaryContact.setEmail(email);
                secondaryContact.setPhoneNumber(phoneNumber);
                secondaryContact.setLinkedContact(primaryContact);
                secondaryContact.setLinkPrecedence("secondary");
                secondaryContact.setCreatedAt(LocalDateTime.now());
                secondaryContact.setUpdatedAt(LocalDateTime.now());
                contactRepository.save(secondaryContact);
                secondaryContactIds.add(secondaryContact.getId());
            }
        }

        return primaryContact;
    }

    public List<Integer> getSecondaryContactIds(Contact primaryContact) {
        List<Integer> secondaryIds = new ArrayList<>();
        return secondaryIds;
    }
}
