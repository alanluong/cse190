package com.example.bidit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

public class SendMessageDialogFragment extends DialogFragment {
	
	public static SendMessageDialogFragment newInstance(){
		SendMessageDialogFragment instance = new SendMessageDialogFragment();
		return instance;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		final View view = inflater.inflate(R.layout.dialog_sendmessage, null);

		builder.setView(view)
				.setPositiveButton(R.string.send,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface di, int id) {
								//send message
								
							}

						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								dismiss();
							}
						});
		
		return builder.create();
	}

}
