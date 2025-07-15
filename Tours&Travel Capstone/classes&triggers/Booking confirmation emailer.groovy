public class BookingConfirmationEmailer {

    @future(callout=false)

    public static void sendBookingConfirmation(Set<Id> bookingIds) {

        List<Messaging.SingleEmailMessage> emails = new List<Messaging.SingleEmailMessage>();

        List<Booking__c> bookings = [SELECT Id, Name, Customer_Email__c,  Total_Billing_Amount__c

                                     FROM Booking__c

                                     WHERE Id IN :bookingIds];

        for (Booking__c booking : bookings) {

            if (String.isNotBlank(booking.Customer_Email__c)) {

                Messaging.SingleEmailMessage mail = new Messaging.SingleEmailMessage();

                mail.setToAddresses(new String[] { booking.Customer_Email__c });

                mail.setSubject('Booking Confirmed: ' + booking.Name);

                mail.setPlainTextBody(

                    'Dear Customer,' + '\n\n' +

                    'Your booking has been confirmed. Please find the details below:\n' +

                    'Booking ID: ' + booking.Name + '\n' +

                    'Total Bill Amount Paid: $' + booking.Total_Billing_Amount__c + '\n\n' +

                    'Thank you for booking with us!'

                );

                emails.add(mail);

            }

        }

        if (!emails.isEmpty()) {

            Messaging.sendEmail(emails);

        }

    }

}