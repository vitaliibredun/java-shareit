package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.model.constants.BookingStatus;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class BookingDto {
    private Integer id;
    private Integer itemId;
    private LocalDateTime start;
    private LocalDateTime end;
    private Integer booker;
    private BookingStatus status;
}
