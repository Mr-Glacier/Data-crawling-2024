package zedata.com;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        String firstUrl = "https://www.youxinpai.com/trade/getTradeList";


        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(firstUrl);

            request.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/125.0.0.0 Safari/537.36");
            request.setHeader("Referer", "https://www.youxinpai.com/trade");
            request.setHeader("Content-Type", "application/json; charset=utf-8");
            request.setHeader("Cookie", "xxzlclientid=7a923008-bbf8-4b78-b1f2-1717568308111; xxzlxxid=pfmxwtDfh9JN7gAwBs330bDz99xCin6xXlLaiBRy0BNp5uBTsaB9cJPDll/1KrKTlE4J; id58=CocLxWZidCqUywmePIHHAg==; xxzlbbid=pfmbM3wxMDI5M3wxLjcuMHwxNzIzMTgyMDQ4MjIxfEFxQnkwc01xSGlwUmtDekQrbE9tSGhmTmRJNXMzT2h1N2Iza2NPSllSaFE9fGQ4YTY5NTExMDRhYWVlZWZkNjkwZTNmZTk1ZWM3ZTY0XzE3MjMxNzI5ODA0ODBfNWFlYzc2YzRlOTcxNDg1MjhiMzUyM2JmMmUzM2Y0MTJfMTk3NDQ0NzIwMXxiY2NmZDIxYzAxM2U0MjI5MWE2NmIwOGI1OTdkNDQwNl8xNzIzMTgyMDQ3NDI0XzEzOA==; csrfToken_key=_1n9o0kbmXhT067g_o8xe7bq; csrfToken=Y9JZsBYL-bQxkGxpwdlV_CuYaeIvyjSMumwY; jwt_token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjoiWTlKWnNCWUwtYlF4a0d4cHdkbFZfQ3VZYWVJdnlqU011bXdZIiwiaWF0IjoxNzIzMTg1MjA2LCJleHAiOjE3MjMxODcwMDZ9.Thq-BCD1rd6v5WiMfTmbV1rvggmOIx8KQFgr7z2dJmI");
            request.setHeader("priority", "u=1, i");
            request.setHeader("Origin", "https://www.youxinpai.com");

            // 1. 传递 JSON 数据
            String parmStr = "{\"entities\":\"{\\\"req\\\":{\\\"cityIds\\\":[],\\\"serialIds\\\":[],\\\"appearanceGrades\\\":[],\\\"skeletonGrades\\\":[],\\\"interiorGrades\\\":[],\\\"emissionStandards\\\":[],\\\"carPriceLevel\\\":[],\\\"carYearLevel\\\":[],\\\"carGearbox\\\":[],\\\"carOwners\\\":[],\\\"carUseTypes\\\":[],\\\"fuelTypes\\\":[],\\\"conditionPriceType\\\":[],\\\"transferCounts\\\":[],\\\"startPriceType\\\":[],\\\"isNotBubbleCar\\\":false,\\\"isNotBurnCar\\\":false,\\\"isNotSmallReport\\\":false,\\\"orderFields\\\":10},\\\"page\\\":[{\\\"page\\\":1,\\\"pageSize\\\":2,\\\"pageTab\\\":\\\"pc_circle\\\"},{\\\"page\\\":1,\\\"pageSize\\\":15,\\\"pageTab\\\":\\\"immediately\\\"},{\\\"page\\\":1,\\\"pageSize\\\":2,\\\"pageTab\\\":\\\"delay\\\"},{\\\"page\\\":1,\\\"pageSize\\\":2,\\\"pageTab\\\":\\\"fixedPrice\\\"},{\\\"page\\\":1,\\\"pageSize\\\":2,\\\"pageTab\\\":\\\"benz\\\"},{\\\"page\\\":1,\\\"pageSize\\\":2,\\\"pageTab\\\":\\\"attention\\\"}]}\"}";
            StringEntity stringEntity = new StringEntity(parmStr, "UTF-8");
            request.setEntity(stringEntity);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                System.out.println("Response Code: " + response.getStatusLine().getStatusCode());

                Header[] headers = response.getAllHeaders();
                for (Header header : headers) {
                    System.out.println(header.getName() + ": " + header.getValue());
                }

                String responseBody = EntityUtils.toString(response.getEntity());
                System.out.println("Response Body: " + responseBody);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void AnalsistToken(){
//        String crestToken = "JX7lMFuc-C2itCZHMeqzjx_yJrSKuLV85pzQ";
//
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("data",crestToken);
//        // 获取时间戳 2024-08-06 11:09:13
//        long unixTimestamp = Instant.now().getEpochSecond();
//
//        // 获取当前时间的Unix时间戳（秒）
//        Instant now = Instant.now();
//
//        // 在当前时间上加上20分钟
//        Instant futureTime = now.plus(Duration.ofMinutes(20));
//        jsonObject.put("iat","1722935771");
//        jsonObject.put("exp","1722937571");
//        System.out.println(jsonObject);
//
//
//
//        // 创建Base64编码器
//        Base64.Encoder encoder = Base64.getEncoder();
//
//        String headerStr = "{\"alg\":\"none\",\"typ\":\"JWT\"}";
//        // 对字符串进行编码
//        String encodedHeader = encoder.encodeToString(headerStr.getBytes());
//        String encodedPayload = encoder.encodeToString(jsonObject.toJSONString().getBytes());
//
//        String jwt = encodedHeader + "." + encodedPayload+ ".";
//
//        // Hs256加密 密钥 10293
//
//
//        System.out.println(jwt);
//        Map<String, Object> header = new HashMap<String, Object>();
//        header.put("alg", "HS256");
//        header.put("typ", "JWT");

//        String jwtqianming = JwtUntil.getToken(header,jsonObject.toString(),"10293");
//        System.out.println(jwtqianming);
    }

}