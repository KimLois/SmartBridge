public class BookingTriggerHandler {

    /* -------- 1.  Booking Payment Record Creation -------- */
    public static void createPaymentRecords(List<Booking__c> newBookings) {

        List<Booking_Payment__c> paymentsToInsert = new List<Booking_Payment__c>();

        for (Booking__c booking : newBookings) {
            Booking_Payment__c payment = new Booking_Payment__c();
            payment.Booking__c        = booking.Id;
            payment.Payment_Status__c = 'Pending';   // default
            payment.Payment_Date__c   = Date.today(); 
            paymentsToInsert.add(payment);
        }
        if (!paymentsToInsert.isEmpty()) insert paymentsToInsert;
    }


    /* -------- 2.  Booking Guests Record Creation -------- */
   public static void createBookingGuests(List<Booking__c> bookings) {
    List<Booking_Guest__c> guestsToInsert = new List<Booking_Guest__c>();

    // Step 1: Collect all Customer__c IDs
    Set<Id> customerIds = new Set<Id>();
    for (Booking__c booking : bookings) {
        if (booking.Customer__c != null) {
            customerIds.add(booking.Customer__c);
        }
    }

    // Step 2: Query the Customer Info records
    Map<Id, Customer_Info__c> customerMap = new Map<Id, Customer_Info__c>(
        [SELECT Id, Name, Age__c, Country__c FROM Customer_Info__c WHERE Id IN :customerIds]
    );

    // Step 3: Generate guests
    for (Booking__c booking : bookings) {
        Integer count = (Integer) booking.Number_of_Travelers__c;

        for (Integer i = 1; i <= count; i++) {
            Booking_Guest__c guest = new Booking_Guest__c();
            guest.Booking__c = booking.Id;
            guest.Name       = 'Guest ' + i;

            // Use customer data as default
            Customer_Info__c cust = customerMap.get(booking.Customer__c);
            if (cust != null) {
                guest.Age__c     = cust.Age__c;
                guest.Country__c = cust.Country__c;
            }

            guestsToInsert.add(guest);
        }
    }

    if (!guestsToInsert.isEmpty()) insert guestsToInsert;
}
}