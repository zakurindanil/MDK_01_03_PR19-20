package com.example.network.datas.users;
import com.example.network.domains.apis.MyAsyncTask;
import com.example.network.domains.callbacks.MyResponseCallback;
import com.example.network.domains.common.Settings;
import com.example.network.domains.models.User;
import com.google.gson.GsonBuilder;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import java.io.IOException;
public class UserUpdate extends MyAsyncTask {
    User user;
    String token; // токен пользователя
    public UserUpdate(User user, String token, MyResponseCallback callback) {
        super(callback);

        this.user = user;
        this.token = token;
    }
    @Override
    protected String doInBackground(Void... voids) {
        String rawData = new GsonBuilder().create().toJson(user);

        try {
            Connection.Response response = Jsoup.connect(Settings.URL + "/api/user/update")
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .method(Connection.Method.PUT)
                    .header("Content-type", "application/json")
                    .header("token", token) // Подставляем токен пользователя
                    .requestBody(rawData)
                    .execute();

            return response.statusCode() == 200 ? response.body() : "Error: " + response.body();
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
    }
}
