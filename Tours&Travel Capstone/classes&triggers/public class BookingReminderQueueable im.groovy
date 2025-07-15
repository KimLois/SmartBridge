public class BookingReminderQueueable implements Queueable {

    List<Booking__c> bookingsToRemind;

    public BookingReminderQueueable(List<Booking__c> bookings) {

        this.bookingsToRemind = bookings;

    }

    public void execute(QueueableContext context) {

        List<Messaging.SingleEmailMessage> emails = new List<Messaging.SingleEmailMessage>();

        for (Booking__c booking : bookingsToRemind) {

            if (String.isNotBlank(booking.Customer_Email__c)) {

                Messaging.SingleEmailMessage mail = new Messaging.SingleEmailMessage();

                mail.setToAddresses(new String[] { booking.Customer_Email__c });

                mail.setSubject('Reminder: Your Tour Starts Soon!');

                mail.setPlainTextBody(

                    'Hello,\n\nThis is a friendly reminder that your tour is starting on ' +

                    booking.Travelling_Start_Date__c.format() + 

                    '. Please make necessary arrangements.\n\nThank you for choosing us!'

                );

                emails.add(mail);

            }

        }

        if (!emails.isEmpty()) {

            Messaging.sendEmail(emails);

        }

    }

}