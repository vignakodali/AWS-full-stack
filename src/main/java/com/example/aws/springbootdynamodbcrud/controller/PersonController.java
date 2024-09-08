package com.example.aws.springbootdynamodbcrud.controller;

import com.example.aws.springbootdynamodbcrud.entity.Person;
import com.example.aws.springbootdynamodbcrud.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;
import com.example.aws.springbootdynamodbcrud.service.S3Service;

import java.util.List;

@Controller
public class PersonController {

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private S3Service s3Service;

    @PostMapping(value = "/submitPerson", consumes = "application/json")
    @ResponseBody
    public Person submitPersonJson(@RequestBody Person person) {
        List<Person> existingPersons = personRepository.findByPhotoUrl(person.getPhotoUrl());
        if (!existingPersons.isEmpty()) {
            return existingPersons.get(0);
        }

        personRepository.save(person);
        return person;
    }

    @PostMapping(value = "/submitPerson", consumes = { "multipart/form-data", "application/x-www-form-urlencoded" })
    public String submitPersonForm(@RequestParam("firstname") String firstname,
            @RequestParam("lastname") String lastname,
            @RequestParam("photo") MultipartFile photo,
            Model model) {
        String photoUrl = s3Service.uploadFile(photo, firstname, lastname);
        List<Person> existingPersons = personRepository.findByPhotoUrl(photoUrl);
        if (!existingPersons.isEmpty()) {
            model.addAttribute("message", "Person with this photo already exists!");
            return "result";
        }
        Person person = new Person();
        person.setFirstname(firstname);
        person.setLastname(lastname);
        person.setPhotoUrl(photoUrl);

        personRepository.save(person);

        model.addAttribute("message", "Person saved successfully!");
        return "result";
    }

    @GetMapping("/getPerson/{id}")
    @ResponseBody
    public Person getPerson(@PathVariable String id) {
        return personRepository.findById(id);
    }

    @GetMapping("/getAllPersons")
    @ResponseBody
    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    @PutMapping(value = "/updatePerson/{id}", consumes = "application/json")
    @ResponseBody
    public Person updatePerson(@PathVariable String id, @RequestBody Person person) {
        personRepository.update(id, person);
        return person;
    }

    @DeleteMapping("/deletePerson/{id}")
    @ResponseBody
    public String deletePerson(@PathVariable String id) {
        return personRepository.delete(id);
    }

    @GetMapping("/photos")
    @ResponseBody
    public List<Person> getAllPhotos() {
        return personRepository.findAll();
    }
}
