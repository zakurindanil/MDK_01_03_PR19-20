package com.example.network.datas.products;
import android.content.Context;
import android.net.Uri;
import com.example.network.domains.apis.MyAsyncTask;
import com.example.network.domains.callbacks.MyResponseCallback;
import com.example.network.domains.common.Settings;
import com.example.network.domains.models.Product;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
public class ProductCreate extends MyAsyncTask {
    // Контекст приложения для доступа к ContentResolver и другим системным сервисам
    Context context;
    // Токен аутентификации пользователя для авторизации на сервере
    String token;
    // Объект продукта, содержащий данные для создания (название, описание, цену и т.д.)
    Product product;
    // URI изображения продукта, полученный из галереи или камеры
    Uri uri;
    public ProductCreate(Context context, String token, Product product, Uri uri, MyResponseCallback callback) {
        super(callback);

        this.context = context;
        this.token = token;
        this.product = product;
        this.uri = uri;
    }
    // Фоновый метод, выполняющий отправку данных продукта на сервер
    @Override
    protected String doInBackground(Void... voids) {
        try {
            // Открываем InputStream для чтения данных изображения по URI
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            // Создаем временный файл и копируем в него содержимое изображения
            File tempFile = createTempFileFromServer(inputStream);
            // Формируем параметры запроса с данными продукта
            Map<String, String> params = new HashMap<>();
            params.put("Name", product.name);                            // Название продукта
            params.put("Description", product.description);              // Описание продукта
            params.put("Gender", String.valueOf(product.gender));        // Половая принадлежность
            params.put("Expenditure", product.expenditure);              // Расходы/затраты
            params.put("Price", String.valueOf(product.price));          // Цена продукта
            // Выполняем HTTP POST запрос к API сервера
            Connection.Response response = Jsoup.connect(Settings.URL + "/api/product/create")
                    .ignoreContentType(true)      // Игнорируем Content-Type для работы с файлами
                    .ignoreHttpErrors(true)       // Не выбрасывать исключения при HTTP ошибках
                    .method(Connection.Method.POST) // Метод POST для создания ресурса
                    .header("token", token)       // Добавляем токен авторизации в заголовок
                    .data(params)                 // Добавляем текстовые параметры
                    .data("ImageFile", tempFile.getName(), new java.io.FileInputStream(tempFile)) // Добавляем файл изображения
                    .execute();                   // Выполняем запрос
            // Возвращаем тело ответа если статус 200 (OK), иначе возвращаем текст ошибки
            return response.statusCode() == 200 ? response.body() : "Error: " + response.body();
        } catch (IOException e) {
            // В случае ошибки ввода-вывода возвращаем сообщение об ошибке
            return "Error: " + e.getMessage();
        }
    }
    // Создает временный файл из потока ввода InputStream
    public File createTempFileFromServer(InputStream inputStream) {
        try {
            // Создаем временный файл с префиксом "upload_" и расширением ".jpg"
            File tempFile = File.createTempFile("upload_", ".jpg");
            // Устанавливаем удаление файла при завершении работы приложения
            tempFile.deleteOnExit();
            // Копируем данные из InputStream во временный файл
            try (FileOutputStream out = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[1024]; // Буфер для чтения данных порциями по 1КБ
                int bytesRead;
                // Читаем данные из потока и записываем в файл, пока не достигнем конца
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }

            return tempFile; // Возвращаем созданный временный файл
        } catch (IOException e) {
            e.printStackTrace(); // Выводим стек ошибки в лог
        }
        return null; // В случае ошибки возвращаем null
    }
}
