package com.example.camera_zakurin.presentations;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.camera_zakurin.R;

public class ProductActivity extends AppCompatActivity {
    // Статическая ссылка на текущий экземпляр активности
    public static ProductActivity init;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        init = this;
    }
    // Открывает галерею для выбора изображения
    public void OpenGallery() { }
    // Открывает камеру для создания нового фото
    public void OpenCamera() { }
}
