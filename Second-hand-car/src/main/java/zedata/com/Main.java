package zedata.com;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import zedata.com.until.JwtUntil;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
//        String date = String.valueOf(System.currentTimeMillis());
//        // 输出yyyyMMdd 的时间
//        date = date.substring(0, 8);
//        String currentDateStr = DateUtil.format(DateUtil.date(), "yyyyMMdd_HH");
//        System.out.println(currentDateStr);


//        String main = "";
//        //
//        AnalsistToken();

        String token ="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjoiU0xCanRmc2gteGdLajItT1BGSVQtMm9LbG1iU25JZWFZc084IiwiaWF0IjoxNzIyOTU4MDE2LCJleHAiOjE3MjI5NTk4MTZ9.Y3BG2xgcEkN98dcEFKNFCsRSaNK9Lf1k3O1A_bnUQ1Q";
        String[] split = token.split("\\.");
        System.out.println("头："+ split[0]);
        System.out.println("体："+ split[1]);
        System.out.println("尾："+ split[2]);
        System.out.println("解密 base64");
        Base64.Decoder decoder = Base64.getDecoder();
        System.out.println("头："+ new String(decoder.decode(split[0])));
        System.out.println("体："+ new String(decoder.decode(split[1])));
        System.out.println("尾："+ new String(Base64.getUrlDecoder().decode(split[2])));


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