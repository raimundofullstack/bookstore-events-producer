package com.martins.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.martins.domain.BookStoreEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
@Component
@Slf4j
public class BookStoreEventProducer {

    @Autowired
    KafkaTemplate<Integer,String> kafkaTemplate;

    String topic = "bookstore-events";

    @Autowired
    ObjectMapper objectMapper;

    public CompletableFuture<SendResult<Integer, String>> sendLibraryEvent(BookStoreEvent bookStoreEvent) throws JsonProcessingException {
        Integer key = bookStoreEvent.getBookStoreEventId();
        String value = objectMapper.writeValueAsString(bookStoreEvent);

        CompletableFuture<SendResult<Integer, String>> future = kafkaTemplate.send(topic, key, value);

        // Callback moderno
        return future.whenComplete((result, ex) -> {
            if (ex != null) {
                handleFailure(key,value,ex);
            } else {
                handleSuccess(key,value,result);
            }
        });
    }
    private void handleSuccess(Integer key, String value, SendResult<Integer, String> result) {
        log.info("Mensagem enviada com sucesso para o tópico {} | key={} | partition={} | offset={}",
                topic, key, result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
    }

    private void handleFailure(Integer key, String value, Throwable ex) {
        log.error("Falha ao enviar mensagem para o tópico {} | key={} | erro={}", topic, key, ex.getMessage());
        try {
            throw ex;
        } catch (Throwable e) {
            log.error("Error in OnFailure: {}", e.getMessage());
        }
    }

    private ProducerRecord<Integer, String> buildProducerRecord(Integer key, String value, String topic) {

        // Manda um KafkaRecord com Headers usando o KafkaTemplate
        List<Header> recordHeaders = List.of(new RecordHeader("event-source", "scanner".getBytes()));

        return new ProducerRecord<>(topic, null, key, value, recordHeaders);
    }
    public SendResult<Integer, String>sendBookStoreEventSynchronous(BookStoreEvent bookStoreEvent) throws JsonProcessingException, ExecutionException, InterruptedException, TimeoutException {

        Integer key = bookStoreEvent.getBookStoreEventId();
        String value = objectMapper.writeValueAsString(bookStoreEvent);
        SendResult<Integer,String> sendResult = null;
        try {
            sendResult = kafkaTemplate.sendDefault(key,value).get(1, TimeUnit.SECONDS);
        } catch (ExecutionException | InterruptedException e) {
            log.error("ExecutionException/InterruptedException Mandando uma mensagem com a exception {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Mandando uma mensagem com a exception {}", e.getMessage());
            throw e;
        }

        return sendResult;

    }
}
