package com.example.network.datas.users;

import com.example.network.domains.apis.MyAsyncTask;
import com.example.network.domains.callbacks.MyResponseCallback;
import com.example.network.domains.common.Settings;
import com.example.network.domains.models.User;
import com.google.gson.GsonBuilder;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

/**
 * Асинхронная задача для выполнения входа пользователя в систему.
 * Отправляет POST-запрос с данными пользователя на сервер и обрабатывает ответ.
 * Наследуется от {@link MyAsyncTask} и использует колбэк для возврата результата.
 */
public class UserLogin extends MyAsyncTask {
    /** Объект пользователя с данными для входа (email/username и пароль) */
    User user;

    public UserLogin(User user, MyResponseCallback callback) {
        super(callback);
        this.user = user;
    }

    /**
     * Выполняет HTTP-запрос к серверу в фоновом потоке.
     * Преобразует объект пользователя в JSON, отправляет POST-запрос на endpoint /api/user/login
     * и возвращает результат в виде строки.
     */
    @Override
    protected String doInBackground(Void... voids) {
        // Преобразование объекта User в JSON-строку с помощью Gson
        String rawData = new GsonBuilder().create().toJson(user);
        try {
            // Выполнение HTTP POST-запроса к серверу
            Connection.Response response = Jsoup.connect(Settings.URL + "/api/user/login")
                    .ignoreContentType(true)      // Игнорировать проверку Content-Type
                    .ignoreHttpErrors(true)       // Не выбрасывать исключения при HTTP-ошибках
                    .method(Connection.Method.POST) // Установка метода POST
                    .header("Content-type", "application/json") // Заголовок с типом контента
                    .requestBody(rawData)         // Установка тела запроса (JSON с данными пользователя)
                    .execute();                   // Выполнение запроса
            // Проверка кода ответа: 200 = успешная авторизация
            return response.statusCode() == 200 ?
                    response.body() :                 // Возвращаем тело ответа при успехе
                    "Error: " + response.body();      // Возвращаем ошибку с телом ответа
        } catch (IOException e) {
            // Обработка исключений при выполнении запроса
            return "Error: " + e.getMessage();
        }
    }
}
