package com.example.bidit;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class BidDialogFragment extends DialogFragment {
	
	private EditText mEditText;

    public BidDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    public static BidDialogFragment newInstance() {
    	BidDialogFragment frag = new BidDialogFragment();
        Bundle args = new Bundle();
        //put any arguments - for example: 
        //args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bid, container);
        mEditText = (EditText) view.findViewById(R.id.txt_your_bid);
        
        //get the arguments here
        //String title = getArguments().getString("title", "Enter Name");
        //getDialog().setTitle(title);
        getDialog().setTitle(R.string.bid_prompt);
        
        // Show soft keyboard automatically
        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        
        Button cancelButton = (Button)(view.findViewById(R.id.btn_cancel));
        cancelButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				dismiss();
			}
        });
        Button bidButton = (Button)(view.findViewById(R.id.btn_bid));
        bidButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				//bid
			}
        });
        
        return view;
    }
}
