package com.example.camera_zakurin.domains;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

/**
 * Класс для управления разрешениями (permissions) в приложении
 * Обеспечивает проверку и запрос разрешений, необходимых для работы приложения
 */
public class PermissionManager {
    // Запрашивает разрешение на использование камеры, если оно еще не предоставлено
    public static void GetPermission(Context context, Activity activity) {
        // Проверяем, выдано ли разрешение на использование камеры
        if(CheckPermisson(context) == PackageManager.PERMISSION_DENIED) {
            // Если разрешение не выдано, запрашиваем его у пользователя
            ActivityCompat.requestPermissions(
                    activity,                          // Активность, которая будет обрабатывать результат
                    new String[]{                      // Массив запрашиваемых разрешений
                            Manifest.permission.CAMERA // Запрашиваем разрешение на использование камеры
                    }, 1                               // Код запроса (используется для идентификации в onRequestPermissionsResult)
            );
        }
    }
    // Проверяет, выдано ли разрешение на использование камеры
    public static int CheckPermisson(Context context) {
        // Возвращаем статус разрешения на использование камеры
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
    }
}
