package dk.dtu.philipsclockradio;

import android.os.Handler;
import java.util.Date;

public class StateStandby extends StateAdapter {

    private Date mTime, alarmTime;
    private static Handler mHandler = new Handler();
    private ContextClockradio mContext;
    private String ALHeld = "";
    private int sleep;

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
        context.ui.statusTextview.setText("Standby state");
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
        context.setState(new StateRadioFM());
    }

    @Override
    public void onClick_Sleep(ContextClockradio context) {
        context.setState(new StateSleep());
    }

    @Override
    public void onLongClick_AL1(ContextClockradio context) {
        if(ALHeld.equals("AL2")) {
            context.setState(new StateAlarmSet());
        } else {
            ALHeld = "AL1";
            context.ui.statusTextview.setText("Awaiting hold of AL2");
            System.out.println("User is 'holding' AL1, waiting for AL2");
        }
    }

    @Override
    public void onLongClick_AL2(ContextClockradio context) {
        if(ALHeld.equals("AL1")) {
            context.setState(new StateAlarmSet());
        } else {
            ALHeld = "AL2";
            context.ui.statusTextview.setText("Awaiting hold of AL1");
            System.out.println("User is 'holding' AL2, waiting for AL1");
        }
    }

}
