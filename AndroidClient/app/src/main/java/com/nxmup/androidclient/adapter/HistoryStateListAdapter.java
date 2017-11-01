package com.nxmup.androidclient.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nxmup.androidclient.R;
import com.nxmup.androidclient.util.StateSelector;

import java.util.List;

public class HistoryStateListAdapter extends RecyclerView.Adapter<HistoryStateListAdapter.ViewHolder> {
    private List<String> history;

    public HistoryStateListAdapter(List<String> history) {
        super();
        this.history = history;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_state_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (history.isEmpty()) {
            return;
        }
        String historyInfo = history.get(position);
        String state = historyInfo.split("@")[0];
        String info = StateSelector.getState(state);
        String time = historyInfo.split("@")[1];
        holder.tvHistoryHandState.setText(state);
        holder.tvHistoryInfo.setText(info);
        holder.tvHistoryTime.setText(time);
    }

    @Override
    public int getItemCount() {
        return history.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvHistoryHandState;
        private TextView tvHistoryInfo;
        private TextView tvHistoryTime;

        public ViewHolder(View itemView) {
            super(itemView);
            tvHistoryHandState = itemView.findViewById(R.id.tv_history_hand_state);
            tvHistoryInfo = itemView.findViewById(R.id.tv_history_info);
            tvHistoryTime = itemView.findViewById(R.id.tv_history_time);
        }
    }
}
