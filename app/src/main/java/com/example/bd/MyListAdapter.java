package com.example.bd;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MyListAdapter extends ArrayAdapter<Person> {

    public MyListAdapter(@NonNull Context context, ArrayList<Person> people) {
        super(context, R.layout.adapter_item, people);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final Person person = getItem(position); //получаем одну запись

        if (convertView == null){ //если объектов на экране под запись нет, то создаем по разметке
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_item, null);
        }

        //заполняем объекты на экране данными из записи
        ((TextView) convertView.findViewById(R.id.column_id)).setText(String.valueOf(person.getId()));
        ((TextView) convertView.findViewById(R.id.column_name)).setText(String.valueOf(person.getName()));
        ((TextView) convertView.findViewById(R.id.column_value)).setText(String.valueOf(person.getValue()));

        return convertView;
    }
}
