package org.example.serverlogic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.*;

public class MaxCategoryCalc implements Serializable {

    private static final long serialVersionUID = 108L;

    // Для хранения списка категорий и соответствующих им товаров удобно использовать коллекцию HashSet
    // для быстрого поиска в ней соответствий, ведь в реальности данный список может быть гораздо больше 8 строк
    // При этом порядок элементов не важен абсолютно
    protected static Set<CategoryOfGoods> categoriesOfGoods = new HashSet<>();

    // В эту мапу запишем категорию в качестве ключа, и данные о покупках в список,
    // прикрепленный к каждой категории, например
    // <еда>, <курица, 2022.09.20, 300; сухарики, 2022.09.21, 100> итд
    // <одежда>, <тапки, 2022.10.01, 500; шапка, 2022.10.05, 800> итд
    protected Map<String, List<IncomingPurchase>> purchasesByCategories = new HashMap<>();

    public void readFromTsv(File tsvFile) {
        try (BufferedReader loadCatFromFile = new BufferedReader(new FileReader(tsvFile))) {
            // заводим переменную для чтения из файла, т.к. при упоминании метода в скобках
            // цикла / ифа он выполняется,
            // итого без переменной у нас каждая вторая итерация цикла останется без данных
            String s;
            while (true) {
                if ((s = loadCatFromFile.readLine()) == null) break;
                String[] split = s.split(" |\t");
                CategoryOfGoods c = new CategoryOfGoods();
                c.setCategory(split[2]);
                c.setGoods(split[1]);
                categoriesOfGoods.add(c);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    // Этим методом присваиваем категории входящим покупкам, далее передаем эти категории вместе с
    // суммой покупки в следующий метод, производящий основной расчет наибольшей категории
    public String assignCategory(IncomingPurchase purchase) {
        for (CategoryOfGoods c : categoriesOfGoods) {
            if (c.getGoods().equals(purchase.getTitle())) {
                return c.getCategory();
            }
        }
        return "другое";
    }

    // JSON это формат файла, но по сути содержит в себе тот же String, только строго типизированный, то
    // есть по сути txt, написанный по определенным правилам форматирования
    // Для подсчета трат по категориям за выбранный период напишем метод, который будет принимать информацию,
    // включая дату покупки. Дату мы получим, используя класс для парсинга входящей покупки.
    public String statisticsForPeriod(String category, IncomingPurchase purchase) { // <еда>, <курица, 2022.09.20, 300>
        // GsonBuilder отдает нам форматированный в JSON String
        // Для того чтобы получить данные для создания JSON объекта,
        // нужны соответствующие классы (в OutputStatistics)
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gsonOfMaxCategories = gsonBuilder.create();
        OutputStatistic statistic = new OutputStatistic();
        String maxCategory = "";
        int maxValue = -1;
        String maxYearCategory = "";
        int maxYearValue = -1;
        String maxMonthCategory = "";
        int maxMonthValue = -1;
        String maxDayCategory = "";
        int maxDayValue = -1;
        List<IncomingPurchase> purchaseList = new ArrayList<>();
        GregorianCalendar inputDate = purchase.getDateFromString(); // извлекаем дату у входящей покупки
        // Нам нужны мапы под каждый вариант выбранной статистики, принимающие в качестве ключа категорию
        // товара, в качестве значения - максимальные траты по категории за период
        Map<String, Integer> spendsTotal = new HashMap<>();
        Map<String, Integer> spendsByYear = new HashMap<>();
        Map<String, Integer> spendsByMonth = new HashMap<>();
        Map<String, Integer> spendsByDay = new HashMap<>();
        if (!purchasesByCategories.containsKey(category)) {
            purchaseList.add(purchase);
            purchasesByCategories.put(category, purchaseList);
        } else { // в случае true
            List<IncomingPurchase> value = purchasesByCategories.get(category); // Вытащим список всего что сохранено по этому ключу (всю еду)
            value.add(purchase); // Добавим в список <курица, 2022.09.20, 300>
            purchasesByCategories.put(category, value); // добавим в мапу <еда>, <обновленный список еды>
        }
        for (String key : purchasesByCategories.keySet()) { // перебираем сохраненные категории по одной
            List<IncomingPurchase> value = purchasesByCategories.get(key); // из каждой достаем список позиций (например всю еду в виде <курица, 2022.09.20, 300>)
            for (IncomingPurchase dateFromList : value) { // заходим внутрь списка категории (еды)
                if (!spendsTotal.containsKey(key)) {
                    spendsTotal.put(key, dateFromList.getSum());
                } else {
                    int totalValue = spendsTotal.get(key);
                    totalValue += dateFromList.getSum();
                    spendsTotal.put(key, totalValue);
                }
                GregorianCalendar mappedDate = dateFromList.getDateFromString(); // извлекаем даты всех покупок
                // Отбираем покупки из списка, сделанных в тот же год, что и входящая покупка
                if (inputDate.get(Calendar.YEAR) == mappedDate.get(Calendar.YEAR)) {
                    // суммируем стоимость покупок за выбранный год
                    if (!spendsByYear.containsKey(key)) {
                        spendsByYear.put(key, dateFromList.getSum());
                    } else {
                        int v = spendsByYear.get(key);
                        v += dateFromList.getSum();
                        spendsByYear.put(key, v);
                    }
                    if (inputDate.get(Calendar.MONTH) == mappedDate.get(Calendar.MONTH)) {
                        if (!spendsByMonth.containsKey(key)) {
                            spendsByMonth.put(key, dateFromList.getSum());
                        } else {
                            int v = spendsByMonth.get(key);
                            v += dateFromList.getSum();
                            spendsByMonth.put(key, v);
                        }
                        if (inputDate.get(Calendar.DAY_OF_MONTH) == mappedDate.get(Calendar.DAY_OF_MONTH)) {
                            if (!spendsByDay.containsKey(key)) {
                                spendsByDay.put(key, dateFromList.getSum());
                            } else {
                                int v = spendsByDay.get(key);
                                v += dateFromList.getSum();
                                spendsByDay.put(key, v);
                            }
                        }
                    }
                }
            }
            for (String totalKey : spendsTotal.keySet()) {
                int totalValue = spendsTotal.get(totalKey);
                if (totalValue > maxValue) {
                    maxValue = totalValue;
                    maxCategory = totalKey;
                }
            }
            for (String yearKey : spendsByYear.keySet()) {
                int yearValue = spendsByYear.get(yearKey);
                if (yearValue > maxYearValue) {
                    maxYearValue = yearValue;
                    maxYearCategory = yearKey;
                }
            }
            for (String monthKey : spendsByMonth.keySet()) {
                int monthValue = spendsByMonth.get(monthKey);
                if (monthValue > maxMonthValue) {
                    maxMonthValue = monthValue;
                    maxMonthCategory = monthKey;
                }
            }
            for (String dayKey : spendsByDay.keySet()) {
                int dayValue = spendsByDay.get(dayKey);
                if (dayValue > maxDayValue) {
                    maxDayValue = dayValue;
                    maxDayCategory = dayKey;
                }
            }
            OutputStatistic.MaxCategory maxStat = new OutputStatistic.MaxCategory();
            maxStat.setCategory(maxCategory);
            maxStat.setSum(maxValue);

            OutputStatistic.MaxYearCategory maxYearStat = new OutputStatistic.MaxYearCategory();
            maxYearStat.setCategory(maxYearCategory);
            maxYearStat.setSum(maxYearValue);

            OutputStatistic.MaxMonthCategory maxMonthStat = new OutputStatistic.MaxMonthCategory();
            maxMonthStat.setCategory(maxMonthCategory);
            maxMonthStat.setSum(maxMonthValue);

            OutputStatistic.MaxDayCategory maxDayStat = new OutputStatistic.MaxDayCategory();
            maxDayStat.setCategory(maxDayCategory);
            maxDayStat.setSum(maxDayValue);

            statistic.setMaxCategory(maxStat);
            statistic.setMaxYearCategory(maxYearStat);
            statistic.setMaxMonthCategory(maxMonthStat);
            statistic.setMaxDayCategory(maxDayStat);
        }
        return gsonOfMaxCategories.toJson(statistic);
    }
}