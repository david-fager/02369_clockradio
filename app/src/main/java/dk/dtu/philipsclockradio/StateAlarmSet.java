package dk.dtu.philipsclockradio;

import java.util.Calendar;
import java.util.Date;

public class StateAlarmSet extends StateAdapter {

    private Calendar calendar = Calendar.getInstance();
    private Date alarmTime;
    private String ALPressed = "";

    public StateAlarmSet() {}

    @Override
    public void onEnterState(ContextClockradio context) {
        context.ui.statusTextview.setText("Alarm setting mode");
        calendar.setTime(context.getTime());
        alarmTime = calendar.getTime();
        System.out.println("Alarm time: " + alarmTime.getTime());
        System.out.println("Actual time: " + context.getTime());
        context.ui.turnOnTextBlink();
    }

    @Override
    public void onClick_Hour(ContextClockradio context) {
        alarmTime.setTime(alarmTime.getTime() + 3600000);
        System.out.println("Alarm time: " + alarmTime.getTime()); // TODO: Mangler fiks
        System.out.println("Actual time: " + context.getTime());
    }

    @Override
    public void onClick_Min(ContextClockradio context) {
        alarmTime.setTime(alarmTime.getTime() + 60000);
        System.out.println("Alarm time: " + alarmTime.getTime());
        System.out.println("Actual time: " + context.getTime());
    }

    @Override
    public void onClick_AL1(ContextClockradio context) {
        if(ALPressed.equals("AL2")) {
            context.setState(new StateStandby(context.getTime(), null, 0));
        } else {
            ALPressed = "AL1";
            context.ui.statusTextview.setText("Awaiting press of AL1");
            System.out.println("User is 'pressing' AL1, waiting for AL2");
        }
    }

    @Override
    public void onClick_AL2(ContextClockradio context) {
        if(ALPressed.equals("AL1")) {
            context.setState(new StateStandby(context.getTime(), null, 0));
        } else {
            ALPressed = "AL2";
            context.ui.statusTextview.setText("Awaiting press of AL1");
            System.out.println("User is 'pressing' AL2, waiting for AL1");
        }
    }

    @Override
    public void onExitState(ContextClockradio context) {
        context.ui.turnOffTextBlink();
    }
}
