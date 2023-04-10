package io.pifind.mapserver.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class StreamTestClient {

    public static final String TEST_URL = "https://api.openai.com/v1/chat/completions";

    @Test
    public void testStreamTestClient() {
        try {
            URL url = new URL(TEST_URL);
            // URL url = new URL("http://127.0.0.1:9003/v1/test/message");
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setUseCaches(false);
            urlConnection.setRequestProperty("","");
            urlConnection.setRequestProperty("","");
            urlConnection.setRequestProperty("Connection", "Keep-Alive");
            urlConnection.setRequestProperty("Charset", "UTF-8");
            urlConnection.setRequestProperty("Content-Type", "text/event-stream;charset=UTF-8");
            urlConnection.connect();

            InputStream is = new BufferedInputStream(urlConnection.getInputStream());
            this.readStream(is);
        } catch(Exception e) {
            System.out.println("Error:" + e.getMessage());
        }

    }

//    public void output(HttpURLConnection conn) {
//        String data = "HelloNode SSE.";
//        try {
//            byte[] dataBytes = data.getBytes();
//            conn.setRequestProperty("Content-Length", String.valueOf(dataBytes.length));
//            OutputStream os = conn.getOutputStream();
//            os.write(dataBytes);
//            os.flush();
//            os.close();
//        } catch(Exception e) {}
//    }

    public void readStream(InputStream is) {
        BufferedReader reader = null;
        StringBuffer sb = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(is));
            String line = "";
            while((line = reader.readLine()) != null) {
                this.parseMessage(line);
            }
            reader.close();
        } catch(Exception e) {
            System.out.println("Error:" + e.getMessage());
        }
    }

    public void parseMessage(String msg) {
        //这里可以写接收消息后的逻辑
        System.out.println(msg);
    }

}
