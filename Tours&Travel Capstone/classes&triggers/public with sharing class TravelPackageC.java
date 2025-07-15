public with sharing class TravelPackageController {

    @AuraEnabled(cacheable=true)
    public static List<Travel_Package__c> getPackagesByCountry(String country) {
        return [
            SELECT Id,
                   Name,
                   Package_Type__c,
                   Duration_in_Days__c,
                   Guide_Included__c,
                   Membership__c,
                   Region__c,
                   Transportation_Modes__c,
                   Availability_Status__c,
                   Average_Rating__c,
                   Places_Covered__c
            FROM Travel_Package__c
            WHERE Country__c = :country
        ];
    }
}