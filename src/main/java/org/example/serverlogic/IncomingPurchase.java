package org.example.serverlogic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class IncomingPurchase {
    protected String title;
    protected String date;
    protected int sum;
    protected GregorianCalendar dateFromString = new GregorianCalendar();

    public void splitJson (String incomingData) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        IncomingPurchase incomingPurchase = gson.fromJson(incomingData, IncomingPurchase.class);
        String[] splitDate = incomingPurchase.date.split("\\.");
        dateFromString.set(Calendar.YEAR, Integer.parseInt(splitDate[0]));
        dateFromString.set(Calendar.MONTH, Integer.parseInt(splitDate[1]));
        dateFromString.set(Calendar.DAY_OF_MONTH, Integer.parseInt(splitDate[2]));
//        System.out.println("title: " + incomingPurchase.title +
//                " date: " + dateFromString.get(Calendar.YEAR) + ".0" +
//                dateFromString.get(Calendar.MONTH) + ".0" +
//                dateFromString.get(Calendar.DAY_OF_MONTH) + " sum: " + incomingPurchase.sum);
        this.title = incomingPurchase.title;
        //this.dateFromString = incomingPurchase.dateFromString;
        this.sum = incomingPurchase.sum;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public void setDateFromString(GregorianCalendar date) {
        this.dateFromString = dateFromString;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public int getSum() {
        return sum;
    }

    public GregorianCalendar getDateFromString() {
        return dateFromString;
    }

    @Override
    public String toString() {
        return "IncomingPurchase{" +
                "title='" + title + '\'' +
                ", sum=" + sum +
                ", dateFromString=" + dateFromString +
                '}';
    }
}
