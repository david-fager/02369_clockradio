package dk.dtu.philipsclockradio;

public class StateSleep extends StateAdapter {

    private int[] sleepTimer = {120, 90, 60, 30, 15, 0};
    private int index = 0;

    StateSleep() {}

    @Override
    public void onEnterState(ContextClockradio context) {
        context.ui.setDisplayText(String.valueOf(sleepTimer[index]));
    }

    @Override
    public void onClick_Sleep(ContextClockradio context) {
        context.ui.setDisplayText(String.valueOf(sleepTimer[++index]));
    }
}
