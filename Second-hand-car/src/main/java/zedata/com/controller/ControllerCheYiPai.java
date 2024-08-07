package zedata.com.controller;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.util.DateUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import zedata.com.dao.DaoFather;
import zedata.com.entity.BeanCheYiPaiCarInfo;
import zedata.com.entity.Bean_car_ryk_date;
import zedata.com.until.ReadUntil;
import zedata.com.until.SaveUntil;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Mr-Glacier
 */
public class ControllerCheYiPai {

    public void Method_downHtml(String url,String parmStr){
        try{
            long timestamp = System.currentTimeMillis();
            System.out.println(timestamp);
            Random random = new Random();
            int fourDigitNumber = 1000 + random.nextInt(9000);
            String timestampStr = String.valueOf(timestamp)+fourDigitNumber+"000";
            System.out.println(timestampStr);
            Thread.sleep(5000);
            // 将数字转换为字符串
            String fourDigitString = String.valueOf(fourDigitNumber);
            //171757323 0969 8551  000
            Connection.Response res =  Jsoup.connect(url).method(Connection.Method.POST)
                    .header("Accept", "application/json, text/plain, */*")
                    .header("Accept-Encoding","gzip, deflate, br, zstd")
                    .header("Accept-Language","zh-CN,zh;q=0.9")
                    .header("App","10")
                    .header("Clienttype", "4")
                    .header("Connection", "keep-alive")
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/125.0.0.0 Safari/537.36")
                    .header("Tid", "17177319378798585000")
                    .header("Host","shenyu.cheyipai.com")
                    .header("Referer","https://www.cheyipai.com/")
                    .header("Origin","https://www.cheyipai.com")
//                    .header("Imei", "2500400422")//261342069
                    .header("Version","8.6.5")
                    .header("Cookie", "JSESSIONID=AD0596C61A8CB049D36E687009FD71C6; acw_tc=b65cfd2c17177396994197960e6dd3f4a85b41edde571f6b13e60b55b2e3f0")
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .requestBody(parmStr)
                    .execute();
            System.out.println(res.body());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String Method_PostData(String url,String parmStr,String cookie){
        try {
            Document document = Jsoup.connect(url)
                    .header("Content-Type", "application/json")
                    .header("Cookie", cookie)
                    .requestBody(parmStr)
                    .ignoreContentType(true)
                    .post();

            // 打印响应
            Random random = new Random();
            int fourDigitNumber = 1000 + random.nextInt(5000);
            Thread.sleep(fourDigitNumber);
            return document.body().text();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解析方法,读取文件方法
     */
    public void Method_AnalysisData(String savePath,String date){
        DaoFather daoFather = new DaoFather(2, 4);
        ReadUntil readUntil = new ReadUntil();
        List<String> fileList = readUntil.getFileNames(savePath);
        for (String fileName : fileList) {
            String filePath = savePath + fileName;
            String data = readUntil.Method_ReadFile(filePath);
            JSONObject jsonObject = JSONObject.parseObject(data).getJSONObject("data");
            JSONArray jsonArray = jsonObject.getJSONArray("auctionGoodsRoundVOList");
            for (int i = 0; i < jsonArray.size(); i++){
                JSONObject jsonObject1 = jsonArray.getJSONObject(i).getJSONObject("carInfo");
                String carName = jsonObject1.getString("carName");
                String registDate = jsonObject1.getString("registDate");
                String mileage = jsonObject1.getString("mileage");
                String price = jsonArray.getJSONObject(i).getJSONObject("auctionInfo").getString("priorityOfferW");
                String locationName = jsonObject1.getString("locationName");

                Bean_car_ryk_date bean_car_ryk_date = new Bean_car_ryk_date();
                bean_car_ryk_date.setC_name(carName);
                bean_car_ryk_date.setC_licenseDate(registDate);
                bean_car_ryk_date.setC_mileage(mileage);
                bean_car_ryk_date.setC_price(price);
                bean_car_ryk_date.setC_date(date);
                bean_car_ryk_date.setC_fileName(fileName);
                bean_car_ryk_date.setC_county(locationName);
                bean_car_ryk_date.setC_deatileUrl("-");
                bean_car_ryk_date.setC_source("车易拍");
                daoFather.Method_Insert(bean_car_ryk_date);
            }


        }
    }

    public List<BeanCheYiPaiCarInfo> methodAnalysisData(String data){
        List<BeanCheYiPaiCarInfo> beanList = new ArrayList<>();
        JSONObject jsonObject = JSONObject.parseObject(data).getJSONObject("data");
        JSONArray jsonArray = jsonObject.getJSONArray("auctionGoodsRoundVOList");
        if (jsonArray.size() > 0){
            for (int i = 0; i < jsonArray.size(); i++){
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                BeanCheYiPaiCarInfo beanCheYiPaiCarInfo = new BeanCheYiPaiCarInfo();

                JSONObject auctionInfo = jsonObject1.getJSONObject("auctionInfo");
                beanCheYiPaiCarInfo.setC_auctionBidCount(auctionInfo.getString("auctionBidCount"));
                beanCheYiPaiCarInfo.setC_auctionId(auctionInfo.getString("auctionId"));
                beanCheYiPaiCarInfo.setC_auctionLeftTime(auctionInfo.getString("auctionLeftTime"));
                beanCheYiPaiCarInfo.setC_auctionMode(auctionInfo.getString("auctionMode"));
                beanCheYiPaiCarInfo.setC_auctionModeName(auctionInfo.getString("auctionModeName"));
                beanCheYiPaiCarInfo.setC_auctionPrice(auctionInfo.getString("auctionPrice"));
                beanCheYiPaiCarInfo.setC_auctionPriceW(auctionInfo.getString("auctionPriceW"));
                beanCheYiPaiCarInfo.setC_auctionPriceY(auctionInfo.getString("auctionPriceY"));
                beanCheYiPaiCarInfo.setC_auctionSerialNumber(auctionInfo.getString("auctionSerialNumber"));
                beanCheYiPaiCarInfo.setC_auctionSerialNumberHundred(auctionInfo.getString("auctionSerialNumberHundred"));
                beanCheYiPaiCarInfo.setC_beginTime(auctionInfo.getString("beginTime"));
                beanCheYiPaiCarInfo.setC_bidDepositNoDomainY(auctionInfo.getString("bidDepositNoDomainY"));
                beanCheYiPaiCarInfo.setC_bidDepositY(auctionInfo.getString("bidDepositY"));
                beanCheYiPaiCarInfo.setC_bidRisk(auctionInfo.getString("bidRisk"));
                beanCheYiPaiCarInfo.setC_bidRiskY(auctionInfo.getString("bidRiskY"));
                beanCheYiPaiCarInfo.setC_bottomDealTag(auctionInfo.getString("bottomDealTag"));
                beanCheYiPaiCarInfo.setC_brightBid(auctionInfo.getString("brightBid"));
                beanCheYiPaiCarInfo.setC_browerStatus(auctionInfo.getString("browerStatus"));
                beanCheYiPaiCarInfo.setC_commissionIsDistinguishDomainNoDomain(auctionInfo.getString("commissionIsDistinguishDomainNoDomain"));
                beanCheYiPaiCarInfo.setC_compareWithTenderDisplay(auctionInfo.getString("compareWithTenderDisplay"));
                beanCheYiPaiCarInfo.setC_countDownStatus(auctionInfo.getString("countDownStatus"));
                beanCheYiPaiCarInfo.setC_distanceBeginLeftTime(auctionInfo.getString("distanceBeginLeftTime"));
                beanCheYiPaiCarInfo.setC_domainPlatformCommissionY(auctionInfo.getString("domainPlatformCommissionY"));
                beanCheYiPaiCarInfo.setC_domainStoreCommissionY(auctionInfo.getString("domainStoreCommissionY"));
                beanCheYiPaiCarInfo.setC_endTime(auctionInfo.getString("endTime"));
                beanCheYiPaiCarInfo.setC_enquiryTag(auctionInfo.getString("enquiryTag"));
                beanCheYiPaiCarInfo.setC_exceedBasePrice(auctionInfo.getString("exceedBasePrice"));
                beanCheYiPaiCarInfo.setC_finalOffer(auctionInfo.getString("finalOffer"));
                beanCheYiPaiCarInfo.setC_finalOfferDomainY(auctionInfo.getString("finalOfferDomainY"));
                beanCheYiPaiCarInfo.setC_finalOfferNoDomainY(auctionInfo.getString("finalOfferNoDomainY"));
                beanCheYiPaiCarInfo.setC_finalOfferW(auctionInfo.getString("finalOfferW"));
                beanCheYiPaiCarInfo.setC_finalOfferY(auctionInfo.getString("finalOfferY"));
                beanCheYiPaiCarInfo.setC_focusStatus(auctionInfo.getString("focusStatus"));
                beanCheYiPaiCarInfo.setC_frozenDeposit(auctionInfo.getString("frozenDeposit"));
                beanCheYiPaiCarInfo.setC_frozenDepositY(auctionInfo.getString("frozenDepositY"));
                beanCheYiPaiCarInfo.setC_frozenType(auctionInfo.getString("frozenType"));
                beanCheYiPaiCarInfo.setC_highestPriceRemind(auctionInfo.getString("highestPriceRemind"));
                beanCheYiPaiCarInfo.setC_intelligenceBidRight(auctionInfo.getString("intelligenceBidRight"));
                beanCheYiPaiCarInfo.setC_intelligenceRound(auctionInfo.getString("intelligenceRound"));
                beanCheYiPaiCarInfo.setC_isAutoBid(auctionInfo.getString("isAutoBid"));
                beanCheYiPaiCarInfo.setC_isCrossDomainUser(auctionInfo.getString("isCrossDomainUser"));
                beanCheYiPaiCarInfo.setC_leftTimeStr(auctionInfo.getString("leftTimeStr"));
                beanCheYiPaiCarInfo.setC_minimumPriceStep(auctionInfo.getString("minimumPriceStep"));
                beanCheYiPaiCarInfo.setC_moreThanTender(auctionInfo.getString("moreThanTender"));
                beanCheYiPaiCarInfo.setC_myBidStatus(auctionInfo.getString("myBidStatus"));
                beanCheYiPaiCarInfo.setC_myIntelligenceBidStatus(auctionInfo.getString("myIntelligenceBidStatus"));
                beanCheYiPaiCarInfo.setC_noDomainPlatformCommissionY(auctionInfo.getString("noDomainPlatformCommissionY"));
                beanCheYiPaiCarInfo.setC_noDomainStoreCommissionY(auctionInfo.getString("noDomainStoreCommissionY"));
                beanCheYiPaiCarInfo.setC_nonLocalTagDesc(auctionInfo.getString("nonLocalTagDesc"));
                beanCheYiPaiCarInfo.setC_operationPlatform(auctionInfo.getString("operationPlatform"));
                beanCheYiPaiCarInfo.setC_priceButtonRight(auctionInfo.getString("priceButtonRight"));
                beanCheYiPaiCarInfo.setC_priorityOffer(auctionInfo.getString("priorityOffer"));
                beanCheYiPaiCarInfo.setC_priorityOfferOrAuctionPrice(auctionInfo.getString("priorityOfferOrAuctionPrice"));
                beanCheYiPaiCarInfo.setC_priorityOfferW(auctionInfo.getString("priorityOfferW"));
                beanCheYiPaiCarInfo.setC_priorityOfferY(auctionInfo.getString("priorityOfferY"));
                beanCheYiPaiCarInfo.setC_procureStatus(auctionInfo.getString("procureStatus"));
                beanCheYiPaiCarInfo.setC_promiseTag(auctionInfo.getString("promiseTag"));
                beanCheYiPaiCarInfo.setC_residueBidCount(auctionInfo.getString("residueBidCount"));
                beanCheYiPaiCarInfo.setC_riskHighestPriceY(auctionInfo.getString("riskHighestPriceY"));
                beanCheYiPaiCarInfo.setC_roundId(auctionInfo.getString("roundId"));
                beanCheYiPaiCarInfo.setC_roundPackType(auctionInfo.getString("roundPackType"));
                beanCheYiPaiCarInfo.setC_showBasePrice(auctionInfo.getString("showBasePrice"));
                beanCheYiPaiCarInfo.setC_showPlatformCommission(auctionInfo.getString("showPlatformCommission"));
                beanCheYiPaiCarInfo.setC_showStoreCommission(auctionInfo.getString("showStoreCommission"));
                beanCheYiPaiCarInfo.setC_status(auctionInfo.getString("status"));
                beanCheYiPaiCarInfo.setC_statusName(auctionInfo.getString("statusName"));
                beanCheYiPaiCarInfo.setC_stopEndTime(auctionInfo.getString("stopEndTime"));
                beanCheYiPaiCarInfo.setC_stoppingTag(auctionInfo.getString("stoppingTag"));
                beanCheYiPaiCarInfo.setC_storeCode(auctionInfo.getString("storeCode"));
                beanCheYiPaiCarInfo.setC_tenderStageFlag(auctionInfo.getString("tenderStageFlag"));
                beanCheYiPaiCarInfo.setC_timeDesc(auctionInfo.getString("timeDesc"));
                beanCheYiPaiCarInfo.setC_timeStr(auctionInfo.getString("timeStr"));

                JSONObject carInfo = jsonObject1.getJSONObject("carInfo");
                beanCheYiPaiCarInfo.setC_brand(carInfo.getString("brand"));
                beanCheYiPaiCarInfo.setC_browerStatus(carInfo.getString("browerStatus"));
                beanCheYiPaiCarInfo.setC_carName(carInfo.getString("carName"));
                beanCheYiPaiCarInfo.setC_carmodelName(carInfo.getString("carmodelName"));
                beanCheYiPaiCarInfo.setC_emissionStandard(carInfo.getString("emissionStandard"));
                beanCheYiPaiCarInfo.setC_firstImage(carInfo.getString("firstImage"));
                beanCheYiPaiCarInfo.setC_firstRowName(carInfo.getString("firstRowName"));
                beanCheYiPaiCarInfo.setC_goodsId(carInfo.getString("goodsId"));
                beanCheYiPaiCarInfo.setC_hasCarConditionVideo(carInfo.getString("hasCarConditionVideo"));
                beanCheYiPaiCarInfo.setC_licenseAbbreviation(carInfo.getString("licenseAbbreviation"));
                beanCheYiPaiCarInfo.setC_locationCode(carInfo.getString("locationCode"));
                beanCheYiPaiCarInfo.setC_locationName(carInfo.getString("locationName"));
                beanCheYiPaiCarInfo.setC_mileage(carInfo.getString("mileage"));
                beanCheYiPaiCarInfo.setC_offSiteCarName(carInfo.getString("offSiteCarName"));
                beanCheYiPaiCarInfo.setC_offsiteCar(carInfo.getString("offsiteCar"));
                beanCheYiPaiCarInfo.setC_rank(carInfo.getString("rank"));
                beanCheYiPaiCarInfo.setC_registDate(carInfo.getString("registDate"));
                beanCheYiPaiCarInfo.setC_registDateDesc(carInfo.getString("registDateDesc"));
                beanCheYiPaiCarInfo.setC_reportTemplateCode(carInfo.getString("reportTemplateCode"));
                beanCheYiPaiCarInfo.setC_secondRowName(carInfo.getString("secondRowName"));
                beanCheYiPaiCarInfo.setC_seriesId(carInfo.getString("seriesId"));
                beanCheYiPaiCarInfo.setC_seriesName(carInfo.getString("seriesName"));
                beanCheYiPaiCarInfo.setC_storeCode(carInfo.getString("storeCode"));
                beanCheYiPaiCarInfo.setC_tradeCode(carInfo.getString("tradeCode"));
                beanCheYiPaiCarInfo.setC_transferCount(carInfo.getString("transferCount"));
                beanCheYiPaiCarInfo.setC_transferCountDesc(carInfo.getString("transferCountDesc"));
                beanCheYiPaiCarInfo.setC_vehicleGradeBQ(carInfo.getString("vehicleGradeBQ"));
                beanCheYiPaiCarInfo.setC_vehicleGradeFGWS(carInfo.getString("vehicleGradeFGWS"));
                beanCheYiPaiCarInfo.setC_vehicleGradeSLBZ(carInfo.getString("vehicleGradeSLBZ"));
                beanCheYiPaiCarInfo.setC_vehicleGradeSSS(carInfo.getString("vehicleGradeSSS"));
                beanCheYiPaiCarInfo.setC_vehicleGradeYBYQ(carInfo.getString("vehicleGradeYBYQ"));

                JSONObject roundInfo = jsonObject1.getJSONObject("roundInfo");
                beanCheYiPaiCarInfo.setC_productNum(roundInfo.getString("productNum"));
                beanCheYiPaiCarInfo.setC_roundId(roundInfo.getString("roundId"));
                beanCheYiPaiCarInfo.setC_roundName(roundInfo.getString("roundName"));
                beanCheYiPaiCarInfo.setC_roundPic(roundInfo.getString("roundPic"));
                beanCheYiPaiCarInfo.setC_registDate(roundInfo.getString("roundStartDate"));
                beanList.add(beanCheYiPaiCarInfo);
            }
        }
        return beanList;
    }


    public Integer Method_SaveData(List<BeanCheYiPaiCarInfo> beanList){
        Integer count = 0;
        DaoFather daoFather = new DaoFather(0,0);
        // 仅仅查询 一个字段的List
        List<String> columnList = Collections.singletonList("C_goodsId");
        List<Map<String,String>> list = daoFather.methodFindFree(columnList);
        List<String> storeCodeList = list.stream().map(map -> map.get("C_goodsId")).collect(Collectors.toList());


        // 对传入的数据自己本身进行一次去重
        List<BeanCheYiPaiCarInfo> distinctBeanList = new ArrayList<>(beanList.stream().collect(Collectors.toMap(
                        BeanCheYiPaiCarInfo::getC_goodsId,
                        beanCheYiPaiCarInfo -> beanCheYiPaiCarInfo,
                        (bean1, bean2) -> bean1))
                .values());


        for (BeanCheYiPaiCarInfo beanCheYiPaiCarInfo : distinctBeanList) {
            String goodsId = beanCheYiPaiCarInfo.getC_goodsId();
            if (storeCodeList.contains(goodsId)){
                System.out.println("数据已经存在");
            }else {
                daoFather.Method_Insert(beanCheYiPaiCarInfo);
                count++;
            }
        }
        System.out.println("本次数据入库完成"+ DateUtil.format(DateUtil.date(), "yyyyMMdd-HH:mm:ss"));
        return count;
    }

}