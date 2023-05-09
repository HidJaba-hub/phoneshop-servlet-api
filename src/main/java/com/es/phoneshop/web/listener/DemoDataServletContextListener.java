package com.es.phoneshop.web.listener;

import com.es.phoneshop.model.entity.PriceHistory;
import com.es.phoneshop.model.entity.Product;
import com.es.phoneshop.service.CustomProductService;
import com.es.phoneshop.service.ProductService;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Currency;
import java.util.Date;
import java.util.List;

public class DemoDataServletContextListener implements ServletContextListener {
    private final Currency usd = Currency.getInstance("USD");

    private ProductService productService;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        this.productService = CustomProductService.getInstance();

        boolean insertDemoData = Boolean.valueOf(event.getServletContext().getInitParameter("insertDemoData"));

        if (insertDemoData) {
            saveSampleProducts();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

    private void saveSampleProducts() {
        productService.saveProduct(new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg", generateRandomPriceHistory()));
        productService.saveProduct(new Product("sgs2", "Samsung Galaxy S II", new BigDecimal(200), usd, 2, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg", generateRandomPriceHistory()));
        productService.saveProduct(new Product("sgs3", "Samsung Galaxy S III", new BigDecimal(300), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg", generateRandomPriceHistory()));
        productService.saveProduct(new Product("iphone", "Apple iPhone", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg", generateRandomPriceHistory()));
        productService.saveProduct(new Product("iphone6", "Apple iPhone 6", new BigDecimal(1000), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg", generateRandomPriceHistory()));
        productService.saveProduct(new Product("sec901", "Sony Ericsson C901", new BigDecimal(420), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Ericsson%20C901.jpg", generateRandomPriceHistory()));
        productService.saveProduct(new Product("xperiaxz", "Sony Xperia XZ", new BigDecimal(120), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Xperia%20XZ.jpg", generateRandomPriceHistory()));
        productService.saveProduct(new Product("nokia3310", "Nokia 3310", new BigDecimal(70), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Nokia/Nokia%203310.jpg", generateRandomPriceHistory()));
        productService.saveProduct(new Product("palmp", "Palm Pixi", new BigDecimal(170), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Palm/Palm%20Pixi.jpg", generateRandomPriceHistory()));
        productService.saveProduct(new Product("simc56", "Siemens C56", new BigDecimal(70), usd, 20, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C56.jpg", generateRandomPriceHistory()));
        productService.saveProduct(new Product("simc61", "Siemens C61", new BigDecimal(80), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C61.jpg", generateRandomPriceHistory()));
        productService.saveProduct(new Product("htces4g", "HTC EVO Shift 4G", new BigDecimal(320), usd, 3, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/HTC/HTC%20EVO%20Shift%204G.jpg", generateRandomPriceHistory()));
        productService.saveProduct(new Product("simsxg75", "Siemens SXG75", new BigDecimal(150), usd, 40, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg", generateRandomPriceHistory()));
    }

    private List<PriceHistory> generateRandomPriceHistory() {
        List<PriceHistory> priceHistoryList = new ArrayList<>();
        int priceHistoryLength = createRandomIntBetween(1, 10);

        for (int i = 0; i < priceHistoryLength; i++) {
            Date date = Date.from(createRandomDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
            BigDecimal price = new BigDecimal(createRandomIntBetween(100, 5000));
            priceHistoryList.add(new PriceHistory(date, usd, price));
        }
        priceHistoryList.sort(Comparator.comparing(PriceHistory::getDate));
        return priceHistoryList;
    }

    private int createRandomIntBetween(int start, int end) {
        return start + (int) Math.round(Math.random() * (end - start));
    }

    private LocalDate createRandomDate() {
        int day = createRandomIntBetween(1, 28);
        int month = createRandomIntBetween(1, 12);
        int year = createRandomIntBetween(2000, 2023);
        return LocalDate.of(year, month, day);
    }
}
