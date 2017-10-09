package com.nxmup.androidclient.application;

import com.nxmup.androidclient.service.StateService;

public class AppCache {
    private static StateService mStateService;

    public static StateService getStateService() {
        return mStateService;
    }

    public static void setStateService(StateService StateService) {
        mStateService = StateService;
    }
}
