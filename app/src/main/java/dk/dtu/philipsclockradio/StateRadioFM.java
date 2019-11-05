package dk.dtu.philipsclockradio;

import android.os.Handler;

public class StateRadioFM extends StateAdapter {

    private ContextClockradio mContext;
    private Handler handler = new Handler();
    private boolean waitASecond = false;
    private int fmFreq = 100;
    private int[] fmStationLower = {91, 99, 103, 107};
    private int[] fmStationUpper = {93, 100, 105, 108};
    private String[] fmStationName = {"DRP4", "NOVA", "VOIC", "ROCK"};
    private String currentStation = "";
    private String[] savedStations;
    private boolean setStatus;

    public StateRadioFM(String[] savedStations, boolean setStatus) {
        if (savedStations != null) {
            this.savedStations = savedStations;
        } else {
            this.savedStations = new String[10];
        }
        this.setStatus = setStatus;
    }

    @Override
    public void onEnterState(ContextClockradio context) {
        mContext = context;
        mContext.ui.setDisplayText("FM");
        System.out.println("Current passband: FM");
        waitASecond = true;
        handler.postDelayed(displayDelay, 1000);
        if (setStatus) {
            context.ui.toggleRadioPlaying();
        }
    }

    @Override
    public void onClick_Power(ContextClockradio context) {
        if (!waitASecond) {
            context.setState(new StateRadioAM(savedStations, false));
        }
    }

    // This shows the start frequency after three seconds delay of showing AM or FM
    Runnable displayDelay = new Runnable() {
        @Override
        public void run() {
            mContext.ui.setDisplayText(String.valueOf(fmFreq));
            handler.postDelayed(checkStation, 500);
            waitASecond = false;
        }
    };

    // Lower frequency
    @Override
    public void onClick_Hour(ContextClockradio context) {
        if (!waitASecond) {
            currentStation = "";
            if (--fmFreq > 87) {
                mContext.ui.setDisplayText(String.valueOf(fmFreq));
                System.out.println("Lowering frequency to: " + fmFreq);
            }
            handler.postDelayed(checkStation, 500);
        }
    }

    // Higher frequency
    @Override
    public void onClick_Min(ContextClockradio context) {
        if (!waitASecond) {
            currentStation = "";
            if (++fmFreq < 109) {
                mContext.ui.setDisplayText(String.valueOf(fmFreq));
                System.out.println("Increasing frequency to: " + fmFreq);
            }
            handler.postDelayed(checkStation, 500);
        }
    }

    Runnable checkStation = new Runnable() {
        @Override
        public void run() {
            System.out.println("Looking for a station on frequency: " + fmFreq);
            for (int i = 0; i < 4; i++) {
                if (fmFreq >= fmStationLower[i] && fmFreq <= fmStationUpper[i]) {
                    mContext.ui.setDisplayText(fmStationName[i]);
                    currentStation = fmStationName[i];
                    System.out.println("Found station: " + fmStationName + " on frequency: " + fmFreq);
                    break;
                } else {
                    mContext.ui.setDisplayText(String.valueOf(fmFreq));
                }
            }
        }
    };

    @Override
    public void onLongClick_Preset(ContextClockradio context) {
        if (!currentStation.equals("")) {
            context.setState(new StateRadioSave(currentStation, true));
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
        context.ui.toggleRadioPlaying();
        mContext.setState(new StateStandby(mContext.getTime(), null));
    }

}
