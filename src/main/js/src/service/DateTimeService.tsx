/**
 * Service handle dates easily.
 *
 * @author marmer
 * @since  11.04.2018
 */
export class DateTimeService {
    public static dayAfter(date: Date): Date {
        let newDate: Date = new Date(date);
        newDate.setDate(date.getDate() + 1);
        return newDate;
    }

    public static dayBefore(date: Date): Date {
        let newDate = new Date(date);
        newDate.setDate(date.getDate() - 1);

        return newDate;
    }
}