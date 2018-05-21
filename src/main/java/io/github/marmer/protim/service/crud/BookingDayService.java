package io.github.marmer.protim.service.crud;

import io.github.marmer.protim.service.model.BookingDay;

import java.util.Calendar;
import java.util.Optional;

/**
 * Service related to Bookingdays.
 */
public interface BookingDayService {
    /**
     * Provides the {@link BookingDay} for a specific date.
     *
     * @param date Date of the booking day.
     * @return The related {@link BookingDay}
     */
    Optional<BookingDay> getBookingDay(Calendar date);
}
