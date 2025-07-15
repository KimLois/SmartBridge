@isTest
private class BookingTriggerTest {

    @isTest
    static void testTriggerCreatesPaymentAndGuestsWithUpdatedFields() {
        // Create test customer
        Customer_Info__c customer = new Customer_Info__c(
            Name = 'John Doe',
            Email__c = 'annapurna@gmail.com', // your valid email address
            Phone__c = '1234567890',
            Date_of_Birth__c = Date.today().addYears(-25), // age must be > 0
            Country__c = 'India'
        );
        insert customer;

        // Create a Travel Package
        Travel_Package__c packageRec = new Travel_Package__c(
            Name = 'European Delight',
            Country__c = 'India',
            Price_Per_Person__c = 2000,
            Duration_in_Days__c = 3,
            Places_Covered__c = 'Paris, Rome, Berlin'
        );
        insert packageRec;

        // Create a test Booking record
        Booking__c booking = new Booking__c(
            Number_of_Travelers__c = 3,
            Booking_Status__c = 'Pending',
            Travelling_Start_Date__c = Date.today().addDays(10),
            Booking_Date__c = Date.today(),
            TravelPackage__c = packageRec.Id,
            Membership__c = 'Gold',
            Preferred_Accommodation__c = 'Guest House',
            Customer__c = customer.Id,
            Customer_Email__c = 'annapurna@gmail.com'
        );

        Test.startTest();
        insert booking; // Trigger fires here
        Test.stopTest();

        // Validate Booking Payment creation
        List<Booking_Payment__c> payments = [
            SELECT Id, Booking__c, Payment_Status__c
            FROM Booking_Payment__c
            WHERE Booking__c = :booking.Id
        ];
        System.assertEquals(1, payments.size(), 'One payment record should be created.');
        System.assertEquals('Pending', payments[0].Payment_Status__c, 'Default Payment Status should be Pending.');

        // Validate Booking Guest creation
        List<Booking_Guest__c> guests = [
            SELECT Id, Booking__c, Name
            FROM Booking_Guest__c
            WHERE Booking__c = :booking.Id
        ];
        System.assertEquals(3, guests.size(), 'Three BookingGuest records should be created.');
        System.assertEquals('Guest 1', guests[0].Name, 'Guest naming should follow convention.');
    }
}