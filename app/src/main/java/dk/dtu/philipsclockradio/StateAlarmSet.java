package dk.dtu.philipsclockradio;

import java.util.Date;

public class StateAlarmSet extends StateAdapter {

    private Date mTime;
    private String ALPressed = "";

    public StateAlarmSet(Date mTime) {
        this.mTime = mTime;
    }

    @Override
    public void onEnterState(ContextClockradio context) {
        context.ui.statusTextview.setText("Alarm setting mode");
        context.ui.turnOnTextBlink();
    }

    @Override
    public void onClick_Hour(ContextClockradio context) {
        mTime.setTime(mTime.getTime() + 3600000);
        context.ui.setDisplayText(mTime.toString().substring(11,16));
    }

    @Override
    public void onClick_Min(ContextClockradio context) {
        mTime.setTime(mTime.getTime() + 60000);
        context.ui.setDisplayText(mTime.toString().substring(11,16));
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
