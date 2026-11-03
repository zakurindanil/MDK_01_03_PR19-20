package com.example.network.domains.apis;

import android.os.AsyncTask;

import com.example.network.domains.callbacks.MyResponseCallback;

/**
 * Асинхронная задача для выполнения сетевых операций или компиляции в фоновом потоке.
 * Использует колбэк {@link MyResponseCallback} для передачи результата обратно в UI-поток.
 */
public class MyAsyncTask extends AsyncTask<Void, Void, String> {
    // Ссылка на колбэк для уведомления о результате выполнения задачи.
    protected MyResponseCallback callback;

    public MyAsyncTask(MyResponseCallback callback) {
        this.callback = callback;
    }

    // Здесь должна происходить основная работа (сетевые запросы, компиляция и т.д.).
    @Override
    protected String doInBackground(Void... voids) {
        return "";
    }

    /**
     * Выполняется в UI-потоке после завершения фоновой задачи.
     * Анализирует результат и вызывает соответствующий метод колбэка.
     */
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        // Проверяем, передан ли колбэк
        if(callback != null) {
            // Проверяем результат: если он не null и не начинается с "Error" - считаем успехом
            if(result != null && !result.startsWith("Error"))
                callback.onCompile(result);  // Уведомляем об успешном выполнении
            else
                callback.onError(result);    // Уведомляем об ошибке
        }
    }
}
