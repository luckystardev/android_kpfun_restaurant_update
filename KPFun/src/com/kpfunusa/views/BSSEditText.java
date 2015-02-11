package com.kpfunusa.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.kpfunusa.R;

;

public class BSSEditText extends EditText implements View.OnTouchListener,
		TextWatcher {

	public BSSEditText(Context context) {
		super(context);
		init();
	}

	public BSSEditText(Context context, AttributeSet attris) {
		super(context, attris);
		init();
	}

	public BSSEditText(Context context, AttributeSet attris, int ints) {
		super(context, attris, ints);
		init();
	}

	public void init() {
		Drawable drawable = getResources().getDrawable(R.drawable.search_blue);
		Drawable[] arrDrawable = getCompoundDrawables();
		arrDrawable[0] = drawable;
		setCompoundDrawablesWithIntrinsicBounds(arrDrawable[0], arrDrawable[1],
				arrDrawable[2], arrDrawable[3]);
		setOnTouchListener(this);
		addTextChangedListener(this);
	}

	public void setDrawableRight() {
		Drawable drawable = getResources().getDrawable(R.drawable.close_blue);
		Drawable[] arrDrawable = getCompoundDrawables();
		arrDrawable[2] = drawable;
		// dinh vi tri cua anh o tren duoi trai phai
		setCompoundDrawablesWithIntrinsicBounds(arrDrawable[0], arrDrawable[1],
				arrDrawable[2], arrDrawable[3]);
	}

	public void setHideDrawableRight() {

		Drawable[] arrDrawable = getCompoundDrawables();

		// dinh vi tri cua anh o tren duoi trai phai
		setCompoundDrawablesWithIntrinsicBounds(arrDrawable[0], arrDrawable[1],
				null, arrDrawable[3]);
	}

	public void onTextChanged(CharSequence paramChaferSequence, int paramInt1,
			int paramInt2, int paramInt3) {

	}

	@Override
	public void afterTextChanged(Editable s) {
		if (this.getText().toString().equals("")) {
			setHideDrawableRight();			
		} else {
			setDrawableRight();
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int edtWidth = this.getWidth();
		int edtPadding = this.getPaddingRight();
		int iconWidth = 0;
		Drawable[] arrDrawable = getCompoundDrawables();
		if ((arrDrawable != null) && (arrDrawable.length == 4)) {
			Drawable icon = arrDrawable[2];
			if (icon != null) {
				iconWidth = icon.getIntrinsicWidth();
			}
		}
		if (event.getX() > (edtWidth - edtPadding - iconWidth)) {
			this.setText("");
			setHideDrawableRight();
		}

		return false;
	}

}
