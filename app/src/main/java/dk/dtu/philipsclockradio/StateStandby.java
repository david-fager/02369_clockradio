package dk.dtu.philipsclockradio;

import android.os.Handler;
import java.util.Date;

public class StateStandby extends StateAdapter {

    private Date mTime, alarmTime;
    private static Handler mHandler = new Handler();
    private ContextClockradio mContext;
    private int sleep;
    private int alarmState = 0; // 0 is muted, 1 is radio, 2 is buzzer

    StateStandby(Date time, Date alarmTime, int sleep){
        mTime = time;
        this.alarmTime = alarmTime;
        this.sleep = sleep;
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
                if (alarmTime != null) {
                    compareAlarmAndTime();
                }
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
        context.setState(new StateRadioFM(null));
    }

    @Override
    public void onClick_Sleep(ContextClockradio context) {
        stopClock();
        context.setState(new StateSleep());
    }

    private void compareAlarmAndTime() {

    }

    @Override
    public void onClick_AL1(ContextClockradio context) {

    }

    @Override
    public void onClick_AL2(ContextClockradio context) {

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
