package com.example.api5.controller;

import com.example.api5.entity.Contact;
import com.example.api5.repository.ContactRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ContactController {
    @Autowired
    private ContactRepository contactRepository;

    @Operation
            (
                    tags = {"Получить контакты"},
                    description = "GET запрос на получение всех контактов",
                    summary = "Получение всех контактов",
                    responses = {@ApiResponse(responseCode = "200", description = "Список успешно получен", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Contact.class,name = "Contact"))),
                            @ApiResponse(responseCode = "204", description = "Контент не найден", content = @Content()),
                            @ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content())}
            )
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

    @Operation
            (
                    tags = {"Создать контакты"},
                    description = "POST запрос на получение создание контакта",
                    summary = "Создание контактов",
                    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Пример контакта",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schemaProperties = {
                                    @SchemaProperty(name = "number",schema = @Schema(example = "89853661411")),
                                            @SchemaProperty(name = "name",schema = @Schema(example = "Name")),
                                            @SchemaProperty(name = "surname",schema = @Schema(name = "Surname"))})),
                    responses = {@ApiResponse(responseCode = "201", description = "Контакт успешно создан", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Contact.class,name = "Contact",type = "array"))),
                            @ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content())}

            )
    @PostMapping("/contacts")
    public ResponseEntity<Contact> save(@RequestBody Contact Contact) {
        try {
            return new ResponseEntity<>(contactRepository.save(Contact), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Operation
            (
                    tags = {"Получить контакты"},
                    description = "GET запрос на получение контакта с заданным id",
                    summary = "Получение контакта по id",
                    responses = {@ApiResponse(responseCode = "200", description = "Контакт успешно получен", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Contact.class,name = "Contact"))),
                            @ApiResponse(responseCode = "204", description = "Контакта не существует", content = @Content()),
                            @ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content())}
            )
    @GetMapping("/contacts/{id}")
    public ResponseEntity<Contact> getContact(@Parameter(description = "id контакта,который мы ищем",required = true,in = ParameterIn.PATH) @PathVariable Optional<Long> id) {
        Contact contact = contactRepository.findContactById(id.get());
        if (contact == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(contact, HttpStatus.OK);
        }
    }
    @Operation
            (
                    tags = {"Изменить контакты"},
                    description = "Put запрос на изменение контакта",
                    summary = "Изменение контакта",
                    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Пример контакта",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,schema = @Schema(implementation = Contact.class))),
                    responses = {@ApiResponse(responseCode = "200", description = "Контакт успешно изменён", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Contact.class,name = "Contact",title = "Contact"))),
                            @ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content())}
            )
    @PutMapping("/contacts/{id}")
    public ResponseEntity<Contact> putContact(@RequestBody Contact contact) {
        try {
            return new ResponseEntity<>(contactRepository.save(contact), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Operation
            (
                    tags = {"Удалить контакты"},
                    description = "DELETE запрос на удаление всех контактов",
                    summary = "Удаление всех контактов",
                    responses = {@ApiResponse(responseCode = "204", description = "Все контакты удалены", content = @Content()),
                            @ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content())}
            )
    @DeleteMapping("/contacts")
    public ResponseEntity<HttpStatus> deleteContacts() {
        try {
            contactRepository.deleteAll();
            return new ResponseEntity<>((HttpStatus.NO_CONTENT));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Operation
            (
                    tags = {"Удалить контакты"},
                    description = "DELETE запрос на удаление контактов по id",
                    summary = "Удаление контактов по id",
                    responses = {@ApiResponse(responseCode = "204", description = "Контакт удалён", content = @Content()),
                            @ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content())}
            )
    @DeleteMapping("/contacts/{id}")
    public ResponseEntity<HttpStatus> deleteContact(@Parameter(description = "id контакта,который мы хотим удалить",required = true,in = ParameterIn.PATH) @PathVariable Long id) {
        try {
            Optional<Contact> contact = contactRepository.findById(id);
            contact.ifPresent(value -> contactRepository.delete(value));
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
