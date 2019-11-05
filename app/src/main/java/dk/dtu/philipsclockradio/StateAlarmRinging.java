package dk.dtu.philipsclockradio;

import java.util.Date;

public class StateAlarmRinging extends StateAdapter {

    private Date snoozedTime = new Date();
    private int alarmType;

    public StateAlarmRinging(int alarmType) {
        this.alarmType = alarmType;
    }

    @Override
    public void onEnterState(ContextClockradio context) {
        if (alarmType == 1) {
            context.ui.toggleRadioPlaying();
            System.out.println("Alarm went off, playing the 'radio'");
            context.ui.setDisplayText("NOVA");
        } else if (alarmType == 2) {
            System.out.println("Alarm went off, playing buzzer");
            context.ui.statusTextview.setText("Alarm buzzing");
        } else {
            System.out.println("Impossible. Enjoy the ride back to StateStandby...");
            context.setState(new StateStandby(context.getTime(), null));
        }
    }

    @Override
    public void onClick_AL1(ContextClockradio context) {
        context.ui.toggleRadioPlaying();
        context.ui.turnOffLED(1);
        context.ui.turnOffLED(2);
        context.setState(new StateStandby(context.getTime(), null));
    }

    @Override
    public void onClick_AL2(ContextClockradio context) {
        context.ui.toggleRadioPlaying();
        context.ui.turnOffLED(1);
        context.ui.turnOffLED(2);
        context.setState(new StateStandby(context.getTime(), null));
    }

    @Override
    public void onClick_Snooze(ContextClockradio context) {
        if (alarmType == 1) {
            context.ui.toggleRadioPlaying();
        }
        long currentTime = context.getTime().getTime();
        snoozedTime.setTime(currentTime + 60000 * 2);
        System.out.println("Snoozing until renewed alarm: " + snoozedTime);
        context.setState(new StateStandby(context.getTime(), snoozedTime));
    }

}
