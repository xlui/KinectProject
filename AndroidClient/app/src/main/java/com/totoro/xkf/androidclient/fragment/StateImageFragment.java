package com.totoro.xkf.androidclient.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.totoro.xkf.androidclient.R;
import com.totoro.xkf.androidclient.presenter.StatePresenter;

public class StateImageFragment extends Fragment {
    private StatePresenter mPresenter;
    public SwipeRefreshLayout stateRefresh;
    private ImageView stateImage;
    private TextView stateDate;

    public void setPresenter(StatePresenter presenter) {
        mPresenter = presenter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.state_image_fragment, container, false);
        stateRefresh = view.findViewById(R.id.state_refresh);
        stateImage = view.findViewById(R.id.state_image);
        stateDate = view.findViewById(R.id.state_date);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        stateRefresh.setOnRefreshListener(mPresenter);
        mPresenter.onRefresh();
    }

    public void updateViews(String date, String imageUrl) {
        Glide.with(StateImageFragment.this).load(imageUrl).into(stateImage);
        stateDate.setText("最后上传于:  " + date);
        stateRefresh.setRefreshing(false);
    }
}
