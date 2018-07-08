package io.github.marmer.protim.persistence.relational.booking;

import io.github.marmer.protim.service.Converter;
import io.github.marmer.protim.service.model.Booking;
import org.springframework.stereotype.Service;

@Service
public class BookingDBOConverter implements Converter<Booking, BookingDBO> {
    @Override
    public BookingDBO convert(final Booking source) {
        return source == null ? null : new BookingDBO()
                .setStartTime(source.getStartTime())
                .setDuration(source.getDuration())
                .setDescription(source.getDescription())
                .setNotes(source.getNotes())
                .setTicket(source.getTicket());
    }
}