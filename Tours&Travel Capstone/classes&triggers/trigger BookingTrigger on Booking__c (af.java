trigger BookingTrigger on Booking__c (after insert, after update) {

    if (Trigger.isAfter && Trigger.isInsert) {

        // creates Booking Payment records

        BookingTriggerHandler.createPaymentRecords(Trigger.new);

        

        // creates BookingGuests records

        BookingTriggerHandler.createBookingGuests(Trigger.new);  

    }

    else  if (Trigger.isAfter && Trigger.isUpdate) {  

        Set<Id> bookingIdsToSend = new Set<Id>();

    for (Booking__c booking : Trigger.new) {

        Booking__c oldBooking = Trigger.oldMap.get(booking.Id);

        // Check for status change to "Confirmed"

        if (booking.Booking_Status__c == 'Confirmed' && oldBooking.Booking_Status__c != 'Confirmed') {

            bookingIdsToSend.add(booking.Id);

        }

    }

     if (!bookingIdsToSend.isEmpty()) {

        // Call the Future Method

        BookingConfirmationEmailer.sendBookingConfirmation(bookingIdsToSend);

    }

    }

}