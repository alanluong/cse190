package com.example.bidit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MessageAdapter extends ArrayAdapter<Message> {

	public MessageAdapter(Context context, int resource) {
		super(context, resource);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;

		if (v == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.messages_list_item, null);
		}

		Message it = this.getItem(position);
		TextView messageContent = (TextView) v.findViewById(R.id.msg_content);
		messageContent.setText("" + it.getContent().substring(0, 20));

		
		return v;
	}
}
