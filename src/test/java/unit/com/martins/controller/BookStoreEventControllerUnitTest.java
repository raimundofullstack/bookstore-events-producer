package com.martins.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.martins.domain.Book;
import com.martins.domain.BookStoreEvent;
import com.martins.domain.BookStoreEventType;
import com.martins.producer.BookStoreEventProducer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookStoreEventController.class)
@AutoConfigureMockMvc
public class BookStoreEventControllerUnitTest {

    @Autowired
    MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    BookStoreEventProducer bookStoreEventProducer;

    @Test
    void should_post_book_store_event() throws Exception{
        //given
        Book book = Book.builder()
                .bookId(123)
                .bookName("Livro UnitTest")
                .bookAuthor("M Barreto")
                .build();

        BookStoreEvent bookStoreEvent = BookStoreEvent.builder()
                .bookStoreEventId(null)
                .book(book)
                .build();

        String json = objectMapper.writeValueAsString(bookStoreEvent);
        //when

        doNothing().when(bookStoreEventProducer).sendLibraryEvent(isA(BookStoreEvent.class));

        //then
        mockMvc.perform(post("/v1/bookstore-events")
                .content(json)
                .contentType(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(status().isCreated());

    }
    @Test
    void should_not_post_book_store_event_when_status_code_was_400() throws Exception{
        //given
        Book book = Book.builder()
                .bookId(null)
                .bookName("Livro UnitTest")
                .bookAuthor(null)
                .build();

        BookStoreEvent bookStoreEvent = BookStoreEvent.builder()
                .bookStoreEventId(null)
                .book(book)
                .build();

        String json = objectMapper.writeValueAsString(bookStoreEvent);

        doNothing().when(bookStoreEventProducer).sendLibraryEvent(isA(BookStoreEvent.class));

        String expectedErrorMessage = "book.bookAuthor - must not be blank , book.bookId - must not be null";
        mockMvc.perform(post("/v1/bookstore-events")
                        .content(json)
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(status().is4xxClientError())
                .andExpect((content().string(expectedErrorMessage)));

    }
}
