package com.example.taurus.bihu;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Taurus on 2018/2/24.
 * 回答问题的Dialog
 */

public class AnswerDialog extends Dialog {
    private EditText mAnswerEdit;
    private Button mButtonDone;
    private Button mButtonCancel;
    private TextView mTextView;

    public AnswerDialog(@NonNull Context context) {
        super(context);
    }

    public TextView getmTextView() {
        return mTextView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_answer);
        mAnswerEdit = (EditText) findViewById(R.id.answer_edit);
        mButtonCancel = (Button) findViewById(R.id.cancel);
        mButtonDone = (Button) findViewById(R.id.done);
        mTextView = (TextView) findViewById(R.id.dialog_text);

    }

    public EditText getmAnswerEdit() {
        return mAnswerEdit;
    }

    public void setmButtonDone(View.OnClickListener listener) {
        if (listener != null)
            mButtonDone.setOnClickListener(listener);
    }

    public void setmButtonCancel(View.OnClickListener listener) {
        if (listener != null)
            mButtonCancel.setOnClickListener(listener);
    }

}
