package com.unimater.controller;

import com.google.gson.Gson;
import com.unimater.dao.SaleItemDAO;
import com.unimater.model.SaleItem;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.util.List;

public class SaleItemHandler implements HttpHandler {
    private final SaleItemDAO saleItemDAO;
    private final Gson gson = new Gson();

    public SaleItemHandler(Connection connection) {
        this.saleItemDAO = new SaleItemDAO(connection);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            List<SaleItem> saleItems = saleItemDAO.getAll();
            String response = gson.toJson(saleItems);
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, response.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        } else if ("POST".equals(exchange.getRequestMethod())) {
            InputStream is = exchange.getRequestBody();
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            SaleItem saleItem = gson.fromJson(body, SaleItem.class);
            saleItemDAO.upsert(saleItem);
            exchange.sendResponseHeaders(201, -1);
        }
    }
}