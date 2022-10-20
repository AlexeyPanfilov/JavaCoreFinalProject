import org.example.serverlogic.IncomingPurchase;
import org.example.serverlogic.MaxCategoryCalc;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class MaxCategoryCalcTest {

    IncomingPurchase purchase = new IncomingPurchase();
    MaxCategoryCalc categoryCalc = new MaxCategoryCalc();

    @Test
    @DisplayName("Тест присвоения категории")
    void assignCategory() {
        File tsvFile = new File("categories.tsv");
        categoryCalc.readFromTsv(tsvFile);
        String s = "{\"title\": \"булка\", \"date\": \"2022.3.12.\", \"sum\": 100}";
        purchase.splitJson(s);
        Assertions.assertEquals("еда", categoryCalc.assignCategory(purchase));
    }

    @Test
    @DisplayName("Тест выбора максимальных категорий")
    void maxCalc () {
        String  maxCategory;
        purchase.splitJson("{\"title\": \"булка\", \"date\": \"2022.3.12.\", \"sum\": 100}");
        maxCategory = categoryCalc.statisticsForPeriod("еда", purchase);
        Assertions.assertEquals("{\"maxCategory\":{\"category\":\"еда\",\"sum\":100},\"maxYearCategory\":{\"category\":\"еда\",\"sum\":100},\"maxMonthCategory\":{\"category\":\"еда\",\"sum\":100},\"maxDayCategory\":{\"category\":\"еда\",\"sum\":100}}", maxCategory);
    }
}
