package com.unimater.controller;

import com.google.gson.Gson;
import com.unimater.dao.ProductDAO;
import com.unimater.model.Product;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.util.List;

public class ProductHandler implements HttpHandler {
    private final ProductDAO productDAO;
    private final Gson gson = new Gson();

    public ProductHandler(Connection connection) {
        this.productDAO = new ProductDAO(connection);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            List<Product> products = productDAO.getAll();
            String response = gson.toJson(products);
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, response.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        } else if ("POST".equals(exchange.getRequestMethod())) {
            InputStream is = exchange.getRequestBody();
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            Product product = gson.fromJson(body, Product.class);
            productDAO.upsert(product);
            exchange.sendResponseHeaders(201, -1);
        }
    }
}