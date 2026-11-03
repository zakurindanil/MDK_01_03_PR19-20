package com.example.camera_zakurin.presentations;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.camera_zakurin.R;
import com.example.network.datas.products.ProductCreate;
import com.example.network.domains.callbacks.MyResponseCallback;
import com.example.network.domains.models.Product;
import com.example.uicomponents.button.BtnCustom;
import com.example.uicomponents.edittext.EtDefault;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Активность для создания нового продукта
 * Позволяет пользователю ввести информацию о продукте и выбрать изображение
 */
public class ProductActivity extends AppCompatActivity {
    // Статическая ссылка на текущий экземпляр активности (используется для доступа из callback'ов)
    public static ProductActivity init;
    // Поля ввода данных продукта
    EtDefault etName, etDescription, etExpenditure, etPrice; // Кастомные поля ввода
    Spinner sCategory;                       // Выпадающий список для категории
    BtnCustom bthCreate;                     // Кнопка создания продукта
    View bthImageSelect;                     // Кнопка выбора изображения
    BottomSheetHelper bottomSheetHelper;     // Помощник для BottomSheet диалога
    String currentPhotoPath;                 // Путь к сохраненному фото
    Uri imageURI;                            // URI выбранного изображения

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        // Сохраняем ссылку на активность для статического доступа
        init = this;
        // Инициализация View элементов
        etName = findViewById(R.id.etName);                 // Поле "Название"
        etDescription = findViewById(R.id.etDescription);   // Поле "Описание"
        etExpenditure = findViewById(R.id.etExpenditure);   // Поле "Расход"
        etPrice = findViewById(R.id.etPrice);               // Поле "Стоимость"
        bthCreate = findViewById(R.id.bthCreate);           // Кнопка "Подтвердить"
        bthImageSelect = findViewById(R.id.bthImageSelect); // Кнопка выбора изображения
        sCategory = findViewById(R.id.sCategory);           // Спиннер категорий
        // Инициализация помощника для BottomSheet диалога выбора источника изображения
        bottomSheetHelper = new BottomSheetHelper(this);
        // Инициализация полей ввода с подсказками и заголовками
        etName.init("Название", "Введите название", "");               // Поле названия
        etDescription.init("Описание", "Введите описание", "");        // Поле описания
        etExpenditure.init("Расход", "Введите расход", "");            // Поле расхода
        etPrice.init("Стоимость", "Введите стоимость", "");            // Поле стоимости
        // Настройка кнопки создания продукта
        bthCreate.init("Подтвердить", BtnCustom.TypeButton.PRIMARY);
        bthCreate.setEnabled(false); // Изначально кнопка неактивна, пока не заполнены все поля
        // Устанавливаем слушатель изменения фокуса для полей ввода
        // При потере фокуса проверяется валидность введенных данных
        etName.editText.setOnFocusChangeListener(LastFocus);
        etPrice.editText.setOnFocusChangeListener(LastFocus);
        // Настройка кнопки выбора изображения
        bthImageSelect.setOnClickListener(v -> {
            // Показываем BottomSheet диалог с вариантами: галерея или камера
            bottomSheetHelper.dialog.show();
        });
        // Настройка кнопки создания продукта
        bthCreate.Btn.setOnClickListener(v -> {
            // Создаем объект Product из данных, введенных пользователем
            Product product = new Product(
                    etName.editText.getText().toString(),                       // Название
                    etDescription.editText.getText().toString(),                // Описание
                    sCategory.getSelectedItemPosition(),                        // Категория (позиция в спиннере)
                    etExpenditure.editText.getText().toString(),                // Расход
                    Integer.parseInt(etPrice.editText.getText().toString())     // Цена (преобразуем в целое число)
            );
            // Создаем и выполняем асинхронную задачу для отправки продукта на сервер
            ProductCreate RequestProductCreate = new ProductCreate(
                    this,                    // Контекст
                    MainActivity.TOKEN,      // Токен авторизации
                    product,                 // Объект продукта
                    imageURI,                // URI выбранного изображения
                    new MyResponseCallback() { // Callback для обработки результата
                        @Override
                        public void onCompile(String result) {
                            Log.d("PRODUCT CREATE", result); // Логируем ответ сервера
                            Toast.makeText(init, "Новый продукт создан!", Toast.LENGTH_SHORT).show();
                            finish(); // Закрываем активность и возвращаемся на главный экран
                        }

                        @Override
                        public void onError(String error) { Log.e("PRODUCT CREATE", error); }
                    }
            );
            // Запускаем асинхронную задачу
            RequestProductCreate.execute();
        });
    }
    /**
     * Слушатель изменения фокуса для полей ввода
     * Выполняет валидацию данных и управляет активностью кнопки "Подтвердить"
     */
    View.OnFocusChangeListener LastFocus = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            // Если поле получило фокус, ничего не делаем (ждем окончания ввода)
            if(hasFocus) return;
            // Флаг валидности всех полей
            boolean state = true;
            // Проверяем, что все текстовые поля заполнены
            if(etName.editText.getText().toString().isEmpty()) state = false;
            if(etDescription.editText.getText().toString().isEmpty()) state = false;
            if(etExpenditure.editText.getText().toString().isEmpty()) state = false;
            if(etPrice.editText.getText().toString().isEmpty()) state = false;
            // Проверяем, что поле цены содержит только цифры
            boolean isCorrectPrice = Pattern.matches("\\d*", etPrice.editText.getText().toString());
            // Отображаем ошибку, если в поле цены есть нецифровые символы
            etPrice.editText.setError(isCorrectPrice ? null : "Поле принимает только цифры");
            if(!isCorrectPrice) state = false;
            // Устанавливаем активность кнопки в зависимости от валидности всех полей
            bthCreate.setEnabled(state);
        }
    };
    // Открывает галерею для выбора существующего изображения
    public void OpenGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");                      // Фильтр только для изображений
        intent.setAction(Intent.ACTION_GET_CONTENT);    // Действие - получение контента
        startActivityForResult(Intent.createChooser(intent, "Выберите изображение"), 1);
    }
    /**
     * Открывает камеру для создания нового фото
     * Создает временный файл для сохранения снимка
     */
    public void OpenCamera() {
        try {
            // Создаем Intent для запуска камеры
            Intent PictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Создаем временный файл для сохранения фотографии
            File PhotoFile = File.createTempFile(
                    "MY_PHOTO_CADR",                                        // Префикс имени файла
                    ".jpg",                                                 // Расширение файла
                    getExternalFilesDir(Environment.DIRECTORY_PICTURES));   // Директория для сохранения
            // Сохраняем абсолютный путь к файлу
            currentPhotoPath = PhotoFile.getAbsolutePath();
            // Получаем URI для файла с использованием FileProvider
            // Это необходимо для безопасного доступа к файлу (начиная с Android 7.0)
            imageURI = FileProvider.getUriForFile(
                    this,
                    getPackageName() + ".provider", // Authority из файла манифеста
                    PhotoFile);
            // Добавляем флаги для временного доступа к URI
            PictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            PictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            // Указываем, куда сохранить сделанное фото
            PictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageURI);
            // Запускаем активность камеры с ожиданием результата
            startActivityForResult(PictureIntent, 2);
        } catch (IOException e) {
            e.printStackTrace(); // Выводим ошибку в лог
        }
    }
    /**
     * Обработка результата от вызванных активностей (галерея/камера)
     * @param requestCode Код запроса (1 - галерея, 2 - камера)
     * @param resultCode Код результата (RESULT_OK - успех, RESULT_CANCELED - отмена)
     * @param data Intent с данными результата
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Закрываем BottomSheet диалог после выбора изображения
        bottomSheetHelper.dialog.cancel();
        // Если операция выполнена успешно
        if (resultCode == RESULT_OK) {
            // Если изображение выбрано из галереи (requestCode = 1)
            if (requestCode == 1)
                imageURI = data.getData(); // Получаем URI выбранного изображения
            // Отображаем выбранное изображение в кнопке (ImageView)
            ((ImageView) bthImageSelect).setImageURI(imageURI);
        }
    }
}
