package com.ygsoft.matcloud.piloader.util;

import java.io.*;
import java.net.*;

public class HttpUtil {

	public HttpUtil() {
	}

	public static String sendGet(String url, String param){
        String result;
        BufferedReader in;
        result = "";
        in = null;
        try{
            String urlNameString = (new StringBuilder(String.valueOf(url))).append("?").append(param).toString();
            URL realUrl = new URL(urlNameString);
            URLConnection connection = realUrl.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.connect();
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while((line = in.readLine()) != null) 
                result = (new StringBuilder(String.valueOf(result))).append(line).toString();
        }
        catch(Exception e){
            System.out.println((new StringBuilder("\u53D1\u9001GET\u8BF7\u6C42\u51FA\u73B0\u5F02\u5E38\uFF01")).append(e).toString());
            e.printStackTrace();
        }
        try{
            if(in != null)
                in.close();
        }
        catch(Exception e2){
            e2.printStackTrace();
        }
        return result;
    }

	public static String sendPost(String url, String param){
        PrintWriter out;
        BufferedReader in;
        String result;
        out = null;
        in = null;
        result = "";
        try{
            URL realUrl = new URL(url);
            URLConnection conn = realUrl.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            out = new PrintWriter(conn.getOutputStream());
            out.print(param);
            out.flush();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while((line = in.readLine()) != null) 
                result = (new StringBuilder(String.valueOf(result))).append(line).toString();
        }
        catch(Exception e){
            System.out.println((new StringBuilder("\u53D1\u9001POST\u8BF7\u6C42\u51FA\u73B0\u5F02\u5E38\uFF01")).append(e).toString());
            e.printStackTrace();
        }
        try{
            if(out != null)
                out.close();
            if(in != null)
                in.close();
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
       
        return result;
    }

	public static String jsonPostString(String strURL, String params){
        OutputStreamWriter out;
        BufferedReader in;
        String result;
        out = null;
        in = null;
        result = "";
        try{
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.connect();
            out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            out.append(params);
            out.flush();
            out.close();
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while((line = in.readLine()) != null) 
                result = (new StringBuilder(String.valueOf(result))).append(line).toString();
        }
        catch(IOException e){
            System.out.println((new StringBuilder("\u53D1\u9001POST\u8BF7\u6C42\u51FA\u73B0\u5F02\u5E38\uFF01")).append(e).toString());
            e.printStackTrace();
        }
        try{
            if(out != null)
                out.close();
            if(in != null)
                in.close();
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
       
        return result;
    }
}