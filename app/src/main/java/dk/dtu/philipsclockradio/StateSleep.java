package dk.dtu.philipsclockradio;

import android.os.AsyncTask;
import android.os.SystemClock;

import java.util.Date;

public class StateSleep extends StateAdapter {

    private ContextClockradio mContext;
    private int[] sleepTimer = {120, 90, 60, 30, 15, 0};
    private int index = 0;
    private AsyncTask idle;
    private Date alarmTime;
    private String[] savedStations;
    private int alarmType = 0; // 0 is muted, 1 is radio, 2 is buzzer

    StateSleep(Date alarmTime, String[] savedStations, int alarmType) {
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
        mContext = context;
        mContext.ui.setDisplayText(String.valueOf(sleepTimer[index]));
        mContext.ui.turnOnLED(3);
        checkIfIdle();
    }

    // Switches between the possible sleep lengths.
    @Override
    public void onClick_Sleep(ContextClockradio context) {
        if (idle != null) {
            idle.cancel(true);
        }
        if (++index > sleepTimer.length - 1) {
            index = 0;
        }
        if (sleepTimer[index] == 0) {
            mContext.ui.setDisplayText("OFF");
            mContext.ui.turnOffLED(3);
        } else {
            mContext.ui.setDisplayText(String.valueOf(sleepTimer[index]));
            mContext.ui.turnOnLED(3);
        }
        checkIfIdle();
    }

    // Checking for user being idle, every second it checks if it is cancelled to relieve the system
    public void checkIfIdle() {
        idle = new AsyncTask() {
            @Override
            protected String doInBackground(Object[] objects) {
                for (int i = 0; i < 5; i++) {
                    SystemClock.sleep(1000);
                    System.out.println("User inactive for: " + (i+1) + " seconds.");
                    if(isCancelled()) {
                        System.out.println("User was active. Idle AsyncTask cancelled");
                        return null;
                    }
                }
                return "isIdle";
            }

            @Override
            protected void onPostExecute(Object o) {
                System.out.println("5 seconds idle, closing sleep state");
                mContext.setState(new StateStandby(mContext.getTime(), alarmTime, savedStations, alarmType));
            }
        }.execute();
    }

}
