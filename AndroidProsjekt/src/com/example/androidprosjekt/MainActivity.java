package com.example.androidprosjekt;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MainActivity extends Activity {
	
	private static final String TOTAL_PRICE = "TOTAL_PRICE";
	private static final String CURRENT_DISCOUNT = "CURRENT_DISCOUNT";
	private static final String PRICE_WITHOUT_DISCOUNT = "PRICE_WITHOUT_DISCOUNT";
	
	private double priceBeforeDiscount;
	private double discountAmount;
	private double finalPrice;
	
	EditText priceBeforeDiscountET;
	EditText discountAmountET;
	EditText finalPriceET;
	
	SeekBar discountSeekBar;
	TextView netError;

	ConnectivityManager connMgr; 
	NetworkInfo networkInfo; 	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		
		if(savedInstanceState == null ) {
			priceBeforeDiscount = 0.0;
			discountAmount = .15;
			finalPrice = 0.0;
		} else {
			priceBeforeDiscount = savedInstanceState.getDouble(PRICE_WITHOUT_DISCOUNT);
			discountAmount = savedInstanceState.getDouble(CURRENT_DISCOUNT);
			finalPrice = savedInstanceState.getDouble(TOTAL_PRICE);
		}
		
		priceBeforeDiscountET = (EditText) findViewById(R.id.priceEditText);
		discountAmountET = (EditText) findViewById(R.id.discountEditText);
		finalPriceET = (EditText) findViewById(R.id.finalEditText);
		
		
		discountSeekBar = (SeekBar) findViewById(R.id.discountSeekBar);
		discountSeekBar.setOnSeekBarChangeListener(discountSeekBarListener);
		priceBeforeDiscountET.addTextChangedListener(priceBeforeDiscountListener);
		
		if(!isOnline()) {
			String onlineErr = "You are not connected to the Internet. Some features are disabled!";
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setMessage(onlineErr).create().show();
		}	
		
	}
	
	private Boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
		return (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected());

	}

	private void updateDiscountAndFinalPrice(){
		double discountAmount = Double.parseDouble(discountAmountET.getText().toString());
		double finalPrice = priceBeforeDiscount - (priceBeforeDiscount * discountAmount);		
		
		finalPriceET.setText(String.format("%.02f", finalPrice));	
	}
	
	private TextWatcher priceBeforeDiscountListener = new TextWatcher(){

		@Override
		public void afterTextChanged(Editable arg0) {}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {			
			try {
				priceBeforeDiscount = Double.parseDouble(arg0.toString());
			}
			catch(NumberFormatException e){	
				priceBeforeDiscount = 0.0;
			}
			
			updateDiscountAndFinalPrice();			
		}				
	};
	
	protected void onSaveInstanceState(Bundle outState){
		
		super.onSaveInstanceState(outState);
		
		outState.putDouble(TOTAL_PRICE, finalPrice);
		outState.putDouble(CURRENT_DISCOUNT, discountAmount);
		outState.putDouble(PRICE_WITHOUT_DISCOUNT, priceBeforeDiscount);
		
	}
	
	private OnSeekBarChangeListener discountSeekBarListener = new OnSeekBarChangeListener() {

		@Override
		public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
			discountAmount = (discountSeekBar.getProgress()) * .01;
			discountAmountET.setText(String.format("%.02f", discountAmount));
			updateDiscountAndFinalPrice();
				
		}

		@Override
		public void onStartTrackingTouch(SeekBar arg0) {}

		@Override
		public void onStopTrackingTouch(SeekBar arg0) {}		
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}