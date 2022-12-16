package com.example.myapplication;

import static com.example.myapplication.CustomButtonStatus.EMPTY;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class CustomButton extends FrameLayout {
    private CustomButton[][] gameBoard = ((MainActivity) MainActivity.main_context).getButtons();

    private int row;
    private int col;
    private int value;
    private int boxRow;
    private int boxCol;
    private CustomButtonStatus status;
    private TextView textView;
    private TableLayout memo;
    private TextView[] memos;

    public CustomButton(@NonNull Context context) {
        super(context);
    }

    public CustomButton(Context context, int row, int col) {
        super(context);
        this.row = row;
        this.col = col;
        this.status = EMPTY;

        boxRow = row/3;
        boxCol = col/3;

        this.textView = makeTextView(context);

        addView(textView);
        setClickable(true);
        setBackgroundResource(R.drawable.button_selector);
    }

    public TextView makeTextView(Context context) {
        TextView makedTextView = new TextView(context);
        makedTextView.setGravity(Gravity.CENTER);
        makedTextView.setTextSize(20);
        makedTextView.setTypeface(Typeface.DEFAULT_BOLD);

        return makedTextView;
    }

    public void set(int a) {
        if (a == 0) {
            this.value = 0;
            textView.setText(null);
        } else {
            this.value = a;
            textView.setText(String.valueOf(value));
        }
    }

    public boolean isConflict(int num) {
        return (isHorizontalConflict(num) || isVerticalConflict(num) || isBoxConflict(num));
    }

    public boolean isVerticalConflict(int num) {
        for(int i=0; i<9; i++) {
            if(gameBoard[i][col].getValue() == num)
                return true;
        }
        return false;
    }

    public boolean isHorizontalConflict(int num) {
        for(int j=0; j<9; j++) {
            if(gameBoard[row][j].getValue() == num)
                return true;
        }
        return false;
    }

    public boolean isBoxConflict(int num) {
        int startRow = boxRow * 3;
        int startCol = boxCol * 3;
        for(int i=startRow; i<startRow+3; i++) {
            for(int j=startCol; j<startCol+3; j++) {
                if(gameBoard[i][j].getValue() == num)
                    return true;
            }
        }
        return false;
    }

    public void setMemoLayout(Context context) {
        if(this.status != CustomButtonStatus.GIVEN) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            memo = (TableLayout) layoutInflater.inflate(R.layout.layout_memo, null);
            addView(memo);
        }
    }

    public void setConflict() {
        setBackgroundColor(Color.RED);
    }

    public void unsetConflict() {
        setBackgroundColor(Color.WHITE);
    }

    public TextView[] getMemos() {
        memos = new TextView[9];
        int k=0;
        for(int i=0; i<3; i++) {
            TableRow tableRow = (TableRow) memo.getChildAt(i);
            for(int j=0; j<3; j++, k++) {
                memos[k] = (TextView) tableRow.getChildAt(j);
            }
        }
        return memos;
    }

    public int getValue() {
        return value;
    }

    public CustomButtonStatus getStatus() {
        return status;
    }

    public void setStatus(CustomButtonStatus status) {
        this.status = status;
    }
}
