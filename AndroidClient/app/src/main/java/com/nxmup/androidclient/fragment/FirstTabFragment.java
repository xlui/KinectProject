package com.nxmup.androidclient.fragment;

import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nxmup.androidclient.R;
import com.nxmup.androidclient.application.AppCache;
import com.nxmup.androidclient.listener.OnStateChangeListener;
import com.nxmup.androidclient.util.LogUtil;

public class FirstTabFragment extends Fragment implements OnStateChangeListener {
    private ImageView ivImageState;
    private TextView tvState;
    private String lastState = "state";

    public FirstTabFragment() {
        super();
        AppCache.getStateService().updateStateConstantly();
        AppCache.getStateService().setOnStateChangeListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_1, container, false);
        ivImageState = view.findViewById(R.id.iv_image_state);
        tvState = view.findViewById(R.id.tv_state);
        tvState.setText(lastState);
        return view;
    }

    @Override
    public void onStateChange(final String newState) {
        lastState = newState;
        ivImageState.post(new Runnable() {
            @Override
            public void run() {
                tvState.setText(newState);
            }
        });
    }
}
