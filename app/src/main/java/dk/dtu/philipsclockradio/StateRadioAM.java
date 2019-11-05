package dk.dtu.philipsclockradio;

import android.os.Handler;

public class StateRadioAM extends StateAdapter {

    private ContextClockradio mContext;
    private Handler handler = new Handler();
    private boolean waitASecond = false;
    private int  amFreq = 1000;
    private int[] amStationLower = {600, 921, 1234, 1691};
    private int[] amStationUpper = {650, 989, 1357, 1700};
    private String[] amStationName = {"DRP3", "R100", "MYRO", "R247"};
    private String currentStation = "";
    private String[] savedStations;

    public StateRadioAM(String[] savedStations) {
        if (savedStations != null) {
            this.savedStations = savedStations;
        } else {
            this.savedStations = new String[10];
        }
    }

    @Override
    public void onEnterState(ContextClockradio context) {
        mContext = context;
        mContext.ui.setDisplayText("AM");
        System.out.println("Current passband: AM");
        waitASecond = true;
        handler.postDelayed(displayDelay, 1000);

    }

    @Override
    public void onClick_Power(ContextClockradio context) {
        if (!waitASecond) {
            context.setState(new StateRadioFM(savedStations));
        }
    }

    // This shows the start frequency after three seconds delay of showing AM or FM
    Runnable displayDelay = new Runnable() {
        @Override
        public void run() {
            mContext.ui.setDisplayText(String.valueOf(amFreq));
            handler.postDelayed(checkStation, 500);
            waitASecond = false;
        }
    };

    // Lower frequency
    @Override
    public void onClick_Hour(ContextClockradio context) {
        if (!waitASecond) {
            if (--amFreq > 529) {
                mContext.ui.setDisplayText(String.valueOf(amFreq));
                System.out.println("Lowering frequency to: " + amFreq);
            }
            handler.postDelayed(checkStation, 500);
        }
    }

    // Higher frequency
    @Override
    public void onClick_Min(ContextClockradio context) {
        if (!waitASecond) {
            currentStation = "";
            if (++amFreq < 1701) {
                mContext.ui.setDisplayText(String.valueOf(amFreq));
                System.out.println("Increasing frequency to: " + amFreq);
            }
            handler.postDelayed(checkStation, 500);
        }
    }

    Runnable checkStation = new Runnable() {
        @Override
        public void run() {
            System.out.println("Looking for a station on frequency: " + amFreq);
            for (int i = 0; i < 4; i++) {
                if (amFreq >= amStationLower[i] && amFreq <= amStationUpper[i]) {
                    mContext.ui.setDisplayText(amStationName[i]);
                    currentStation = amStationName[i];
                    System.out.println("Found station: " + amStationName + " on frequency: " + amFreq);
                    break;
                } else {
                    mContext.ui.setDisplayText(String.valueOf(amFreq));
                }
            }
        }
    };

    @Override
    public void onLongClick_Preset(ContextClockradio context) {
        if (!currentStation.equals("")) {
            context.setState(new StateRadioSave(currentStation, false));
        } else {
            System.out.println("Held 'preset' but was not tuned into a station");
        }
    }

    @Override
    public void onLongClick_Power(ContextClockradio context) {
        System.out.println("Before exiting radio, here is a list of the final saved stations:");
        for (int i = 0; i < savedStations.length; i++) {
            System.out.println("[" + i + "] " + savedStations[i]);
        }
        mContext.setState(new StateStandby(mContext.getTime(), null, 0));
    }

}
