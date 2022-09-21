package com.example.api5.repository;

import com.example.api5.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact,Long> {
    Contact findContactById(long id);
    void deleteContactById(long id);
}
