//package org.joychou.controller;
//
//public class LFI {
//
//}
////
//// Source code recreated from a .class file by IntelliJ IDEA
//// (powered by FernFlower decompiler)
////
//
//package com.test2.aaa1;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.URL;
//import java.net.URLConnection;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//public class testURL extends HttpServlet {
//    public testURL() {
//    }
//
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        this.doPost(req, resp);
//    }
//
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String tartget_url = req.getParameter("url");
//        String pri = tartget_url.substring(0, tartget_url.indexOf(":"));
//        if (pri.matches("(?i)file|(?i)gopher|(?i)data")) {
//            resp.getWriter().write(String.valueOf((new StringBuilder()).append("false")));
//        } else {
//            resp.getWriter().write(String.valueOf(this.getContent(tartget_url)));
//        }
//
//    }
//
//    public StringBuilder getContent(String url) throws IOException {
//        URL urL = new URL(url);
//        URLConnection con = urL.openConnection();
//        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//        StringBuilder content = new StringBuilder();
//
//        String inputLine;
//        while((inputLine = in.readLine()) != null) {
//            content.append(inputLine);
//            content.append("\n");
//        }
//
//        return content;
//    }
//}
