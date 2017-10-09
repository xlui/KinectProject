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
import com.nxmup.androidclient.listener.OnStateChangeListener;

public class FirstTabFragment extends Fragment implements OnStateChangeListener {
    private ImageView ivImageState;
    private TextView tvState;
    private Button btnOldManState;
    private Button btnEscortService;
    private Button btnMe;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_1, container, false);
        ivImageState = view.findViewById(R.id.iv_image_state);
        tvState = view.findViewById(R.id.tv_state);
        btnOldManState = view.findViewById(R.id.btn_old_man_state);
        btnMe = view.findViewById(R.id.btn_me);
        btnEscortService = view.findViewById(R.id.btn_escort_service);
        return view;
    }



    @Override
    public void onStateChange() {

    }
}
