package dk.dtu.philipsclockradio;

import java.util.Date;

public class StateRadioSave extends StateAdapter {

    private ContextClockradio mContext;
    private int saveIndex = 0;
    private String currentStation;
    private String[] savedStations = new String[10];
    private boolean fm;
    private Date alarmTime;
    private int alarmType = 0; // 0 is muted, 1 is radio, 2 is buzzer

    public StateRadioSave(String currentStation, boolean fm, Date alarmTime, String[] savedStations, int alarmType) {
        this.currentStation = currentStation;
        this.fm = fm;
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
        mContext.ui.setDisplayText(String.valueOf(saveIndex + 1));
        mContext.ui.turnOnTextBlink();
        System.out.println("Changed to save mode. Saving station " + currentStation);
    }

    // Save station at current number
    @Override
    public void onClick_Preset(ContextClockradio context) {
        savedStations[saveIndex] = currentStation;
        mContext.ui.turnOffTextBlink();
        System.out.println("Saved station " + currentStation + " to array index " + saveIndex + ". Printing saves:");
        for (int i = 0; i < savedStations.length; i++){
            System.out.println("[" + i + "] " + savedStations[i]);
        }
        if (fm) {
            context.setState(new StateRadioFM(savedStations, false, alarmTime, alarmType));
        } else {
            context.setState(new StateRadioAM(savedStations, false, alarmTime, alarmType));
        }
    }

    // Enter number choosing for save station
    @Override
    public void onLongClick_Preset(ContextClockradio context) {
        if (++saveIndex > 9) {
            saveIndex = 0;
        }
        mContext.ui.setDisplayText(String.valueOf(saveIndex + 1));
        System.out.println("Changed array index to " + saveIndex);
    }

}
