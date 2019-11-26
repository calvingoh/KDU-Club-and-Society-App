package com.example.kduclubandsociety.Dashboard;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.kduclubandsociety.Class.Attendance;
import com.example.kduclubandsociety.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AttendanceAdapter extends ArrayAdapter<Attendance> {
    private Activity context;
    private List<Attendance> attendanceList;

    public AttendanceAdapter(Activity context, List<Attendance> attendanceList) {
        super(context, R.layout.dashboard_attendance, attendanceList);
        this.context = context;
        this.attendanceList = attendanceList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.attendance_listview_row, null, true);

        TextView date = listViewItem.findViewById(R.id.txtDate);
        date.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        CheckBox checkBoxPresent = listViewItem.findViewById(R.id.cbPresent);
        checkBoxPresent.setVisibility(View.GONE);


        Attendance meeting= attendanceList.get(position);

        date.setText(meeting.getDate());
        //checkBoxPresent.setChecked(false);
        return listViewItem;
    }
}
