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
        purchase.setTitle("булка");
        Assertions.assertEquals("еда", categoryCalc.assignCategory(purchase));
    }

    @Test
    @DisplayName("Тест выбора максимальной категории")
    void categoriesCount () {
        Map<String, Integer> categoriesAndSpends = new HashMap<>();
        categoriesAndSpends.put("еда", 700);
        categoriesAndSpends.put("другое", 300);
        categoriesAndSpends.put("одежда", 600);
        String maxCategory = "";
        for (String key : categoriesAndSpends.keySet()) {
            maxCategory = categoryCalc.categoriesCount(key, categoriesAndSpends.get(key));
        }
        Assertions.assertEquals("{\"maxCategory\": {\"category\": \"еда\",\"sum\": 700}}", maxCategory);
    }
}
