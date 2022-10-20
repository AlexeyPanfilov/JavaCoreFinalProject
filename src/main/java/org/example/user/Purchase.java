package org.example.user;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.Socket;

public class Purchase {

    protected String title;
    protected String date;
    protected int sum;

    public Purchase(String title, String date, int sum) {
        this.title = title;
        this.date = date;
        this.sum = sum;
    }

    // Описываем сохранение объекта в JSON формат и отправку его серверу.
    // Создавать физически файл на диске не нужно
    public String convertToJson() {
            // Здесь напишем сохранение объекта в JSON
//            GsonBuilder gsonBuilder = new GsonBuilder();
//            Gson gson = gsonBuilder.create();
            return "{\"title\": \"" + title + "\", \"date\": \"" +
                    date + "\", \"sum\": " + sum + "}";
            // серверу в итоге передаем по сути форматированный "под JSON" String
        // но лучше всё же JSON объект сохранить во избежание неверного форматирования стринга

    }

    public String toString() {
        return "{\"title\": \"" + title + "\", \"date\": \"" + date + "\", \"sum\": " + sum + "}";
    }
}