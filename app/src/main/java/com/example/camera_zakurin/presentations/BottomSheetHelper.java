package com.example.camera_zakurin.presentations;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.example.camera_zakurin.R;
import com.example.uicomponents.button.BtnCustom;
import com.google.android.material.bottomsheet.BottomSheetDialog;

/**
 * Вспомогательный класс для управления BottomSheet диалогом выбора изображения
 * Предоставляет пользователю выбор между галереей и камерой для получения изображения
 */
public class BottomSheetHelper {
    // Диалоговое окно BottomSheet для отображения вариантов выбора
    public BottomSheetDialog dialog;
    // Конструктор - создает и настраивает BottomSheet диалог
    public BottomSheetHelper(Context context) {
        // Инициализируем BottomSheetDialog с переданным контекстом
        dialog = new BottomSheetDialog(context);
        // Инфлейтим (раздуваем) XML макет для содержимого BottomSheet
        // R.layout.bs_select_image - макет с двумя кнопками для выбора источника изображения
        View view = LayoutInflater.from(context).inflate(R.layout.bs_select_image, null);
        // Получаем ссылки на кнопки из макета
        BtnCustom bthGallery = view.findViewById(R.id.bthGallery); // Кнопка "Выбрать из галереи"
        BtnCustom bthCamera = view.findViewById(R.id.bthCamera);   // Кнопка "Сфотографировать"
        // Инициализируем кнопку галереи с текстом и стилем
        bthGallery.init(
                "🖼️ Выбрать из галереи",  // Текст с иконкой
                BtnCustom.TypeButton.SECONDARY             // Вторичный стиль кнопки
        );
        // Инициализируем кнопку камеры с текстом и стилем
        bthCamera.init(
                "📸 Сфотографировать",          // Текст с иконкой
                BtnCustom.TypeButton.SECONDARY             // Вторичный стиль кнопки
        );
        // Устанавливаем обработчик нажатия для кнопки галереи
        bthGallery.Btn.setOnClickListener(v -> {
            // При нажатии открываем галерею для выбора изображения
            ProductActivity.init.OpenGallery();
        });
        // Устанавливаем обработчик нажатия для кнопки камеры
        bthCamera.Btn.setOnClickListener(v -> {
            // При нажатии открываем камеру для создания нового фото
            ProductActivity.init.OpenCamera();
        });
        // Устанавливаем подготовленное View в качестве содержимого диалога
        dialog.setContentView(view);
    }
}
