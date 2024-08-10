package zedata.com.controller;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import zedata.com.until.SaveUntil;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author Mr-Glacier
 * @ApiNote  优信拍数据获取
 */
public class ControllerYouXinPai {


    /**
     * 使用 httpClient 进行发送请求 并且获取得到 响应 cookie
     */
    public Map<String,String> postRequestByHttpClient(String mainUrl, String parmStr, String cookie) {
        Map<String,String> map = new HashMap<>();
        // 初始错误代码
        map.put("httpStatus", "400");

        // 初始化返回参数
        String csrfTokenKey = "";
        String csrfToken = "";
        String jwtToken = "";
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(mainUrl);
            request.addHeader("Content-Type", "application/json; charset=utf-8");
            request.addHeader("Referer", "https://www.youxinpai.com/trade");
            request.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/125.0.0.0 Safari/537.36");
            request.addHeader("X-Requested-With", "XMLHttpRequest");
            request.addHeader("priority", "u=1, i");
            request.addHeader("origin", "https://www.youxinpai.com");
            request.addHeader("Cookie", cookie);
            StringEntity stringEntity = new StringEntity(parmStr, "UTF-8");
            request.setEntity(stringEntity);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                System.out.println("Response Code: " + response.getStatusLine().getStatusCode());
                map.remove("httpStatus");
                map.put("httpStatus", String.valueOf(response.getStatusLine().getStatusCode()));
                Header[] headers = response.getAllHeaders();
                for (Header header : headers) {
                    if ("Set-Cookie".equals(header.getName())){
                        System.out.println(header.getValue());
                    }
                    String waitDealStr = header.getValue();
                    // 待处理字符串
                    if (waitDealStr.contains("csrfToken_key=")) {
                        csrfTokenKey = waitDealStr.split(";")[0].replace("csrfToken_key=", "");
                    }
                    if (waitDealStr.contains("csrfToken=")) {
                        csrfToken = waitDealStr.split(";")[0].replace("csrfToken=", "");
                    }
                    if (waitDealStr.contains("jwt_token=")) {
                        jwtToken = waitDealStr.split(";")[0].replace("jwt_token=", "");
                     }
                    map.put("csrfTokenKey", csrfTokenKey);
                    map.put("csrfToken", csrfToken);
                    map.put("jwtToken", jwtToken);
                }
                String responseBody = EntityUtils.toString(response.getEntity());
                if ("200".equals(map.get("httpStatus"))){
                    map.put("responseBody", responseBody);
                }
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }


    /**
     * 优信拍 流程控制方法
     */
    public void methodProcessControl(String firstUrl) {
        SaveUntil saveUntil = new SaveUntil();

        String mainSavePath = "";



        // 拼接Cookie-->存在几个 过期时间较长的字段,采取记录形式
        // 1.xxzlclientid 过期时间 2025-08-04T
        String xxzlclientid = "7a923008-bbf8-4b78-b1f2-1717568308111";
        // 2. xxzlxxid 过期时间 2025-08-04T
        String xxzlxxid = "pfmxwtDfh9JN7gAwBs330bDz99xCin6xXlLaiBRy0BNp5uBTsaB9cJPDll/1KrKTlE4J";
        // 3. xxzlbbid 过期时间 2025-08-04T
        String xxzlbbid = "pfmbM3wxMDI5M3wxLjcuMHwxNzIyNzg0MTM2NTcwfDVwaEo4NlNXaWcrV1pSSzFlLytoTkdrZEsvbS9lK0UweCtHUWxSVHU0eG89fDNkNTdiMmQwNzI2NmU4N2MxNjcxMTFkZmRiMjAwZmRmXzE3MjI3ODQxMzMxMzdfOTRkYjQ0OTJkNDA3NDhhODkwNzlmOWVmMzQ5M2Q2OGRfMTk3NDQ0NzIwMXwwN2ExMmI0YThmZWEyMWMwMTYxZjM3OWI4ODczMjVmNF8xNzIyNzg0MTM1MDIzXzI1Ng==";
        // 4. id58 过期时间很长
        String id58 = "CocLxWZidCqUywmePIHHAg==";

        String csrfTokenKeyStr = "csrfTokenKeyStr";
        String csrfTokenStr = "csrfTokenStr";
        String jwtTokenStr = "JwtTokenStr";

        // cookie 模板
        String tokenStrModel = "xxzlclientid="+xxzlclientid+"; " +
                "xxzlxxid="+xxzlxxid+"; " +
                "id58="+id58+"; " +
                "xxzlbbid="+xxzlbbid+"; " +
                "csrfToken_key=csrfTokenKeyStr; " +
                "csrfToken=csrfTokenStr; " +
                "jwt_token=JwtTokenStr";

        int pageNum = 1;
//        while (true){
//            String parmStr = "{\"entities\":\"{\\\"req\\\":{\\\"cityIds\\\":[],\\\"serialIds\\\":[],\\\"appearanceGrades\\\":[],\\\"skeletonGrades\\\":[],\\\"interiorGrades\\\":[],\\\"emissionStandards\\\":[],\\\"carPriceLevel\\\":[],\\\"carYearLevel\\\":[],\\\"carGearbox\\\":[],\\\"carOwners\\\":[],\\\"carUseTypes\\\":[],\\\"fuelTypes\\\":[],\\\"conditionPriceType\\\":[],\\\"transferCounts\\\":[],\\\"startPriceType\\\":[],\\\"isNotBubbleCar\\\":false,\\\"isNotBurnCar\\\":false,\\\"isNotSmallReport\\\":false,\\\"orderFields\\\":10},\\\"page\\\":[{\\\"page\\\":1,\\\"pageSize\\\":2,\\\"pageTab\\\":\\\"pc_circle\\\"},{\\\"page\\\":" + pageNum + ",\\\"pageSize\\\":15,\\\"pageTab\\\":\\\"immediately\\\"},{\\\"page\\\":1,\\\"pageSize\\\":2,\\\"pageTab\\\":\\\"delay\\\"},{\\\"page\\\":1,\\\"pageSize\\\":2,\\\"pageTab\\\":\\\"fixedPrice\\\"},{\\\"page\\\":1,\\\"pageSize\\\":2,\\\"pageTab\\\":\\\"benz\\\"},{\\\"page\\\":1,\\\"pageSize\\\":2,\\\"pageTab\\\":\\\"attention\\\"}]}\"}";
//            String tokenStr = tokenStrModel.replace("csrfTokenKeyStr",csrfTokenKeyStr)
//                    .replace("csrfTokenStr",csrfTokenStr)
//                    .replace("JwtTokenStr",jwtTokenStr);
//            Map<String,String> map = postRequestByHttpClient(firstUrl, parmStr, tokenStr);
//            if ("200".equals(map.get("httpStatus"))){
//                String responseBody = map.get("responseBody");
//                JSONObject jsonObject = JSONObject.parseObject(responseBody).getJSONObject("data").getJSONObject("entities").getJSONObject("immediately");
//                JSONArray jsonArray = jsonObject.getJSONArray("auctionListEntity");
//                if (jsonArray.size()==0){
//                    break;
//                }else {
//                    saveUntil.Method_SaveFile(savePath+pageNum+".txt",responseBody);
//                    csrfTokenKeyStr = map.get("csrfTokenKey");
//                    csrfTokenStr = map.get("csrfToken");
//                    jwtTokenStr = map.get("jwtToken");
//                }
//            }else{
//                break;
//            }
//        }
    }








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
                    .header("origin", "https://www.youxinpai.com")
                    .cookie("Cookie", "xxzlclientid=7a923008-bbf8-4b78-b1f2-1717568308111; xxzlxxid=pfmxwtDfh9JN7gAwBs330bDz99xCin6xXlLaiBRy0BNp5uBTsaB9cJPDll/1KrKTlE4J; id58=CocLxWZidCqUywmePIHHAg==; xxzlbbid=pfmbM3wxMDI5M3wxLjcuMXwxNzIzMTc2MzIzMzYyfE9OdWZ6cVJjR3VLeWQ3aGlUL3dNSG5mQXJXcGhlWXJYenBmQzNEaVc0UVE9fGI5NTc5NTM1ODM3OGZiOWU5ZWFmY2Q4ODc4NGEyYTYwXzE3MjMxNzYzMjE3NjdfYzQyZWZkZjIyNjgxNGIwMmIzNTM3ZmI5ZTRlOGIzM2ZfMjAyNzk3NDU5NnwwYzBhN2U1M2I1NDk1ZDFmYzZhMzk2ZGVlMzg5NDJjMV8xNzIzMTc2MzIyNjcxXzI1Ng==; csrfToken_key=7IX22vzce5M_4ipsf4A3rMuV; csrfToken=oy8WM5HT-6Zm-x84vj1TKsm96bvnK_37IYcs; jwt_token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjoib3k4V001SFQtNlptLXg4NHZqMVRLc205NmJ2bktfMzdJWWNzIiwiaWF0IjoxNzIzMTc2MzI1LCJleHAiOjE3MjMxNzgxMjV9.OAhN2xlrd5UDPwx1vD9IOTrtBYCUPam2GVirJaj59go")
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .requestBody(parmStr)
                    .execute();
            System.out.println(res.statusCode());
            System.out.println(res.body());
            // 获取所有响应头
            Map<String, String> headers = res.headers();
            System.out.println(headers);
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
