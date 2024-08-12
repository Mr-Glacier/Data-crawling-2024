package zedata.com.controller;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonArray;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import zedata.com.config.LoggingConfig;
import zedata.com.dao.DaoFather;
import zedata.com.entity.BeanCheYiPaiCarInfo;
import zedata.com.entity.BeanYouXinPaiCarInfo;
import zedata.com.until.SaveUntil;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Mr-Glacier
 * @ApiNote  优信拍数据获取
 */
public class ControllerYouXinPai {

    private static final Logger logger = LogManager.getLogger(LoggingConfig.class);


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
                logger.info("请求响应状态码:{}", response.getStatusLine().getStatusCode());
                map.remove("httpStatus");
                map.put("httpStatus", String.valueOf(response.getStatusLine().getStatusCode()));
                Header[] headers = response.getAllHeaders();
                for (Header header : headers) {
                    if ("Set-Cookie".equals(header.getName())){
                        logger.info("cookie:{}", header.getValue());
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
            logger.error("请求失败", e);
        }
        return map;
    }


    /**
     * 解析所有的数据并且返回本次解析的数据
     */
    public List<BeanYouXinPaiCarInfo> methodAnalysisData(String data,String currentTime) {
        List<BeanYouXinPaiCarInfo> beanList = new ArrayList<>();
        JSONObject jsonObject = JSONObject.parseObject(data).getJSONObject("data").getJSONObject("immediately");
        JSONArray dataArray =   jsonObject.getJSONArray("auctionListEntity");
        if (dataArray.size() > 0){
            for (int i = 0; i < dataArray.size(); i++) {
                BeanYouXinPaiCarInfo beanYouXinPaiCarInfo = new BeanYouXinPaiCarInfo();
                JSONObject oneCarJson = dataArray.getJSONObject(i);
                beanYouXinPaiCarInfo.setC_wareHouseTime(currentTime);
                beanYouXinPaiCarInfo.setC_auctionStatus(oneCarJson.getString("auctionStatus"));
                beanYouXinPaiCarInfo.setC_auctionTitle(oneCarJson.getString("auctionTitle"));
                beanYouXinPaiCarInfo.setC_auctionType(oneCarJson.getString("auctionType"));
                beanYouXinPaiCarInfo.setC_bidEndTime(oneCarJson.getString("bidEndTime"));
                beanYouXinPaiCarInfo.setC_bidStartTime(oneCarJson.getString("bidStartTime"));
                beanYouXinPaiCarInfo.setC_carCityName(oneCarJson.getString("carCityName"));
                beanYouXinPaiCarInfo.setC_carColor(oneCarJson.getString("carColor"));
                beanYouXinPaiCarInfo.setC_carPlaceCity(oneCarJson.getString("carPlaceCity"));
                beanYouXinPaiCarInfo.setC_channelCount(oneCarJson.getString("channelCount"));
                beanYouXinPaiCarInfo.setC_channelTitle(oneCarJson.getString("channelTitle"));
                beanYouXinPaiCarInfo.setC_cityId(oneCarJson.getString("cityId"));
                beanYouXinPaiCarInfo.setC_conditionGradeSmall(oneCarJson.getString("conditionGradeSmall"));
                beanYouXinPaiCarInfo.setC_currentIndex(oneCarJson.getString("currentIndex"));
                beanYouXinPaiCarInfo.setC_currentPublishOrder(oneCarJson.getString("currentPublishOrder"));
                beanYouXinPaiCarInfo.setC_energyType(oneCarJson.getString("energyType"));
                beanYouXinPaiCarInfo.setC_hasVideo(oneCarJson.getString("hasVideo"));
                beanYouXinPaiCarInfo.setC_sourceId(oneCarJson.getString("id"));
                beanYouXinPaiCarInfo.setC_imgUrl(oneCarJson.getString("imgUrl"));
                beanYouXinPaiCarInfo.setC_isAttention(oneCarJson.getString("isAttention"));
                beanYouXinPaiCarInfo.setC_isNewPublish(oneCarJson.getString("isNewPublish"));
                beanYouXinPaiCarInfo.setC_kilometers(oneCarJson.getString("kilometers"));
                beanYouXinPaiCarInfo.setC_labelColor(oneCarJson.getString("labelColor"));
                beanYouXinPaiCarInfo.setC_labelName(oneCarJson.getString("labelName"));
                beanYouXinPaiCarInfo.setC_mileage(oneCarJson.getString("mileage"));
                beanYouXinPaiCarInfo.setC_parkingNum(oneCarJson.getString("parkingNum"));
                beanYouXinPaiCarInfo.setC_power(oneCarJson.getString("power"));
                beanYouXinPaiCarInfo.setC_pricesStart(oneCarJson.getString("pricesStart"));
                beanYouXinPaiCarInfo.setC_publishType(oneCarJson.getString("publishType"));
                beanYouXinPaiCarInfo.setC_redCar(oneCarJson.getString("redCar"));
                beanYouXinPaiCarInfo.setC_reportViewType(oneCarJson.getString("reportViewType"));
                beanYouXinPaiCarInfo.setC_reservePrice(oneCarJson.getString("reservePrice"));
                beanYouXinPaiCarInfo.setC_standardCode(oneCarJson.getString("standardCode"));
                beanYouXinPaiCarInfo.setC_startPriceType(oneCarJson.getString("startPriceType"));
                beanYouXinPaiCarInfo.setC_totalGrade(oneCarJson.getString("totalGrade"));
                beanYouXinPaiCarInfo.setC_year(oneCarJson.getString("year"));
                beanYouXinPaiCarInfo.setC_crykey(oneCarJson.getString("crykey"));
                beanList.add(beanYouXinPaiCarInfo);
            }
        }
        return beanList;
    }

    /**
     * 对于本次数据进行去重入库
     */
    public int methodInsertData(List<BeanYouXinPaiCarInfo> beanList) {
        // 初始化计数器
        int count = 0;
        // 首先对于本次数据 内部重复值进行去除
        List<BeanYouXinPaiCarInfo> distinctBeanList = new ArrayList<>(beanList.stream().collect(Collectors.toMap(
                        BeanYouXinPaiCarInfo::getC_sourceId,
                        beanCheYiPaiCarInfo -> beanCheYiPaiCarInfo,
                        (bean1, bean2) -> bean1))
                .values());

        // 然后查询得到数据库中所有的数据 C_sourceId List
        List<String> sourceIdList = new ArrayList<>();
        DaoFather daoFather = new DaoFather(0,2);
        List<String> columnList = Collections.singletonList("C_sourceId");
        for (Map<String,String> map : daoFather.methodFindFree(columnList,"")){
            sourceIdList.add(map.get("C_sourceId"));
        }

        //如果 sourceId 不存在于数据中,则进行入库操作
        for (BeanYouXinPaiCarInfo bean : distinctBeanList){
            if (!sourceIdList.contains(bean.getC_sourceId())){
                daoFather.MethodInsert(bean);
                count++;
            }
        }
        return count;
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
