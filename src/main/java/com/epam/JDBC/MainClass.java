package com.epam.JDBC;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class MainClass {
    private static final Logger log = LogManager.getRootLogger();

    public static void main(String[] args) {
        int table;
        String query, FIOid, from, to, books, qount;
        Connection connection = null;
        try {
            connection = getConnection();       // подсоединение к бд

        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            assert connection != null;
            Statement stmt = connection.createStatement();
            JDBC jdbc = new JDBC(stmt);
            for (; ; ) {
                log.info( "\n\n1 : Изменить/добавить строку в таблицу"
                        + "\n2 : Удалить строку из таблицы"
                        + "\n3 : Вывести таблицу"
                        + "\n4 : Ввести запрос самому"
                        + "\n5 : Вывести все таблицы"
                        + "\n6 : Вывести книги и их количество определенного человека"
                        + "\n7 : Передать книги одного человека другому"
                        + "\n0 : Выход"
                        + "\nВведите пункт:\n\n");
                Scanner in = new Scanner(System.in);
                int num = in.nextInt();
                in.nextLine();
                switch (num) {
                    case 1:
                        log.info("В какую таблицу добавить? Введите цифру < 1:books | 2:homeLibrary | 3:humans>");
                        table = in.nextInt();
                        in.nextLine();
                        jdbc.addToTable(table);
                        break;

                    case 2:
                        log.info("Из какой таблицы хотите удалить строку? Введите цифру < 1:books | 2:homeLibrary | 3:humans>");
                        table = in.nextInt();
                        in.nextLine();
                        jdbc.deleteFromTable(table);
                        break;

                    case 3:
                        log.info("Какую таблицу вывести? Введите цифру < 1:books | 2:homeLibrary | 3:humans>");
                        table = in.nextInt();
                        in.nextLine();
                        switch (table) {
                            case 1:
                                jdbc.printTable("books","bookId");
                                break;
                            case 2:
                                jdbc.printTable("homeLibrary","FIOid");
                                break;
                            case 3:
                                jdbc.printTable("humans","FIOid");
                                break;
                        }
                        break;

                    case 4:
                        in.nextLine();
                        log.info("Введите запрос:");
                        query = in.nextLine();
                        jdbc.makeRequest(query);
                        break;

                    case 5:
                        jdbc.printTable("books","bookId");
                        jdbc.printTable("homeLibrary","FIOid");
                        jdbc.printTable("humans","FIOid");
                        break;

                    case 6:
                        jdbc.printTable("humans","FIOid");
                        log.info("Введите id человека, книги которого хотите посмотреть:");
                        FIOid=in.nextLine();
                        jdbc.seeBooks(FIOid);
                        break;

                    case 7:
                        jdbc.printBooksAndHumans();
                        log.info("От какого человека передать:");
                        from = in.nextLine();
                        log.info("Кому:");
                        to = in.nextLine();
                        log.info("Каких книг (id книги):");
                        books = in.nextLine();
                        log.info("Сколько:");
                        qount = in.nextLine();
                        jdbc.transferBooks(from, to, books, qount);
                        break;

                    case 0:
                        stmt.close();
                        System.exit(0);
                        break;

                    default:
                        log.info("Неизвестный ввод");
                        break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static Connection getConnection() throws SQLException {
        Properties props = new Properties();
        try (InputStream input = new FileInputStream("src\\main\\resources\\JDBC.properties")) {
            props.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String URL = "JDBC:oracle:thin:@localhost:1521:xe";
        String USER_NAME = props.getProperty("user");
        String PASSWORD = props.getProperty("password");
        return DriverManager.getConnection(URL, USER_NAME, PASSWORD);
    }

}
