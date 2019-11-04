package dk.dtu.philipsclockradio;

import android.os.Handler;

public class StateRadio extends StateAdapter {

    private ContextClockradio mContext;
    private Handler handler = new Handler();
    private boolean fm = true; // set false for am frequency
    private boolean waitASecond = false;
    private boolean saveMode = false;
    private int fmFreq = 100, amFreq = 1000;
    private int[] fmStationLower = {91, 99, 103, 107};
    private int[] fmStationUpper = {93, 100, 105, 108};
    private String[] fmStationName = {"DRP4", "NOVA", "VOIC", "ROCK"};
    private int[] amStationLower = {600, 921, 1234, 1691};
    private int[] amStationUpper = {650, 989, 1357, 1700};
    private String[] amStationName = {"DRP3", "R100", "MYRO", "R247"};
    private String currentStation = "";
    private int saveIndex = 0;
    private String[] savedStations = new String[9];

    public StateRadio() {}

    @Override
    public void onEnterState(ContextClockradio context) {
        mContext = context;
        mContext.ui.setDisplayText("FM");
        System.out.println("Current passband: FM");
        waitASecond = true;
        handler.postDelayed(displayDelay, 1000);
    }

    @Override
    public void onClick_Power(ContextClockradio context) {
        if (!waitASecond && !saveMode) {
            waitASecond = true;
            if (fm) {
                fm = false;
                mContext.ui.setDisplayText("AM");
                System.out.println("Changed to passband: AM");
            } else {
                fm = true;
                mContext.ui.setDisplayText("FM");
                System.out.println("Changed to passband: FM");
            }
            handler.postDelayed(displayDelay, 1000);
        }
    }

    // This shows the start frequency after three seconds delay of showing AM or FM
    Runnable displayDelay = new Runnable() {
        @Override
        public void run() {
            if (fm) {
                mContext.ui.setDisplayText(String.valueOf(fmFreq));
            } else {
                mContext.ui.setDisplayText(String.valueOf(amFreq));
            }
            waitASecond = false;
        }
    };

    // Lower frequency
    @Override
    public void onClick_Hour(ContextClockradio context) {
        if (!waitASecond && !saveMode) {
            currentStation = "";
            if (fm) {
                if (--fmFreq > 87) {
                    mContext.ui.setDisplayText(String.valueOf(fmFreq));
                    System.out.println("Lowering frequency to: " + fmFreq);
                }
            } else {
                if (--amFreq > 529) {
                    mContext.ui.setDisplayText(String.valueOf(amFreq));
                    System.out.println("Lowering frequency to: " + fmFreq);
                }
            }
            handler.postDelayed(checkStation, 500);
        }
    }

    // Higher frequency
    @Override
    public void onClick_Min(ContextClockradio context) {
        if (!waitASecond && !saveMode) {
            currentStation = "";
            if (fm) {
                if (++fmFreq < 109) {
                    mContext.ui.setDisplayText(String.valueOf(fmFreq));
                    System.out.println("Increasing frequency to: " + fmFreq);
                }
            } else {
                if (++amFreq < 1701) {
                    mContext.ui.setDisplayText(String.valueOf(amFreq));
                    System.out.println("Increasing frequency to: " + fmFreq);
                }
            }
            handler.postDelayed(checkStation, 500);
        }
    }

    Runnable checkStation = new Runnable() {
        @Override
        public void run() {
            if (fm) {
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
            } else {
                System.out.println("Looking for a station on frequency: " + amFreq);
                for (int i = 0; i < 4; i++) {
                    if (amFreq >= amStationLower[i] && amFreq <= amStationUpper[i]) {
                        mContext.ui.setDisplayText(amStationName[i]);
                        currentStation = amStationName[i];
                        System.out.println("Found station: " + amStationName + " on frequency: " + amFreq);
                        break;
                    } else {
                        mContext.ui.setDisplayText(String.valueOf(fmFreq));
                    }
                }
            }
        }
    };

    // Save station at current number
    @Override
    public void onClick_Preset(ContextClockradio context) {
        if (saveMode) {
            savedStations[saveIndex] = currentStation;
            mContext.ui.turnOffTextBlink();
            mContext.ui.setDisplayText(currentStation);
            System.out.println("Saved station " + currentStation + " to array index " + saveIndex + ". Printing saves:");
            for (int i = 0; i < savedStations.length; i++){
                System.out.println("[" + i + "] " + savedStations[i]);
            }
            saveMode = false;
        }
    }

    // Enter number choosing for save station
    @Override
    public void onLongClick_Preset(ContextClockradio context) {
        if (saveMode) {
            if (++saveIndex > 9) {
                saveIndex = 0;
            }
            mContext.ui.setDisplayText(String.valueOf(saveIndex + 1));
            System.out.println("Changed array index to " + saveIndex);
        } else {
            if (!currentStation.equals("")) {
                saveMode = true;
                mContext.ui.setDisplayText(String.valueOf(saveIndex + 1));
                mContext.ui.turnOnTextBlink();
                System.out.println("Changed to save mode. Saving station " + currentStation + ". Current index: " + saveIndex);
            } else {
                System.out.println("Held 'preset' but was not tuned into a station");
            }
        }
    }

    @Override
    public void onLongClick_Power(ContextClockradio context) {
        mContext.setState(new StateStandby(mContext.getTime()));
    }

}
