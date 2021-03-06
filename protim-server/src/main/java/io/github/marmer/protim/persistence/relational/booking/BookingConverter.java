package io.github.marmer.protim.persistence.relational.booking;

import io.github.marmer.protim.service.Converter;
import io.github.marmer.protim.service.booking.Booking;
import org.springframework.stereotype.Service;

@Service("converterBookingDBOToBooking")
public class BookingConverter implements Converter<BookingDBO, Booking> {
    @Override
    public Booking convert(final BookingDBO source) {
        return source == null ? null : Booking.builder()
                .startTime(source.getStartTime())
                .duration(source.getDuration())
                .description(source.getDescription())
                .notes(source.getNotes())
                .ticket(source.getTicket())
                .build();
    }
}
