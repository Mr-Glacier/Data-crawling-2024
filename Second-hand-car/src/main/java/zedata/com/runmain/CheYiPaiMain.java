package zedata.com.runmain;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import zedata.com.controller.ControllerCheYiPai;
import zedata.com.until.HelpCreateFile;
import zedata.com.until.SaveUntil;

/**
 * @author Mr-Glacier
 */
public class CheYiPaiMain {
    /**
     * 车易拍的启动类
     */
    public static void main(String[] args) {
        // 数据主要存储位置
        String mainPath = "D:\\ZEDATA_2024\\汽车后市场数据\\二手车\\车易拍数据\\";
        // 获取当天存储时间 yyyyMMdd
        String currentDateStr = DateUtil.format(DateUtil.date(), "yyyyMMdd_HH");
        String savePath = mainPath + currentDateStr+"\\";
        // 如果文件夹不存在 就创建
        HelpCreateFile.Method_Creat_folder(savePath);


        // 下载批次所需的入参
        String jsonData = "{\"lastAuctionId\":\"1470725242541113345\",\"pageIndex\":1,\"pageSize\":10,\"activityCode\":\"\",\"pageType\":3,\"auctionScreenListQuery\":{\"quickConditionInfoVOS\":[]}}";
        String cookie = "JSESSIONID=0CC3C6DEDCB0D99875975A6377BF47D8; acw_tc=b65cfd2c17177396994197960e6dd3f4a85b41edde571f6b13e60b55b2e3f0";

        // 车易拍的请求路径
        String mainUrl = "https://shenyu.cheyipai.com/auction/biz/auctionListController/getAuctionCarList.json";

        ControllerCheYiPai controllerCheYiPai = new ControllerCheYiPai();
        SaveUntil saveUntil = new SaveUntil();

        String data = controllerCheYiPai.Method_PostData(mainUrl, jsonData, cookie);
        JSONObject jsonObject = JSONObject.parseObject(data).getJSONObject("data");

        int total = jsonObject.getInteger("auctionInfoCount");


        saveUntil.Method_SaveFile(savePath + "车易拍_1.txt", data);
        // 计算商和余数
        int quotient = total / 10;
        int remainder = total % 10;

        if (remainder != 0) {
            quotient++;
        }
        System.out.println("共" + quotient + "页");
        for (int i = 2; i <= quotient; i++) {
            System.out.println("第" + i + "页");
            String jsonDataNew = jsonData.replace("\"pageIndex\":1", "\"pageIndex\":" + i);
            String dataNew = controllerCheYiPai.Method_PostData(mainUrl, jsonDataNew, cookie);
            JSONObject jsonObjecNew = JSONObject.parseObject(dataNew).getJSONObject("data");
            JSONArray jsonArrayNew = jsonObjecNew.getJSONArray("auctionGoodsRoundVOList");
            if (jsonArrayNew.size() != 0) {
                saveUntil.Method_SaveFile(savePath + "车易拍_" + i + ".txt", dataNew);
            }
        }


        // 入库
//        controllerCheYiPai.Method_AnalysisData(savePath,"2024060712");


    }
}
