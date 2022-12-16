package com.example.myapplication;

import static com.example.myapplication.CustomButtonStatus.CONFLICT;
import static com.example.myapplication.CustomButtonStatus.EMPTY;
import static com.example.myapplication.CustomButtonStatus.INPUT;
import static com.example.myapplication.CustomButtonStatus.MEMO;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class NumberPad extends Dialog {
    private CustomButton clickedBtn;
    private TableLayout numPad;
    private TableRow[] numberRows;
    private Button[][] numPadBtn;

    private View.OnClickListener mClickNumListener;
    private View.OnClickListener mClickCancelListener;
    private View.OnClickListener mClickDelListener;

    public NumberPad(@NonNull Context context) {
        super(context);
    }

    public NumberPad(Context context, CustomButton clickedBtn) {
        super(context);
        this.clickedBtn = clickedBtn;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.numpad);

        onCreateNumPad();
    }

    public void onCreateNumPad() {
        numPad = (TableLayout) findViewById(R.id.numpad);
        onCreateTextView();
        onCreateRowBtn();
//        onCreateNumberRow();
//        onCreateNumPadBtn();
        onCreateBottomRow();
    }

    public void onCreateTextView() {
        TextView textView = new TextView(numPad.getContext());
        textView.setText("Input Number");
        TableLayout.LayoutParams textViewParam = new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        textView.setLayoutParams(textViewParam);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setTextSize(20);
        numPad.addView(textView);
    }

    public void onCreateRowBtn() {
        int count = 1;
        numberRows = new TableRow[3];
        numPadBtn = new Button[3][3];
        for(int i=0; i<3; i++) {
            numberRows[i] = new TableRow(numPad.getContext());
            for(int j=0; j<3; j++) {
                numPadBtn[i][j] = new Button(numPad.getContext());
                numPadBtn[i][j].setText(String.valueOf(count++));
                numberRows[i].addView(numPadBtn[i][j]);
                mClickNumListener = onCreateClickListener();
                numPadBtn[i][j].setOnClickListener(mClickNumListener);
            }
            numPad.addView(numberRows[i]);
        }
    }

    public void onCreateNumberRow() {
        numberRows = new TableRow[3];
        for(int i=0; i<3; i++) {
            numberRows[i] = new TableRow(numPad.getContext());
            numPad.addView(numberRows[i]);
        }
    }

    public void onCreateNumPadBtn() {
        int count = 1;
        numPadBtn = new Button[3][3];
        for(int i=0; i<3; i++) {
            for(int j=0; j<3; j++) {
                numPadBtn[i][j] = new Button(numPad.getContext());
                numPadBtn[i][j].setText(String.valueOf(count++));
                numberRows[i].addView(numPadBtn[i][j]);
                mClickNumListener = onCreateClickListener();
                numPadBtn[i][j].setOnClickListener(mClickNumListener);
            }
        }
    }

    public void onCreateBottomRow() {
        TableRow bottomRow = new TableRow(numPad.getContext());
        numPad.addView(bottomRow);

        Button emptyBtn = new Button(numPad.getContext());
        emptyBtn.setVisibility(View.INVISIBLE);

        Button cancelBtn = new Button(numPad.getContext());
        cancelBtn.setText("CANCEL");

        Button delBtn = new Button(numPad.getContext());
        delBtn.setText("DEL");

        bottomRow.addView(emptyBtn);
        bottomRow.addView(cancelBtn);
        bottomRow.addView(delBtn);

        mClickCancelListener = onClickCancelListener();
        cancelBtn.setOnClickListener(mClickCancelListener);

        mClickDelListener = onClickDelListener();
        delBtn.setOnClickListener(mClickDelListener);
    }

    public View.OnClickListener onCreateClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int choicedBtnNum = Integer.parseInt(((Button) view).getText().toString());

                if(clickedBtn.getStatus() == MEMO) {
                    TextView[] memos = clickedBtn.getMemos();
                    for(int i=0; i<memos.length; i++) {
                        memos[i].setVisibility(View.INVISIBLE);
                    }
                }

                if(clickedBtn.isConflict(choicedBtnNum)) {
                    clickedBtn.setStatus(CONFLICT);
                    clickedBtn.setConflict();
                } else {
                    clickedBtn.unsetConflict();
                }

                clickedBtn.set(choicedBtnNum);
                clickedBtn.setStatus(INPUT);
                dismiss();
            }
        };
    }

    public View.OnClickListener onClickCancelListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        };
    }

    public View.OnClickListener onClickDelListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedBtn.setStatus(EMPTY);
                clickedBtn.setBackgroundColor(Color.WHITE);
                clickedBtn.set(0);
                dismiss();
            }
        };
    }
}
