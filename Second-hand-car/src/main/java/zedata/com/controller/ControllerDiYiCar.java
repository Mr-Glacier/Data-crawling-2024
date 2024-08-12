package zedata.com.controller;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import zedata.com.dao.DaoFather;
import zedata.com.entity.BeanCheYiPaiCarInfo;
import zedata.com.entity.BeanDiYiCarCounty;
import zedata.com.entity.BeanDiYiCarDetailInfo;
import zedata.com.entity.BeanDiYiCarSimpleInfo;
import zedata.com.until.ReadUntil;
import zedata.com.until.SaveUntil;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Mr-Glacier
 * @apiNote 第一汽车网的数据获取  定时爬取时间 按照 1 天执行一次
 */
public class ControllerDiYiCar {

    private  DaoFather daoCounty = new DaoFather(0,2);
    private DaoFather daoSimple = new DaoFather(0,3);

    private DaoFather daoDetail = new DaoFather(0,4);
    private SaveUntil saveUntil = new SaveUntil();
    private ReadUntil readUntil = new ReadUntil();

    /**
     * 请求单个页面的方法
     */
    public static String methodDownHtml(String url, Map<String, String> map){
        String content = "";
        try {
            Document document = Jsoup.connect(url)
                    .method(Connection.Method.GET)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36")
                    .header("Referer", "https://so.iautos.cn/")
                    .header("Host", "so.iautos.cn")
                    .cookies(map)
                    .get();
            content = document.html();
            System.out.println(content);
        }catch (Exception e){
            e.printStackTrace();
        }
        return content;
    }


    /**
     * 下载 区县数据并入库,
     * 采取一个更新的操作,有新的数据加入就添加 ,如果是去除掉了,就删除,
     * 并记录状态 0 禁用, 1启用
     */
    public static Map<String,String> methodDownAndAnalysisCountyData(String mainUrl, String currentTime){
        Map<String, String> map = new HashMap<>();
        map.put("_pk_id.3.9483","3bd51c978eb7306f.1723281370.1.1723281370.1723281370.");
        map.put("_pk_cvar.3.9483","*");
        map.put("_ga","GA1.2.95901842.1723281370");
        map.put("_gat","1");
        map.put("Hm_lvt_561c1d73e7a5e5611b5a93fe18365726","1723270729");
        map.put("Hm_lpvt_561c1d73e7a5e5611b5a93fe18365726","1723281370");
        map.put("HMACCOUNT","B1FB75C3E410B760");
        map.put("_ga_9V8VL6MLJ8","GS1.2.1723281369.1.0.1723281369.0.0.0");

        Map<String,String> map2 = new HashMap<>();
        String html = methodDownHtml(mainUrl,map);
        if (html.length()>0){
            map2.put("state","SUCCESS");
            Document document = Jsoup.parse(html);
            Elements elements = document.select("div.city-content").select(".content-city").select(".city-li").select("dl").select("dd").select("a");
            for (Element element : elements) {
                String country = element.select("a").text();
                String url = element.select("a").attr("href");

                String pinyin = element.select("a").attr("onclick").replaceAll("soGa\\('city', '","").replace("'\\);", "");
                BeanDiYiCarCounty bean = new BeanDiYiCarCounty();
                bean.setC_countyName(country);
                bean.setC_countyUrl("https:" + url);
                bean.setC_countyPage("-");
                bean.setC_downState("否");
                bean.setC_pinyin(pinyin);
                bean.setC_wareHouseTime(currentTime);
                System.out.println(bean.getC_countyUrl());
            }
            return map;
        }else{
            map2.put("state","ERROR");
            return map2;
        }
    }


    /**
     *  下载区县下面所有页面 数据
     */

    public  void methodDownCountyPage(String currentTime,Map<String,String> cookieMap,String mainSavePath){
        List<String> columnList = Arrays.asList("C_countyUrl","C_wareHouseTime","C_countyName","C_ID");
        List<Map<String,String>> list = daoCounty.methodFindFree(columnList, " where  C_wareHouseTime = '"+currentTime+"'");
        for (Map<String,String> map : list){
            String countyUrl = map.get("C_countyUrl");
            String countyName = map.get("C_countyName");
            String countyID = map.get("C_ID");

            int pageNumber = 0;
            for (int i = 1; i < 51; i++) {
                String mainUrl = countyUrl + "p" + i + "asdsvepcatcpbnscac/#buyCars";
                if (i == 1) {
                    mainUrl = countyUrl + "#buyCars";
                }
                String html = methodDownHtml(mainUrl,cookieMap);
                Document document = Jsoup.parse(html);
                Elements elements = document.select(".car-pic-form-box.car-box-list.clear");
                Elements notFount = document.select("div.not-found").select("img");
                if (elements.size()>0&&notFount.size()==0){
                    pageNumber ++;
                    saveUntil.Method_SaveFile(mainSavePath+countyName+"_"+pageNumber+".txt",html);
                }else{
                    break;
                }
            }
            daoCounty.methodUpdateDiYiCountyDetail(Integer.parseInt(countyID),String.valueOf(pageNumber));
        }
    }

    /**
     * 解析所下载的全部页面
     */
    public void methodAnalysisCountyPage(String mainSavePath,String currentTime){
        List<String> fileNameList = readUntil.getFileNames(mainSavePath);
        List<BeanDiYiCarSimpleInfo> beanList = new ArrayList<>();
        for (String filename : fileNameList) {
            String content = readUntil.Method_ReadFile(mainSavePath+filename);
            // 开始解析全部车辆
            Document document = Jsoup.parse(content);
            Elements elements = document.select(".car-pic-form-box.car-box-list.clear").select("li");
            for (Element element : elements) {
                String soruceId = element.select("li").attr("data-id");
                String carName = element.select("a").select("h6.name").text();
                String parameter = element.select("a").select(".parameter").text();
                String date = parameter.split("/")[0];
                String mileage = parameter.split("/")[1];
                String price = element.select("a").select(".price.num").text();
                String tags = element.select("a").select(".tags").text();
                String detailUrl = element.select("a").attr("href");
                String imageUrl = element.select("a").select(".img").attr("class src");
                BeanDiYiCarSimpleInfo bean = new BeanDiYiCarSimpleInfo();
                bean.setC_carName(carName);
                bean.setC_licenseDate(date);
                bean.setC_mileage(mileage);
                bean.setC_price(price);
                bean.setC_tags(tags);
                bean.setC_sourceId(soruceId);
                bean.setC_sourceFileName(filename);
                bean.setC_detailUrl(detailUrl);
                bean.setC_image(imageUrl);
                bean.setC_wareHouseTime(currentTime);
                bean.setC_downState("否");
                bean.setC_downTime("-");
                beanList.add(bean);
            }
        }
        // 对数据进行去重 根据原始Id这列
        List<BeanDiYiCarSimpleInfo> distinctBeanList = new ArrayList<>(beanList.stream().collect(Collectors.toMap(
                        BeanDiYiCarSimpleInfo::getC_sourceId,
                        beanCheYiPaiCarInfo -> beanCheYiPaiCarInfo,
                        (bean1, bean2) -> bean1))
                .values());
        System.out.println("去重前数据条数："+beanList.size());
        System.out.println("去重后数据条数："+distinctBeanList.size());
        // 进行入库操作
        distinctBeanList.forEach(bean -> {
            daoSimple.MethodInsert(bean);
        });
    }

    /**
     * 对于详情页数据进行重新下载
     */
    public void methodDownDetailPage(String mainSavePath,String currentTime,Map<String,String> cookieMap){
        List<String> columnList = Arrays.asList("C_wareHouseTime","C_sourceId","C_ID","C_downState","C_detailUrl");
        List<Map<String,String>> list = daoSimple.methodFindFree(columnList, " where  C_wareHouseTime = '"+currentTime+"'");
        for (Map<String,String> map : list){
            String downState = map.get("C_downState");
            if (downState.equals("否")){
                String sourceId = map.get("C_sourceId");
                String mainUrl = map.get("C_detailUrl");
                String html = methodDownHtml(mainUrl,cookieMap);
                if (html.length()>0){
                    daoSimple.methodUpdateStateTime(Integer.parseInt(map.get("C_ID")));
                    saveUntil.Method_SaveFile(mainSavePath+sourceId+".txt",html);
                }
            }
        }
    }


    /**
     * 解析下载的详情页面
     */
    public Integer methodAnalysisDetailPage(String mainSavePath,String currentTime){
        List<String> columnList = Arrays.asList("C_carName","C_sourceId","C_sourceFileName","C_licenseDate",
        "C_mileage","C_image","C_tags","C_price","C_detailUrl","C_downState");

        List<Map<String, String>> beanList = daoSimple.methodFindFree(columnList, " where  C_wareHouseTime = '"+currentTime+"' and  C_downState = '是' ");
        List<BeanDiYiCarDetailInfo> insertBeanList = new ArrayList<>();
        beanList.forEach(map -> {
            String sourceId = map.get("C_sourceId");
            String C_licenseDate = map.get("C_licenseDate");
            String C_mileage = map.get("C_mileage");
            String C_price = map.get("C_price");
            String C_tags = map.get("C_tags");
            String C_image = map.get("C_image");
            String C_carName = map.get("C_carName");
            String C_sourceFileName = map.get("C_sourceFileName");
            String C_detailUrl = map.get("C_detailUrl");
            BeanDiYiCarDetailInfo bean = new BeanDiYiCarDetailInfo();

            bean.setC_carName(C_carName);
            bean.setC_licenseDate(C_licenseDate);
            bean.setC_mileage(C_mileage);
            bean.setC_price(C_price);
            bean.setC_tags(C_tags);
            bean.setC_image(C_image);
            bean.setC_sourceFileName(C_sourceFileName);
            bean.setC_sourceId(sourceId);
            bean.setC_detailUrl(C_detailUrl);

            String html = readUntil.Method_ReadFile(mainSavePath+sourceId+".txt");
            Document document = Jsoup.parse(html);
            String carNameDetaile = document.select("h1.title.de-drei-col").select("span").text();
            // 新车含税价
            String newCarPriceIncludingTax = document.select("div.header-info.priceloan.loan-left").select("span.left-normal").text();
            String newCarPrice = document.select("div.header-info.priceloan.loan-left").select("span.loan-other").select("p").get(0).text();
            String vehiclePurchaseTax = document.select("div.header-info.priceloan.loan-left").select("span.loan-other").select("p").get(1).text();
            String downPayment = document.select("div.header-info.loan-box.clean").select(".loan-box-text").get(0).text();
            String monthlyPayment = document.select("div.header-info.loan-box.clean").select(".loan-box-text").get(1).text();
            Elements elementsOthers =  document.select("div.header-info.others.clean").select("li");
            Map<String,String> mapValues =  new HashMap<>();
            elementsOthers.forEach(element -> {
                String  key = element.select("h6.t").text();
                String  value = element.select("p").text();
                mapValues.put(key,value);
            });
            String detailDisplacement = "-";
            String detailEmission = "-";
            String location = "-";
            if (mapValues.containsKey("排放")){
                detailDisplacement = mapValues.get("排放");
            }
            if (mapValues.containsKey("排量")){
                detailEmission = mapValues.get("排量");
            }
            if (mapValues.containsKey("所在地")){
                location = mapValues.get("所在地");
            }
            bean.setC_detailName(carNameDetaile);
            bean.setC_newCarPriceIncludingTax(newCarPriceIncludingTax);
            bean.setC_newCarPrice(newCarPrice);
            bean.setC_vehiclePurchaseTax(vehiclePurchaseTax);
            bean.setC_downPayment(downPayment);
            bean.setC_monthlyPayment(monthlyPayment);
            bean.setC_detailDisplacement(detailDisplacement);
            bean.setC_detailEmission(detailEmission);
            bean.setC_location(location);
            bean.setC_wareHouseTime(currentTime);
            insertBeanList.add(bean);
        });
        daoDetail.Method_Insert(insertBeanList);
        return insertBeanList.size();
    }



    public static void main(String[] args) {
        Map<String,String> map = methodDownAndAnalysisCountyData("https://so.iautos.cn/", "2019-04-01");
        String state = map.get("state");
        if (map.get("state").equals("SUCCESS")){

        }
    }
}
