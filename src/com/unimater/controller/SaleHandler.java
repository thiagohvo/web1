package com.unimater.controller;

import com.google.gson.Gson;
import com.unimater.dao.SaleDAO;
import com.unimater.model.Sale;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.util.List;

public class SaleHandler implements HttpHandler {
    private final SaleDAO saleDAO;
    private final Gson gson = new Gson();

    public SaleHandler(Connection connection) {
        this.saleDAO = new SaleDAO(connection);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            List<Sale> sales = saleDAO.getAll();
            String response = gson.toJson(sales);
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, response.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        } else if ("POST".equals(exchange.getRequestMethod())) {
            InputStream is = exchange.getRequestBody();
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            Sale sale = gson.fromJson(body, Sale.class);
            saleDAO.upsert(sale);
            exchange.sendResponseHeaders(201, -1);
        }
    }
}