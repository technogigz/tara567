package com.myTara567.app;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.notice_view> {
        Context mContext;
        ArrayList<Notice_Model> notice_list;

public NoticeAdapter(Context context, ArrayList<Notice_Model> notice_list) {
        this.mContext = context;
        this.notice_list = notice_list;
        }

@NonNull
@Override
public notice_view onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notice_card, parent, false);
        return new notice_view(view);
        }

@Override
public void onBindViewHolder(@NonNull notice_view holder, int position) {
        Log.d("TAG", "onBindViewHolder: " + notice_list.get(position).getNotice_title());
        if (notice_list.get(position).getNotice_title().equals("")) {
        holder.noticeTitle.setVisibility(View.GONE);
        } else  {
        holder.noticeTitle.setVisibility(View.VISIBLE);
        }
        holder.noticeTitle.setText(notice_list.get(position).getNotice_title());
        holder.noticeMsg.setText(notice_list.get(position).getNotice_msg());
        holder.noticeDate.setText(notice_list.get(position).getNotice_date());
        }

@Override
public int getItemCount() {
        return notice_list.size();
        }

public class notice_view extends RecyclerView.ViewHolder {
    TextView noticeTitle, noticeMsg, noticeDate;

    public notice_view(@NonNull View itemView) {
        super(itemView);
        noticeTitle = itemView.findViewById(R.id.noticeTitle);
        noticeMsg = itemView.findViewById(R.id.noticeMsg);
        noticeDate = itemView.findViewById(R.id.noticeDate);
    }
}
}
