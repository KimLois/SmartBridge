public class BookingReminderScheduler implements Schedulable {

    public void execute(SchedulableContext context) {

        Date reminderDate = Date.today().addDays(3);

        List<Booking__c> upcomingBookings = [

            SELECT Id, Travelling_Start_Date__c, Customer_Email__c

            FROM Booking__c

            WHERE Travelling_Start_Date__c = :reminderDate

            AND Booking_Status__c = 'Confirmed'

        ];

        if (!upcomingBookings.isEmpty()) {

            System.enqueueJob(new BookingReminderQueueable(upcomingBookings));

        }

    }

}