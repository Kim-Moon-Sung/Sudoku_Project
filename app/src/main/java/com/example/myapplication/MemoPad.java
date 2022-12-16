package com.example.myapplication;

import static com.example.myapplication.CustomButtonStatus.CONFLICT;
import static com.example.myapplication.CustomButtonStatus.EMPTY;
import static com.example.myapplication.CustomButtonStatus.INPUT;
import static com.example.myapplication.CustomButtonStatus.MEMO;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class MemoPad extends AlertDialog.Builder {
    private CustomButton clickedBtn;
    private ArrayList<ToggleButton> selectedMemoBtn = new ArrayList<>();

    public MemoPad(Context context) {
        super(context);
    }

    public MemoPad(Context context, CustomButton clickedBtn) {
        super(context);
        this.clickedBtn = clickedBtn;

        setTitle("Memo");
        View memoDialogView = (View)View.inflate(context, R.layout.dialog_memo, null);
        setView(memoDialogView);

        setNeutralButton("DELETE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int k) {
                clickedBtn.setStatus(EMPTY);
                TextView[] memos = clickedBtn.getMemos();
                for(int i=0; i<memos.length; i++) {
                    memos[i].setVisibility(View.INVISIBLE);
                }
                dialogInterface.cancel();
            }
        });

        setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int k) {
                if(clickedBtn.getStatus() == EMPTY || clickedBtn.getStatus() == MEMO) {
                    clickedBtn.setStatus(MEMO);
                    TextView[] memos = clickedBtn.getMemos();
                    for(int i=0; i<selectedMemoBtn.size(); i++) {
                        int choicedMemoNum = Integer.parseInt((String)selectedMemoBtn.get(i).getText()) - 1;
                        memos[choicedMemoNum].setVisibility(View.VISIBLE);
                    }
                }
                dialogInterface.cancel();
            }
        });

        TableLayout memoDial = (TableLayout)memoDialogView.findViewById(R.id.dialogMemo);
        for(int i=0; i<3; i++) {
            TableRow toggleRow = (TableRow) memoDial.getChildAt(i);
            for(int j=0; j<3; j++) {
                ToggleButton toggleButton = (ToggleButton)toggleRow.getChildAt(j);
                toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        if(isChecked)
                            selectedMemoBtn.add(toggleButton);
                        else
                            selectedMemoBtn.remove(toggleButton);
                    }
                });
            }
        }
    }
}
