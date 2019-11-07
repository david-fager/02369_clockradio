package dk.dtu.philipsclockradio;

import java.util.Calendar;
import java.util.Date;


public class StateAlarmSet extends StateAdapter {

    private Calendar calendar = Calendar.getInstance();
    private Date alarmTime;
    private String[] savedStations;
    private int alarmType = 0; // 0 is muted, 1 is radio, 2 is buzzer

    public StateAlarmSet(String[] savedStations, int alarmType) {
        if (savedStations != null) {
            this.savedStations = savedStations;
        }
        this.alarmType = alarmType;
    }

    @Override
    public void onEnterState(ContextClockradio context) {
        calendar.setTime(context.getTime());
        alarmTime = calendar.getTime();
        System.out.println("Alarm time: " + alarmTime);
        System.out.println("Actual time: " + context.getTime());
        context.ui.turnOnTextBlink();
    }

    @Override
    public void onClick_Hour(ContextClockradio context) {
        alarmTime.setTime(alarmTime.getTime() + 3600000);
        System.out.println("Alarm time: " + alarmTime);
        System.out.println("Actual time: " + context.getTime());
        context.ui.setDisplayText(alarmTime.toString().substring(11,16));
    }

    @Override
    public void onClick_Min(ContextClockradio context) {
        alarmTime.setTime(alarmTime.getTime() + 60000);
        System.out.println("Alarm time: " + alarmTime);
        System.out.println("Actual time: " + context.getTime());
        context.ui.setDisplayText(alarmTime.toString().substring(11,16));
    }

    @Override
    public void onClick_AL1(ContextClockradio context) {
        context.setState(new StateStandby(context.getTime(), alarmTime, savedStations, alarmType));
    }

    @Override
    public void onClick_AL2(ContextClockradio context) {
        context.setState(new StateStandby(context.getTime(), alarmTime, savedStations, alarmType));
    }

    @Override
    public void onExitState(ContextClockradio context) {
        context.ui.turnOffTextBlink();
    }

}
