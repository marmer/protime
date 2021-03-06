package io.github.marmer.protim.service.booking;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * Service related to Bookingdays.
 */
public interface BookingsCrudService {
    /**
     * Provides the {@link BookingDay} for a specific date.
     *
     * @param date Date of the booking day.
     * @return The related {@link BookingDay}
     */
    Optional<BookingDay> getBookingDay(LocalDate date);

    /**
     * Lists the startTimes of all bookings for a specific day.
     *
     * @param day The day of interest.
     * @return A list of start times.
     */
    List<LocalTime> getBookingStartTimesForDay(LocalDate day);

    /**
     * Serves a booking of a day by its start time.
     *
     * @param day       The related day.
     * @param startTime The start time of the booking
     * @return The booking if it exists.
     */
    Optional<Booking> getBookingAtDayForTime(LocalDate day, LocalTime startTime);

    /**
     * Adds or refreshes a booking at the given day.
     *
     * @param changeRequest Change request.
     */
    void setBookingAtDay(BookingChangeRequest changeRequest);

    /**
     * Deletes a booking which matches the given {@link BookingChangeRequest}.
     *
     * @param changeRequest Description what booking to delete.
     */
    void delete(BookingChangeRequest changeRequest);
}
