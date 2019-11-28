package com.example.cecd;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.cecd.ui.selection.SelectionFragment;

import java.util.ArrayList;

/**
 * Created by coderzpassion on 13/03/16.
 */
public class obj extends BaseAdapter {

    private Context mContext;
    ArrayList<Data> mylist;

    public obj(ArrayList<Data> itemArray,Context mContext) {
        super();
        this.mContext = mContext;
        mylist = itemArray;
    }

    @Override
    public int getCount() {
        return mylist.size();
    }

    @Override
    public String getItem(int position) {
        return mylist.get(position).toString();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder {
        public TextView nametext;
        public CheckBox tick;
    }

    @Override
    public View getView(final int position, View convertView,
                        ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder view = null;
        LayoutInflater inflator = ((Activity) mContext).getLayoutInflater();

        if (view == null) {
            view = new ViewHolder();
            convertView = inflator.inflate(	R.layout.object_layout, null);

            view.nametext = convertView.findViewById(R.id.object_text_view);
            view.tick = convertView.findViewById(R.id.object_checkbox);
            view.tick.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                                             boolean isChecked) {
                    int getPosition = (Integer) buttonView.getTag(); // Here
                    // we get  the position that we have set for the checkbox using setTag.
                    mylist.get(getPosition).setChecked(buttonView.isChecked()); // Set the value of checkbox to maintain its state.
                    SelectionFragment.update();
                }
            });
            convertView.setTag(view);
        } else {
            view = (ViewHolder) convertView.getTag();
        }

        view.tick.setTag(position);
        view.nametext.setText("" + mylist.get(position).getLabel());
        view.tick.setChecked(mylist.get(position).isChecked());

        return convertView;
    }

}