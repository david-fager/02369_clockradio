package dk.dtu.philipsclockradio;

public class StateRadioSave extends StateAdapter {

    private ContextClockradio mContext;
    private int saveIndex = 0;
    private String currentStation;
    private String[] savedStations = new String[10];
    private boolean fm;

    public StateRadioSave(String currentStation, boolean fm) {
        this.currentStation = currentStation;
        this.fm = fm;
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
            context.setState(new StateRadioFM(savedStations));
        } else {
            context.setState(new StateRadioAM(savedStations));
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
