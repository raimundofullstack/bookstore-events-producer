package com.martins.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.martins.domain.BookStoreEvent;
import com.martins.domain.BookStoreEventType;
import com.martins.producer.BookStoreEventProducer;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
public class BookStoreEventController {

    @Autowired
    BookStoreEventProducer bookStoreEventProducer;

    @PostMapping("/v1/bookstore-events")
    public ResponseEntity<BookStoreEvent> postBookStoreEvent(@RequestBody @Valid BookStoreEvent bookStoreEvent) throws JsonProcessingException {
        //Adicionando um novo livro
        bookStoreEvent.setBookStoreEventType(BookStoreEventType.NEW);
        //dispara o evento
        bookStoreEventProducer.sendLibraryEvent(bookStoreEvent);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookStoreEvent);
    }

    @PutMapping("/v1/bookstore-events")
    public ResponseEntity<?> putBookStoreEvent(@RequestBody @Valid BookStoreEvent bookStoreEvent) throws JsonProcessingException, ExecutionException, InterruptedException {

        if(bookStoreEvent.getBookStoreEventId() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Campo bookStoreEventId é obrigatório");
        }
        bookStoreEvent.setBookStoreEventType(BookStoreEventType.UPDATE);
        bookStoreEventProducer.sendLibraryEvent(bookStoreEvent);
        return ResponseEntity.status(HttpStatus.OK).body((bookStoreEvent));
    }
}
