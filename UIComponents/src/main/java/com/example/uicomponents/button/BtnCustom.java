package com.example.uicomponents.button;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.uicomponents.R;

public class BtnCustom extends ConstraintLayout {
    public Button Btn;

    public enum TypeButton {
        PRIMARY, TETRIARY, SECONDARY, OFF, ON
    }

    public BtnCustom(@NonNull Context context) {
        super(context);
        init(R.layout.button);
    }
    public BtnCustom(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(R.layout.button);
    }
    public BtnCustom(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(R.layout.button);
    }

    public void init(Integer idLayout) {
        if (idLayout == null) return;
        LayoutInflater.from(this.getContext()).inflate(idLayout, this, true);
        Btn = findViewById(R.id.btn);
    }

    public void init(String value, TypeButton type) {
        Btn.setText(value);
        if (type == TypeButton.PRIMARY || type == TypeButton.ON) {
            Btn.setBackgroundResource(R.drawable.btn_primary);
            Btn.setTextColor(Color.parseColor("#FFFFFF"));
        } else if (type == TypeButton.SECONDARY) {
            Btn.setBackgroundResource(R.drawable.btn_secondary);
            Btn.setTextColor(Color.parseColor("#1A6FEE"));
        } else if (type == TypeButton.TETRIARY) {
            Btn.setBackgroundResource(R.drawable.btn_tetriary);
            Btn.setTextColor(Color.parseColor("#2D2C2C"));
        } else if (type == TypeButton.OFF) {
            Btn.setBackgroundResource(R.drawable.btn_tetriary);
            Btn.setTextColor(Color.parseColor("#7E7E9A"));
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        Btn.setEnabled(enabled);
    }
}
