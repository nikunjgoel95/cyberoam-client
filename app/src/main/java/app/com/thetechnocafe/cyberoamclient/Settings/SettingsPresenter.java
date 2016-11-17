package app.com.thetechnocafe.cyberoamclient.Settings;

import app.com.thetechnocafe.cyberoamclient.Utils.SharedPreferenceUtils;

/**
 * Created by gurleensethi on 14/11/16.
 */

public class SettingsPresenter implements ISettingsPresenter {
    private ISettingsView mView;

    public SettingsPresenter(ISettingsView view) {
        mView = view;
    }

    @Override
    public void onViewReady() {
        mView.setUpView();

        updateSettingsInView();
    }

    @Override
    public void changeNotificationsState(boolean state) {
        //Change state in shared preferences
        SharedPreferenceUtils.setNotifications(mView.getContext(), state);

        //Update settings
        updateSettingsInView();
    }

    @Override
    public void changeActivityLogState(boolean state) {
        //Change state in shared preferences
        SharedPreferenceUtils.setActivityLog(mView.getContext(), state);

        //Update settings
        updateSettingsInView();
    }

    //Provide View with updated settings
    private void updateSettingsInView() {
        mView.setUpSettingsState(
                SharedPreferenceUtils.getBaseIPAddress(mView.getContext()),
                SharedPreferenceUtils.getBasePort(mView.getContext()),
                SharedPreferenceUtils.getNotifications(mView.getContext()),
                SharedPreferenceUtils.getActivityLog(mView.getContext())
        );
    }
}
