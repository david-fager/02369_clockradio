package dk.dtu.philipsclockradio;

import android.os.Handler;

import java.util.Calendar;
import java.util.Date;


public class StateAlarmSet extends StateAdapter {

    private ContextClockradio mContext;
    private Handler mHandler = new Handler();
    private Calendar calendar = Calendar.getInstance();
    private Date alarmTime, mTime;
    private String ALPressed = "";

    public StateAlarmSet() {}

    @Override
    public void onEnterState(ContextClockradio context) {
        context.ui.statusTextview.setText("Alarm setting mode");
        calendar.setTime(context.getTime());
        alarmTime = calendar.getTime();
        System.out.println("Alarm time: " + alarmTime);
        System.out.println("Actual time: " + context.getTime());
        context.ui.turnOnTextBlink();

        mContext = context;
        mTime = context.getTime();
        context.updateDisplayTime();
        if(!context.isClockRunning){
            System.out.println("STARTED CLOCK IN ALARM STATE");
            startClock();
        }
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
        if(ALPressed.equals("AL2")) {
            stopClock();
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
            stopClock();
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

    // This part from the StateStandby is necessary for the clock to
    // run properly when the states changes. Otherwise time runs too fast.
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

}
