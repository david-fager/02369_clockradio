package dk.dtu.philipsclockradio;

import java.util.Date;


public class StateSetTime extends StateAdapter {
    Date mTime;
    private Date alarmTime;
    private String[] savedStations;
    private int alarmType = 0; // 0 is muted, 1 is radio, 2 is buzzer

    StateSetTime(Date alarmTime, String[] savedStations, int alarmType){
        if (alarmTime != null) {
            this.alarmTime = alarmTime;
        }
        if (savedStations != null) {
            this.savedStations = savedStations;
        }
        this.alarmType = alarmType;
    }

    @Override
    public void onEnterState(ContextClockradio context) {
        mTime = context.getTime();
        context.ui.turnOnTextBlink();
        context.updateDisplayTime();
    }

    @Override
    public void onExitState(ContextClockradio context) {
        context.ui.turnOffTextBlink();
    }

    @Override
    public void onClick_Hour(ContextClockradio context) {
        //Gets current timestamp (Date)
        mTime.setTime(mTime.getTime() + 3600000);
        context.setTime(mTime);
        context.updateDisplayTime();
    }

    @Override
    public void onClick_Min(ContextClockradio context) {
        //Gets current timestamp (Date)
        mTime.setTime(mTime.getTime() + 60000);
        context.setTime(mTime);
        context.updateDisplayTime();
    }

    @Override
    public void onClick_Preset(ContextClockradio context) {
        context.setState(new StateStandby(context.getTime(), alarmTime, savedStations, alarmType));
    }

}
