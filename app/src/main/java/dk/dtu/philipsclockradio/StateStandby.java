package dk.dtu.philipsclockradio;

import android.os.Handler;
import java.util.Date;

public class StateStandby extends StateAdapter {

    private Date mTime, alarmTime;
    private static Handler mHandler = new Handler();
    private ContextClockradio mContext;
    private int alarmType = 0; // 0 is muted, 1 is radio, 2 is buzzer

    StateStandby(Date time, Date alarmTime){
        mTime = time;
        if (alarmTime != null) {
            this.alarmTime = alarmTime;
            System.out.println("StateStandby ready for alarm at: " + alarmTime);
        }
    }

    //Opdaterer hvert 60. sekund med + 1 min til tiden
    Runnable mSetTime = new Runnable() {

        @Override
        public void run() {
            try {
                long currentTime = mTime.getTime();
                mTime.setTime(currentTime + 60000);
                mContext.setTime(mTime);
                System.out.println("Time changed to: " + mTime);
                compareAlarmAndTime();
            } finally {
                mHandler.postDelayed(mSetTime, 60000);
            }
        }
    };

    void startClock() {
        mSetTime.run();
        mContext.isClockRunning = true;
    }

    void stopClock() {
        mHandler.removeCallbacks(mSetTime);
        mContext.isClockRunning = false;
    }

    @Override
    public void onEnterState(ContextClockradio context) {
        //Lokal context oprettet for at Runnable kan få adgang
        mContext = context;

        context.updateDisplayTime();
        if(!context.isClockRunning){
            startClock();
        }
    }

    @Override
    public void onLongClick_Preset(ContextClockradio context) {
        stopClock();
        context.setState(new StateSetTime());
    }

    @Override
    public void onClick_Power(ContextClockradio context) {
        stopClock();
        context.setState(new StateRadioFM(null, true));
    }

    @Override
    public void onClick_Sleep(ContextClockradio context) {
        stopClock();
        context.setState(new StateSleep());
    }

    private void compareAlarmAndTime() {
        if (alarmTime != null && alarmType != 0) {
            if (mTime.compareTo(alarmTime) == 0) {
                stopClock();
                mContext.setState(new StateAlarmRinging(alarmType));
            }
        }
    }

    @Override
    public void onClick_AL1(ContextClockradio context) {
        if (alarmTime != null) {
            if (++alarmType > 2) {
                alarmType = 0;
            }
            if (alarmType == 0) {
                context.ui.turnOffLED(1);
                context.ui.turnOffLED(2);
                System.out.println("Alarm is muted");
            } else if (alarmType == 1) {
                context.ui.turnOnLED(1);
                context.ui.turnOffLED(2);
                System.out.println("Alarm is on radio");
            } else if (alarmType == 2) {
                context.ui.turnOnLED(2);
                context.ui.turnOffLED(1);
                System.out.println("Alarm is on buzzer");
            }
        } else {
            System.out.println("Alarm is not set");
        }
    }

    @Override
    public void onClick_AL2(ContextClockradio context) {
        if (alarmTime != null) {
            if (++alarmType > 2) {
                alarmType = 0;
            }
            if (alarmType == 0) {
                context.ui.turnOffLED(1);
                context.ui.turnOffLED(2);
                System.out.println("Alarm is muted");
            } else if (alarmType == 1) {
                context.ui.turnOnLED(1);
                context.ui.turnOffLED(2);
                System.out.println("Alarm is on radio");
            } else if (alarmType == 2) {
                context.ui.turnOnLED(2);
                context.ui.turnOffLED(1);
                System.out.println("Alarm is on buzzer");
            }
        } else {
            System.out.println("Alarm is not set");
        }
    }

    @Override
    public void onLongClick_AL1(ContextClockradio context) {
        stopClock();
        context.setState(new StateAlarmSet());
    }

    @Override
    public void onLongClick_AL2(ContextClockradio context) {
        stopClock();
        context.setState(new StateAlarmSet());
    }

}
