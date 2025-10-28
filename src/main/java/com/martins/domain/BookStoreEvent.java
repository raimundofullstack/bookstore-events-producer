package com.martins.domain;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BookStoreEvent {
//    @NotNull
    private Integer bookStoreEventId;

//    @NotBlank
    private BookStoreEventType bookStoreEventType;

    @NotNull
    @Valid
    private Book book;
}
