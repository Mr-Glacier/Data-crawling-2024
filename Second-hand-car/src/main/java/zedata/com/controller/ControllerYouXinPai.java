package zedata.com.controller;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.util.Map;
import java.util.Random;

/**
 * @author Mr-Glacier
 * @ApiNote  优信拍数据获取
 */
public class ControllerYouXinPai {

    /**
     * 获取优信拍的第一个页面
     */
    public String methodDownFirstHtml(String firstUrl, String parmStr) {
        String result = "";
        try {
            Connection.Response res = Jsoup.connect(firstUrl).method(Connection.Method.POST)
                    .header("Content-Type", "application/json; charset=utf-8")
                    .header("Referer", "https://www.youxinpai.com/trade")
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/125.0.0.0 Safari/537.36")
                    .header("X-Requested-With", "XMLHttpRequest")
                    .header("priority", "u=1, i")
                    .cookie("Cookie", "xxzlclientid=7a923008-bbf8-4b78-b1f2-1717568308111; xxzlxxid=pfmxwtDfh9JN7gAwBs330bDz99xCin6xXlLaiBRy0BNp5uBTsaB9cJPDll/1KrKTlE4J; id58=CocLxWZidCqUywmePIHHAg==; xxzlbbid=pfmbM3wxMDI5M3wxLjcuMXwxNzIyOTMzNjA1MjkyfDkrdUVRS3RzN1ZRZmRPNHNkUllMR2xzNmFOd0FwMTV2QkswVFpjSG9nTWs9fDhhMWI1MDE3YWY1Y2Y0YmU1ZmI4N2FlM2I2NzIzYzU3XzE3MjI5MTEwNDg0NDZfMjY5YjJiMTM4MmEzNDU4YmExMDAxMDAwMjY3YjhjNjJfMTk3NDQ0NzIwMXw1YjU1NDYwMGIxNGYxNTA4Y2UwZGE2ODQxNWU4OTU2YV8xNzIyOTMzNjAyNjkxXzEzOQ==; csrfToken_key=nKugnIB80Rd5kzObXepmV_I8; csrfToken=JX7lMFuc-C2itCZHMeqzjx_yJrSKuLV85pzQ; jwt_token=eyJhbGciOiJub25lIiwidHlwIjoiSldUIn0=.eyJkYXRhIjoiSlg3bE1GdWMtQzJpdENaSE1lcXpqeF95SnJTS3VMVjg1cHpRIiwiZXhwIjoiMTcyMjkzNzU3MSIsImlhdCI6IjE3MjI5MzU3NzEifQ==.")
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .requestBody(parmStr)
                    .execute();
            System.out.println(res.statusCode());
            System.out.println(res.body());
            // 获取所有响应头
            Map<String, String> headers = res.headers();
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }

            result = res.body();
            Random random = new Random();
            int sleepTime = (random.nextInt(5) + 1) * 1000;
            Thread.sleep(sleepTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
