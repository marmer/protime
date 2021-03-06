package io.github.marmer.protim.test.endtoend;

import io.github.marmer.protim.persistence.relational.booking.BookingDBO;
import io.github.marmer.protim.persistence.relational.booking.BookingDayDBO;
import io.github.marmer.protim.test.DbCleanupService;
import io.github.marmer.protim.test.TransactionlessTestEntityManager;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;

import static io.github.marmer.protim.persistence.relational.booking.BookingDBOMatcher.isBookingDBO;
import static io.github.marmer.protim.persistence.relational.booking.BookingDayDBOMatcher.isBookingDayDBO;
import static io.github.marmer.protim.service.Converter.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles = "USER")
public class BookingEndToEndIT {
    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();
    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DbCleanupService dbCleanupService;
    @Autowired
    private TransactionlessTestEntityManager entityManager;

    @Before
    public void setUp() {
        dbCleanupService.clearAll();
    }

    @Test
    public void testGetDay_MultipleDaysExist_ShouldShowRequestedDay()
            throws Exception {
        // Preparation
        entityManager.persist(
                new BookingDayDBO()
                        .setDay(LocalDate.of(1985, Month.JANUARY, 2)));
        entityManager.persist(
                new BookingDayDBO()
                        .setDay(LocalDate.of(1985, Month.JANUARY, 3)));

        // Execution
        mockMvc.perform(get("/api/v1/day/1985-01-02"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.day", equalTo("1985-01-02")));
    }

    @Test
    public void testGetEntries_BookingsForMultipleDaysExist_ShouldListBookingsForRequestedDays()
            throws Exception {
        // Preparation
        final BookingDBO booking1 = new BookingDBO().setStartTime(LocalTime.of(18, 45));
        final BookingDBO booking2 = new BookingDBO().setStartTime(LocalTime.of(19, 30));
        final BookingDBO booking3 = new BookingDBO().setStartTime(LocalTime.of(13, 15));
        entityManager.persist(
                new BookingDayDBO()
                        .setDay(LocalDate.of(1985, Month.JANUARY, 2))
                        .setBookings(asList(booking1, booking2, booking3)));
        entityManager.persist(
                new BookingDayDBO()
                        .setDay(LocalDate.of(1985, Month.JANUARY, 3))
                        .setBookings(singletonList(new BookingDBO()
                                .setDescription("theOtherBooking"))));

        // Execution
        mockMvc.perform(get("/api/v1/day/1985-01-02/bookings"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.startTimes", contains(
                        "13:15",
                        "18:45",
                        "19:30")));
    }

    @Test
    public void testGetBooking_RequestForAnExistingBooking_ShouldServeTheRelatedBooking()
            throws Exception {
        // Preparation
        final BookingDBO booking1 = new BookingDBO()
                .setStartTime(LocalTime.of(18, 45))
                .setDescription("right");
        final BookingDBO booking2 = new BookingDBO()
                .setStartTime(LocalTime.of(19, 30))
                .setDescription("wrong1");
        final BookingDBO booking3 = new BookingDBO()
                .setStartTime(LocalTime.of(13, 15))
                .setDescription("wrong2");

        entityManager.persist(
                new BookingDayDBO()
                        .setDay(LocalDate.of(1985, Month.JANUARY, 2))
                        .setBookings(asList(booking1, booking2, booking3)));

        // Execution
        mockMvc.perform(get("/api/v1/day/1985-01-02/bookings/{startTime}", "18:45"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.description", is("right")));
    }

    @Test
    public void testPutBooking_EntryIsAdded_RelatedDayWithEntryShouldExist()
            throws Exception {
        // Preparation
        final LocalDate day = LocalDate.of(2014, 7, 13);
        final LocalTime startTime = LocalTime.of(16, 0);

        // Execution
        mockMvc.perform(
                put("/api/v1/day/{day}/bookings/", day, startTime)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content("{\n" +
                                "    \"startTime\": \"16:00\",\n" +
                                "    \"duration\": \"01:56\",\n" +
                                "    \"description\": \"watching football\",\n" +
                                "    \"notes\": \"it's not called soccer\",\n" +
                                "    \"ticket\": \"WORLDCUP-2014\"\n" +
                                "}"))
                .andExpect(status().isCreated());

        // Assertion
        assertThat(entityManager.findAllOf(BookingDayDBO.class), contains(isBookingDayDBO()
                .withDay(day)
                .withBookings(contains(
                        isBookingDBO()
                                .withStartTime(startTime)
                                .withDescription("watching football")
                ))));
    }

    @Test
    public void testPutBooking_EntryIsAddedForOverride_RelatedDayWithOnlyTheNewEntryShouldExist()
            throws Exception {
        // Preparation
        final LocalDate day = LocalDate.of(2014, 7, 13);
        final LocalTime startTime = LocalTime.of(16, 0);

        this.entityManager.persistAndFlush(
                new BookingDayDBO()
                        .setDay(day)
                        .setBookings(singletonList(new BookingDBO()
                                .setDescription("Doing the opposite of watching football")
                                .setStartTime(startTime))));

        // Execution
        mockMvc.perform(
                put("/api/v1/day/{day}/bookings/", day, startTime)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content("{\n" +
                                "    \"startTime\": \"16:00\",\n" +
                                "    \"duration\": \"01:56\",\n" +
                                "    \"description\": \"watching football\",\n" +
                                "    \"notes\": \"it's not called soccer\",\n" +
                                "    \"ticket\": \"WORLDCUP-2014\"\n" +
                                "}"))
                .andExpect(status().isCreated());

        // Assertion
        assertThat(entityManager.findAllOf(BookingDayDBO.class), contains(isBookingDayDBO()
                .withDay(day)
                .withBookings(contains(
                        isBookingDBO()
                                .withStartTime(startTime)
                                .withDescription("watching football")
                ))));
    }

    @Test
    public void testPutBookingAtTime_EntryIsAddedForOverride_RelatedDayWithOnlyTheNewEntryShouldExist()
            throws Exception {
        // Preparation
        final LocalDate day = LocalDate.of(2014, 7, 13);
        final LocalTime startTime = LocalTime.of(16, 0);

        this.entityManager.persistAndFlush(
                new BookingDayDBO()
                        .setDay(day)
                        .setBookings(singletonList(new BookingDBO()
                                .setDescription("Doing the opposite of watching football")
                                .setStartTime(startTime))));

        // Execution
        mockMvc.perform(
                put("/api/v1/day/{day}/bookings/{startTime}", day, startTime)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content("{\n" +
                                "    \"startTime\": \"16:00\",\n" +
                                "    \"duration\": \"01:56\",\n" +
                                "    \"description\": \"watching football\",\n" +
                                "    \"notes\": \"it's not called soccer\",\n" +
                                "    \"ticket\": \"WORLDCUP-2014\"\n" +
                                "}"))
                .andExpect(status().isCreated());

        // Assertion
        assertThat(entityManager.findAllOf(BookingDayDBO.class), contains(isBookingDayDBO()
                .withDay(day)
                .withBookings(contains(
                        isBookingDBO()
                                .withStartTime(startTime)
                                .withDescription("watching football")
                ))));
    }

    @Test
    public void testDeleteBooking_BookingDayWithMoreThanOneBookingIsGiven_ShouldOnlyRemoveTheRequestedBooking()
            throws Exception {

        // Preparation
        final LocalDate day = LocalDate.of(2014, 7, 13);
        final LocalTime startTime = LocalTime.of(16, 0);
        final LocalTime startTimeToRemove = LocalTime.of(15, 0);

        this.entityManager.persistAndFlush(
                new BookingDayDBO()
                        .setDay(day)
                        .setBookings(asList(
                                new BookingDBO()
                                        .setDescription("watching football")
                                        .setStartTime(startTime),
                                new BookingDBO()
                                        .setDescription("doing the opposite of watching football")
                                        .setStartTime(startTimeToRemove))));

        // Execution
        mockMvc.perform(
                delete("/api/v1/day/{day}/bookings/{startTimeToRemove}", day, startTimeToRemove))
                .andExpect(status().isNoContent());

        // Assertion
        assertThat(entityManager.findAllOf(BookingDayDBO.class), contains(isBookingDayDBO()
                .withDay(day)
                .withBookings(contains(
                        isBookingDBO()
                                .withStartTime(startTime)
                                .withDescription("watching football")))));
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    public void testTryingToGetAccess_CurrentUserIsAdmin_AccessShuoldBeDenied()
            throws Exception {
        // Preparation

        // Execution
        mockMvc.perform(get("/api/v1/day/"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testTryingToGetAccess_CurrentUserIsNotAdmin_AccessShuoldBeGranted()
            throws Exception {
        // Preparation

        // Execution
        mockMvc.perform(get("/api/v1/day/"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithAnonymousUser
    public void testTryingToGetAccess_CurrentUserIsAnonymous_AccessShuoldBeDeniedAsUnauthorized()
            throws Exception {
        // Preparation

        // Execution
        mockMvc.perform(get("/api/v1/day/"))
                .andExpect(status().isUnauthorized());
    }

}