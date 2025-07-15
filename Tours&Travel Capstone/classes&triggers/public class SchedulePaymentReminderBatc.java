public class SchedulePaymentReminderBatch implements Schedulable {

    public void execute(SchedulableContext sc) {

        PaymentReminderBatch batch = new PaymentReminderBatch();

        Database.executeBatch(batch, 200);

    }

}