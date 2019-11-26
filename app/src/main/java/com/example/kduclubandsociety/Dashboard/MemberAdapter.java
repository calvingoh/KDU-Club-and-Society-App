package com.example.kduclubandsociety.Dashboard;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.kduclubandsociety.Class.Attendance;
import com.example.kduclubandsociety.Class.Member;
import com.example.kduclubandsociety.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MemberAdapter extends ArrayAdapter<Member> {
    private Activity context;
    private List<Member> memberList;

    public MemberAdapter(Activity context, List<Member> memberList) {
        super(context, R.layout.dashboard_attendance, memberList);
        this.context = context;
        this.memberList = memberList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.attendance_listview_row, null, true);

        TextView name = listViewItem.findViewById(R.id.txtDate);
        CheckBox checkBoxPresent = listViewItem.findViewById(R.id.cbPresent);


        Member member= memberList.get(position);
        Boolean present = member.getPresent();

        name.setText(member.getName());
        name.setTextSize(14);


        if (present){
            checkBoxPresent.setChecked(true);
        }

        else{
            checkBoxPresent.setChecked(false);
        }

        return listViewItem;
    }
}
