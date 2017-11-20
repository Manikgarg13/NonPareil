package de.makkiato.android.mastermind;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class GameView extends LinearLayout {

	public GameView(Context context, int id) {
		super(context);//will call predefined function of linearlayout
		String infService = Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);//gets its context from view class(pre defined) and get getSystemServices from activity class
		li.inflate(id, this, true);//attach linear layout to the main class
	}
	
	public GameView(Context context, AttributeSet ats) {
		super(context, ats);
	}
}
