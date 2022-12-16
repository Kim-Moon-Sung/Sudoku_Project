package com.example.myapplication;

import static com.example.myapplication.CustomButtonStatus.EMPTY;
import static com.example.myapplication.CustomButtonStatus.GIVEN;
import static com.example.myapplication.CustomButtonStatus.INPUT;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    public static Context main_context;

    private TableLayout table;
    private TableRow[] tableRows;
    private TableRow botRow;
    private CustomButton[][] buttons;
    private Button resetBtn;
    private Button confirmBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main_context = this;

        table = (TableLayout) findViewById(R.id.tableLayout);
        table.setPadding(20, 20, 20, 20);

        BoardGenerator board = new BoardGenerator();

        tableRows = new TableRow[9];
        buttons = new CustomButton[9][9];
        for(int i=0; i<9; i++) {
            tableRows[i] = new TableRow(this);
            for(int j=0; j<9; j++) {
                TableRow.LayoutParams layoutParams = onCreateLayoutParam(i, j);
                buttons[i][j] = new CustomButton(this, i, j);
                buttons[i][j].setLayoutParams(layoutParams);
                if(Math.random() < 0.65) {
                    int number = board.get(i, j);
                    buttons[i][j].set(number);
                    buttons[i][j].setStatus(GIVEN);
                }
                buttons[i][j].setMemoLayout(this);
                tableRows[i].addView(buttons[i][j]);
            }
            table.addView(tableRows[i]);
        }

        botRow = new TableRow(this);
        table.addView(botRow);

        TableRow.LayoutParams layoutParams = onCreateLayoutParam(0, 0);

        resetBtn = new Button(this);
        resetBtn.setText("RESET");
        resetBtn.setLayoutParams(layoutParams);
        confirmBtn = new Button(this);
        confirmBtn.setText("Confirm");
        confirmBtn.setLayoutParams(layoutParams);
        botRow.addView(resetBtn);
        botRow.addView(confirmBtn);

        enterEventListener();
        enterLongClickEventListener();
        enterClickResetBtn();
        enterClickConfirmBtn();
    }


    public TableRow.LayoutParams onCreateLayoutParam(int row, int col) {
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, 135, 1.0f);

        if ((row==2 && col==2) || (row==2 && col==5) || (row==5 && col==2) || (row==5 && col==5)) {
            layoutParams.setMargins(10, 10, 20, 20);
        } else if (row==2 || row==5) {
            layoutParams.setMargins(10, 10, 10, 20);
        } else if (col==2 || col==5) {
            layoutParams.setMargins(10, 10, 20, 10);
        } else {
            layoutParams.setMargins(10, 10, 10, 10);
        }

        return layoutParams;
    }

    public void enterEventListener() {
        for(int i=0; i<9; i++) {
            for(int j=0; j<9; j++) {
                if(buttons[i][j].getStatus() != GIVEN) {
                    buttons[i][j].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            NumberPad numberPad = new NumberPad(table.getContext(), (CustomButton)view);
                            numberPad.show();
                        }
                    });
                }
            }
        }
    }

    public void enterLongClickEventListener() {
        for(int i=0; i<9; i++) {
            for(int j=0; j<9; j++) {
                if(buttons[i][j].getStatus() != GIVEN) {
                    buttons[i][j].setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            MemoPad memoPad = new MemoPad(table.getContext(), (CustomButton) view);
                            memoPad.show();
                            return true;
                        }
                    });
                }
            }
        }
    }

    public void enterClickResetBtn() {
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i=0; i<9; i++) {
                    for(int j=0; j<9; j++) {
                        if(buttons[i][j].getStatus() != GIVEN) {
                            buttons[i][j].setStatus(EMPTY);
                            buttons[i][j].unsetConflict();
                            buttons[i][j].set(0);
                            TextView[] btnMemos = buttons[i][j].getMemos();
                            for(int k=0; k<btnMemos.length; k++) {
                                btnMemos[k].setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                }
            }
        });
    }

    public void enterClickConfirmBtn() {
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (confirm()) {
                    Toast.makeText(getApplicationContext(), "정답입니다!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "틀렸습니다!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean confirm() {
        for(int i=0; i<9; i++) {
            for (int j = 0; j < 9; j++) {
                CustomButtonStatus status = buttons[i][j].getStatus();
                if(status != GIVEN && status != INPUT)
                    return false;
            }
        }
        return true;
    }

    public CustomButton[][] getButtons() {
        return buttons;
    }
}