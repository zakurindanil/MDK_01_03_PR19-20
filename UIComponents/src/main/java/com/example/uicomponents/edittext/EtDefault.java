package com.example.uicomponents.edittext;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.uicomponents.R;

public class EtDefault extends ConstraintLayout {
    public TextView title;
    public EditText editText;

    public EtDefault(@NonNull Context context) {
        super(context);
        init();
    }
    public EtDefault(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public EtDefault(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.edit_text, this, true);
        title = findViewById(R.id.et_title);
        editText = findViewById(R.id.et_value);
    }

    // Заголовок, подсказка и значение текстового поля
    public void init(String title, String hint, String text) {
        this.title.setText(title);
        editText.setHint(hint);
        editText.setText(text);
    }
}
