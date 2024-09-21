import com.sun.net.httpserver.HttpServer;
import com.unimater.controller.*;

import java.io.IOException;
import java.net.InetSocketAddress;
public class Main {
    public static void main(String[] args) {
        try{
        HttpServer servidor = HttpServer.create(
                new InetSocketAddress(8080),0
        );
        servidor.createContext("/HelloWorld",new HelloWorldHandler());
            servidor.createContext("/productType",new ProductTypeHandler(DbConnection.createconnection()));
            servidor.createContext("/product", new ProductHandler(DbConnection.createConnection()));
            servidor.createContext("/sale", new SaleHandler(DbConnection.createConnection()));
            servidor.createContext("/saleItem", new SaleItemHandler(DbConnection.createConnection()));
servidor.setExecutor(null);
        servidor.start();
        System.out.println("servidor rodando na porta 8080h");
    }catch (IOException e){
            System.out.println(e);
}}}