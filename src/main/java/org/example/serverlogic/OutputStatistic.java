package org.example.serverlogic;

public class OutputStatistic {

//    Оставлю такой вариант на подумать попозже, может можно обойтись без всех этих доп классов
//    protected Map<String, Map<String, Integer>> statistics;

    private MaxCategory maxCategory;
    private MaxYearCategory maxYearCategory;
    private MaxMonthCategory maxMonthCategory;
    private MaxDayCategory maxDayCategory;

    public MaxCategory getMaxCategory() {
        return maxCategory;
    }

    public MaxYearCategory getMaxYearCategory() {
        return maxYearCategory;
    }

    public MaxMonthCategory getMaxMonthCategory() {
        return maxMonthCategory;
    }

    public MaxDayCategory getMaxDayCategory() {
        return maxDayCategory;
    }

    public void setMaxCategory(MaxCategory maxCategory) {
        this.maxCategory = maxCategory;
    }

    public void setMaxYearCategory(MaxYearCategory maxYearCategory) {
        this.maxYearCategory = maxYearCategory;
    }

    public void setMaxMonthCategory(MaxMonthCategory maxMonthCategory) {
        this.maxMonthCategory = maxMonthCategory;
    }

    public void setMaxDayCategory(MaxDayCategory maxDayCategory) {
        this.maxDayCategory = maxDayCategory;
    }

    protected static class MaxCategory {
        private String category;
        private int sum;

        public String getCategory() {
            return category;
        }

        public int getSum() {
            return sum;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public void setSum(int sum) {
            this.sum = sum;
        }
    }
    protected static class MaxYearCategory extends MaxCategory {
    }
    protected static class MaxMonthCategory extends MaxCategory {
    }
    protected static class MaxDayCategory extends MaxCategory {
    }
}
