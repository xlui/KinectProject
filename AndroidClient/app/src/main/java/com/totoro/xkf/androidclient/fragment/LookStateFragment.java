package com.totoro.xkf.androidclient.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.totoro.xkf.androidclient.R;
import com.totoro.xkf.androidclient.presenter.StatePresenter;

public class LookStateFragment extends Fragment {
    private StatePresenter mPresenter;
    private ImageView stateImage;
    private TextView stateDetail;
    private TextView stateInfo;
    private TextView stateDanger;
    private TextView stateDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.state_fragment, container, false);
        stateImage = view.findViewById(R.id.state_image);
        stateInfo = view.findViewById(R.id.state_info);
        stateDanger = view.findViewById(R.id.state_danger);
        stateDate = view.findViewById(R.id.state_date);
        stateDetail = view.findViewById(R.id.state_detail);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mPresenter.startUpdate();
    }

    public void setPresenter(StatePresenter presenter) {
        mPresenter = presenter;
    }

    public void updateViews(String imageUrl, String detail, String info, String danger, String date) {
        Glide.with(LookStateFragment.this).load(imageUrl).into(stateImage);
        stateDetail.setText(detail);
        stateInfo.setText("手势表示含义:  " + info);
        stateDanger.setText("当前状态:  " + danger);
        stateDate.setText("最后跟新于:  " + date);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.stopUpdate();
    }
}
