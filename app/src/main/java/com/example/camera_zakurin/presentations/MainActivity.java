package com.example.camera_zakurin.presentations;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.camera_zakurin.R;
import com.example.camera_zakurin.domains.PermissionManager;
import com.example.network.datas.products.ProductGetUser;
import com.example.network.domains.callbacks.MyResponseCallback;
import com.example.network.domains.models.Product;
import com.example.uicomponents.button.BtnCustom;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Главная активность приложения
 * Отображает список продуктов и предоставляет возможность добавления новых продуктов
 */
public class MainActivity extends AppCompatActivity {
    // Статический токен для авторизации на сервере
    public static String TOKEN = "031e353b-7fd7-4d36-b802-affd0c5587b2";
    View bthOpenAddProduct; // Кнопка для открытия экрана добавления нового продукта
    LinearLayout llContent; // контейнер для динамического отображения списка продуктов
    List<Product> Products; // Список продуктов, полученных с сервера

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Запрашиваем разрешение на использование камеры (нужно для добавления фото продукта)
        PermissionManager.GetPermission(this, this);
        // Инициализация View элементов
        bthOpenAddProduct = findViewById(R.id.bthOpenAddProduct); // Кнопка добавления продукта
        llContent = findViewById(R.id.llContent);                 // Контейнер для списка продуктов
        // Настройка обработчика нажатия на кнопку добавления продукта
        bthOpenAddProduct.setOnClickListener(v -> {
            // Создаем Intent для перехода к ProductActivity
            Intent intent = new Intent(this, ProductActivity.class);
            startActivity(intent); // Запускаем активность
        });
        // Загружаем список продуктов с сервера
        ProductGetUser();
    }
    // Загружает список продуктов пользователя с сервера
    public void ProductGetUser() {
        // Создаем экземпляр асинхронной задачи для получения продуктов
        ProductGetUser RequestProductGetUser = new ProductGetUser(TOKEN, new MyResponseCallback() {
            @Override
            public void onCompile(String result) {
                // Логируем полученный ответ для отладки
                Log.d("PRODUCT GET USER", result);
                // Парсим JSON строку в список объектов Product
                Products = new GsonBuilder().create().fromJson(
                        result,
                        new TypeToken<ArrayList<Product>>(){}.getType() // Тип для десериализации
                );
                // Создаем UI элементы для каждого продукта
                CreateElement();
            }

            @Override
            public void onError(String error) { Log.e("PRODUCT GET USER", error); }
        });
        // Запускаем асинхронную задачу
        RequestProductGetUser.execute();
    }
    /**
     * Создает элементы интерфейса для каждого продукта в списке
     * Динамически добавляет View в контейнер llContent
     */
    public void CreateElement() {
        // Перебираем все продукты из загруженного списка
        for(Product product : Products) {
            // Инфлейтим (раздуваем) макет элемента продукта
            View itemProduct = LayoutInflater.from(this).inflate(R.layout.item_product, llContent, false);
            // Получаем ссылки на элементы управления внутри макета продукта
            BtnCustom bthBig = itemProduct.findViewById(R.id.bthOpenProduct); // Кнопка открытия продукта
            TextView tvName = itemProduct.findViewById(R.id.tvName);           // Поле для названия
            TextView tvPrice = itemProduct.findViewById(R.id.tvPrice);         // Поле для цены
            // Настраиваем кнопку: текст "Открыть" и основной стиль
            bthBig.init("Открыть", BtnCustom.TypeButton.PRIMARY);
            // Устанавливаем название продукта
            tvName.setText(product.name);
            // Устанавливаем цену продукта с добавлением символа рубля
            tvPrice.setText(product.price + " ₽");
            // Добавляем подготовленный View в контейнер списка продуктов
            llContent.addView(itemProduct);
        }
    }
}
