package com.podarbetweenus.Utility;

import android.database.Cursor;

public class CloseHelper {
    public static void close(Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }
}
