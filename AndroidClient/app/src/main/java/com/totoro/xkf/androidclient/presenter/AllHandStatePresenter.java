package com.totoro.xkf.androidclient.presenter;

import android.widget.ArrayAdapter;

import com.totoro.xkf.androidclient.base.BasePresenter;
import com.totoro.xkf.androidclient.util.StateSelector;
import com.totoro.xkf.androidclient.view.AllHandStateActivity;

public class AllHandStatePresenter extends BasePresenter<AllHandStateActivity> {

    public AllHandStatePresenter(AllHandStateActivity mView) {
        super(mView);
    }

    public ArrayAdapter<String> createAdapter() {
        String[] handStates = new String[StateSelector.handState.length];
        for (int i = 0; i < handStates.length; i++) {
            handStates[i] = StateSelector.handState[i] + "     " + StateSelector.handDetail[i];
        }
        return new ArrayAdapter<>(mView, android.R.layout.simple_list_item_1, handStates);
    }
}
