package com.epam.JDBC;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

class JDBC {
    private ResultSet rs;
    private static final Logger log = LogManager.getRootLogger();
    private Scanner in = new Scanner(System.in);
    private Statement stmt;
    private static int qountColumns;
    private static int fioId;
    private String autor, title, fio, qount, bookId, fioIdStr, output;

    JDBC (Statement stmtMain){
        stmt = stmtMain;
    }

    void addToTable(int table){
        try  {
            if (table == 1) {
                printTable("books","bookId");
                //in.
                log.info("Введите автора:");
                autor = in.nextLine();
                log.info("Введите название книги:");
                title = in.nextLine();
                rs = stmt.executeQuery("SELECT max(bookid) FROM books");
                rs.next();
                int n = Integer.valueOf(rs.getString(1));
                String Nstr = String.valueOf(++n);
                rs.close();
                stmt.executeUpdate("INSERT INTO books (BOOKID,autor,title) VALUES(" + Nstr + ",'" + autor + "'" + ",'" + title + "')");
            } else if (table == 2) {
                printTable("books","bookId");
                printTable("homeLibrary","FIOid");
                printTable("humans","FIOid");
                log.info("Введите id человека:");
                fioIdStr = in.nextLine();
                log.info("Введите количество книг, которое хотите прибавить, или вычесть (с минусом):");
                qount = in.nextLine();
                log.info("Введите id книги:");
                bookId = in.nextLine();
                rs = stmt.executeQuery("SELECT qount FROM homeLibrary WHERE fioId like '" + fioIdStr + "' and bookId=" + bookId);
                if (rs.next()) {
                    qount = String.valueOf((Integer.valueOf(rs.getString(1))) + (Integer.valueOf(qount)));
                    if (Integer.valueOf(qount) <=0){
                        stmt.executeUpdate("DELETE FROM homeLibrary WHERE fioId = "+ fioIdStr + " and bookId = "+ bookId);
                    } else
                    stmt.executeUpdate("UPDATE homeLibrary SET qount = " + qount + "WHERE fioId like '" + fioIdStr + "' and bookId=" + bookId);
                } else {
                    stmt.executeUpdate("INSERT INTO homeLibrary (FIOid,qount,bookid) VALUES(" + fioIdStr + "," + qount + "," + bookId + ")");
                }
                rs.close();
            } else if (table == 3) {
                printTable("humans","FIOid");
                log.info("Введите ФИО:");
                fio = in.nextLine();
                rs = stmt.executeQuery("SELECT FIOid FROM humans WHERE FIO like '" + fio + "'");
                if (rs.next()) {
                    log.info("Такой человек уже добавлен в базу данных");
                    return;
                }
                rs.close();
                rs = stmt.executeQuery("SELECT max(FIOid) FROM humans");
                rs.next();
                fioId = Integer.valueOf(rs.getString(1));
                fioIdStr = String.valueOf(++fioId);
                stmt.executeUpdate("INSERT INTO humans (FIOid,FIO) VALUES(" + fioIdStr + ", '" + fio + "')");
                rs.close();
            } else log.info("Неправильно была введена цифра");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void deleteFromTable(int table){
        try {
            switch (table) {
                case 1:
                    printTable("books", "bookId");
                    log.info("Введите id боок: ");
                    bookId = in.nextLine();
                    stmt.executeUpdate("DELETE FROM homeLibrary WHERE bookId = "+bookId);
                    stmt.executeUpdate("DELETE FROM books WHERE bookId = "+bookId);
                    break;

                case 2:
                    printTable("homeLibrary", "bookId");
                    log.info("Введите id человека: ");
                    fioId = in.nextInt();
                    in.nextLine();
                    log.info("Введите id боок: ");
                    bookId = in.nextLine();
                    stmt.executeUpdate("DELETE FROM homeLibrary WHERE fioId = "+ fioId + " and bookId = "+ bookId);
                    break;

                case 3:
                    printTable("humans", "fioId");
                    log.info("Введите id человека: ");
                    fioId = in.nextInt();
                    in.nextLine();
                    stmt.executeUpdate("DELETE FROM homeLibrary WHERE fioId = "+fioId);
                    stmt.executeUpdate("DELETE FROM humans WHERE fioId = "+ fioId);
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void printTable(String table, String order){
        try {
            output="\n";
            output+="Таблица '"+table+"': \n";
            rs = stmt.executeQuery("SELECT * FROM "+table+" ORDER BY "+order);
            ResultSetMetaData rsmd = rs.getMetaData();
            qountColumns = rsmd.getColumnCount();
            for (int i=1 ; i<=qountColumns ; i++) {
                output +="\t" + rsmd.getColumnName(i) + " | ";
            }
            output+="\n\t------------------------------------\n";
            while (rs.next()) {
                for (int i=1 ; i<qountColumns ; i++){
                    output+="\t" + rs.getString(i) + " : ";
                }
                output+="\t" + rs.getString(qountColumns) + "\n";
            }
            output += "\n";
            log.info(output);
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void makeRequest(String query){
        try {
            output="\n";
            rs = stmt.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            qountColumns = rsmd.getColumnCount();
            while (rs.next()) {
                for (int i = 1; i < qountColumns; i++) {
                    output +=rs.getString(i) + " : ";
                }
                output +=rs.getString(qountColumns) + "\n";
            }
            log.info(output);
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void seeBooks(String fioId) {
        try {
            output="\n";
            output +="Таблица 'человек и его(ее) книги': \n";
            rs = stmt.executeQuery("SELECT humans.fio, books.title, books.autor, qount FROM homelibrary NATURAL JOIN books NATURAL JOIN humans where fioId = "+fioId);
            ResultSetMetaData rsmd = rs.getMetaData();
            qountColumns = rsmd.getColumnCount();
            while (rs.next()) {
                for (int i=1 ; i<qountColumns ; i++){
                    output +=rs.getString(i) + " : ";
                }
                output +=rs.getString(qountColumns) + "\n";
            }
            output += "\n";
            log.info(output);
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void transferBooks(String from, String to, String books, String qountValue) {
        try {
            rs = stmt.executeQuery("SELECT qount FROM homeLibrary WHERE fioId like '" + from + "' and bookId=" + books);
            if (rs.next()) {
                qount = String.valueOf((Integer.valueOf(rs.getString(1))) - (Integer.valueOf(qountValue)));
                if (Integer.valueOf(qount) <=0){
                    stmt.executeUpdate("DELETE FROM homeLibrary WHERE fioId = "+ from + " and bookId = "+ books);
                } else
                    stmt.executeUpdate("UPDATE homeLibrary SET qount = " + qount + "WHERE fioId like '" + from + "' and bookId=" + books);
            } else {
                log.info("У первого человека нет таких книг, либо недостаточное количество");
                return;
            }
            rs.close();

            rs = stmt.executeQuery("SELECT qount FROM homeLibrary WHERE fioId like '" + to + "' and bookId=" + books);
            if (rs.next()) {
                qount = String.valueOf((Integer.valueOf(rs.getString(1))) + (Integer.valueOf(qountValue)));
                stmt.executeUpdate("UPDATE homeLibrary SET qount = " + qount + "WHERE fioId like '" + to + "' and bookId=" + books);
                rs.close();
            } else {
                rs.close();
                stmt.executeUpdate("INSERT INTO homeLibrary (FIOid,qount,bookid) VALUES(" + to + "," + qountValue + "," + books + ")");
            }



        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void printBooksAndHumans() {
        try {
            output="\n";
            log.info("Таблица 'Люди и их книги': \n");
            rs = stmt.executeQuery("SELECT fioid, humans.fio, 'id_book=' || bookid, books.title, books.autor, 'qount=' || qount FROM homelibrary NATURAL JOIN books NATURAL JOIN humans ORDER BY fioId");
            ResultSetMetaData rsmd = rs.getMetaData();
            qountColumns = rsmd.getColumnCount();
            while (rs.next()) {
                for (int i=1 ; i<qountColumns ; i++){
                    output +="\t"+rs.getString(i) + " : ";
                }
                output +=rs.getString(qountColumns)+"\n";
            }
            output += "\n";
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
