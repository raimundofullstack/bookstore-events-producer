package com.martins.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.martins.domain.Book;
import com.martins.domain.BookStoreEvent;
import com.martins.producer.BookStoreEventProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookStoreEventProducerUnitTest {

    @InjectMocks
    BookStoreEventProducer bookStoreEventProducer;

    @Mock
    KafkaTemplate<Integer, String> kafkaTemplate;

    @Spy
    ObjectMapper objectMapper;

    @Test
    void should_validate_book_store_event_with_send_library_failure() throws JsonProcessingException, ExecutionException, InterruptedException {
        Book book = Book.builder()
                .bookId(null)
                .bookName("Livro UnitTest")
                .bookAuthor(null)
                .build();

        BookStoreEvent bookStoreEvent = BookStoreEvent.builder()
                .bookStoreEventId(null)
                .book(book)
                .build();

        //when
        CompletableFuture<SendResult<Integer, String>> failedFuture = new CompletableFuture<>();

        failedFuture.completeExceptionally(new RuntimeException("Kafka send failed"));
       when(kafkaTemplate.send(isA(ProducerRecord.class))).thenReturn(failedFuture);

        //then
        assertThrows(Exception.class, () -> bookStoreEventProducer.sendLibraryEvent(bookStoreEvent).get());
    }

    @Test
    void should_validate_book_store_event_with_send_library_on_sucess() throws JsonProcessingException, ExecutionException, InterruptedException {
        Book book = Book.builder()
                .bookId(123)
                .bookName("Livro UnitTest")
                .bookAuthor("Barreto")
                .build();

        BookStoreEvent bookStoreEvent = BookStoreEvent.builder()
                .bookStoreEventId(123)
                .book(book)
                .build();
        String record = objectMapper.writeValueAsString(bookStoreEvent);

        //when
        CompletableFuture<SendResult<Integer, String>> future = new CompletableFuture<>();

        ProducerRecord<Integer, String> producerRecord = new ProducerRecord("bookstore-events", bookStoreEvent.getBookStoreEventId(), record);
        RecordMetadata recordMetadata = new RecordMetadata(new TopicPartition("bookstore-events", 1),
                1, 1, 342, System.currentTimeMillis(), 1,2);

        SendResult<Integer, String> sendResult = new SendResult<>(producerRecord, recordMetadata);

        future.complete(sendResult);

        when(kafkaTemplate.send(eq("bookstore-events"), anyInt(), anyString()))
                .thenReturn(future);

        CompletableFuture<SendResult<Integer, String>> listenableFuture = bookStoreEventProducer.sendLibraryEvent(bookStoreEvent);

        //then
        SendResult<Integer, String> sendResult1 = listenableFuture.get();

        assert sendResult1.getRecordMetadata().partition()==1;

    }
}
