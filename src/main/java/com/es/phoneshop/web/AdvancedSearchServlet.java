package com.es.phoneshop.web;

import com.es.phoneshop.enums.SearchCriteria;
import com.es.phoneshop.model.entity.Product;
import com.es.phoneshop.service.product.CustomProductService;
import com.es.phoneshop.service.product.ProductService;
import com.es.phoneshop.validators.DefaultParseValidator;
import com.es.phoneshop.validators.ParseValidator;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.maven.shared.utils.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AdvancedSearchServlet extends HttpServlet {

    private ProductService productService;
    private ParseValidator parseValidator;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productService = CustomProductService.getInstance();
        parseValidator = DefaultParseValidator.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String> errors = new HashMap<>();
        List<Product> products = new ArrayList<>();

        SearchCriteria searchCriteria = Optional.ofNullable(request.getParameter("searchCriteria"))
                .map(SearchCriteria::valueOf).orElse(null);

        String description = request.getParameter("query");
        BigDecimal minPrice = parsePrice(request, "minPrice", errors);
        BigDecimal maxPrice = parsePrice(request, "maxPrice", errors);

        if (errors.isEmpty() && searchCriteria != null) {
            products = productService.getProductsByQuery(description, minPrice, maxPrice, searchCriteria);
            request.setAttribute("message", products.size() + " products were found");
        } else {
            request.setAttribute("errors", errors);
        }

        request.setAttribute("products", products);
        request.setAttribute("searchCriteria", SearchCriteria.getSearchCriteria());
        request.getRequestDispatcher("/WEB-INF/pages/advancedSearch.jsp").forward(request, response);
    }

    private BigDecimal parsePrice(HttpServletRequest request, String priceParameter, Map<String, String> errors) {
        String value = request.getParameter(priceParameter);
        BigDecimal price = BigDecimal.ZERO;
        if (StringUtils.isEmpty(value)) {
            if (priceParameter.equals("maxPrice")) {
                price = new BigDecimal(Double.MAX_VALUE);
            }
            return price;
        } else {
            parseValidator.validatePrice(value, errors, priceParameter);
        }
        if (!errors.containsKey(priceParameter)) {
            try {
                price = BigDecimal.valueOf((Long) DecimalFormat.getInstance().parse(value));
            } catch (ParseException e) {
                errors.put(priceParameter, "Parse error");
            } catch (ClassCastException e) {
                errors.put(priceParameter, "Too long value");
            }
        }
        return price;
    }
}
