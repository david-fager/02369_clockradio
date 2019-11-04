package dk.dtu.philipsclockradio;

public class StateAlarmStates extends StateAdapter {

    public StateAlarmStates() {}

    @Override
    public void onEnterState(ContextClockradio context) {
        context.ui.statusTextview.setText("Alarm states state");
    }



}
