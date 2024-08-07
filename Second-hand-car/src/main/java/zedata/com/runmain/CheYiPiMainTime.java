package zedata.com.runmain;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import zedata.com.controller.ControllerCheYiPai;
import zedata.com.entity.BeanCheYiPaiCarInfo;
import zedata.com.until.HelpCreateFile;
import zedata.com.until.SaveUntil;
import zedata.com.until.SentEmail;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author Mr-Glacier
 * @apiNote 定时任务爬取数据 车易拍定为1个小时爬取一次,但是一天进行一次入库
 */
public class CheYiPiMainTime {
    public static void main(String[] args) {
        // 创建一个调度线程池，大小为1
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // 数据主要存储位置
        String mainPath = "D:\\ZEDATA_2024\\汽车后市场数据\\二手车\\车易拍定时任务数据\\";

        ControllerCheYiPai controllerCheYiPai = new ControllerCheYiPai();
        SaveUntil saveUntil = new SaveUntil();

        // 请求入参
        String jsonData = "{\"lastAuctionId\":\"1470725242541113345\",\"pageIndex\":1,\"pageSize\":10,\"activityCode\":\"\",\"pageType\":3,\"auctionScreenListQuery\":{\"quickConditionInfoVOS\":[]}}";
        String cookie = "JSESSIONID=0CC3C6DEDCB0D99875975A6377BF47D8; acw_tc=b65cfd2c17177396994197960e6dd3f4a85b41edde571f6b13e60b55b2e3f0";
        // 车易拍的请求路径
        String mainUrl = "https://shenyu.cheyipai.com/auction/biz/auctionListController/getAuctionCarList.json";

        Runnable task = () -> {
            try{
                // 构建一个集合存储本次下载解析的数据
                List<BeanCheYiPaiCarInfo> beanList = new ArrayList<>();
                String messageBegin = "定时任务开始执行 : \t" + DateUtil.format(DateUtil.date(), "yyyyMMdd-HH mm SS");
                System.out.println(messageBegin);
                saveUntil.Method_SaveFile_True("F:\\ZKZD\\Java项目\\Data-crawling-2024\\Second-hand-car\\src\\main\\resources\\cheyipaiSUCCESSLog.txt", messageBegin+"\n");
                // 获取当天存储时间 yyyyMMdd
                String currentDateStr = DateUtil.format(DateUtil.date(), "yyyyMMdd_HH");
                String savePath = mainPath + currentDateStr+"\\";
                // 如果文件夹不存在 就创建
                HelpCreateFile.Method_Creat_folder(savePath);

                // 首先请求第一个文件
                String data = controllerCheYiPai.Method_PostData(mainUrl, jsonData, cookie);
                saveUntil.Method_SaveFile(savePath + "车易拍_1.txt", data);
                JSONObject jsonObject = JSONObject.parseObject(data).getJSONObject("data");

                // 记录第一个文件的List
                List<BeanCheYiPaiCarInfo> beanListFirst = controllerCheYiPai.methodAnalysisData(data);
                beanList.addAll(beanListFirst);
                // 记录本次请求的所有总数
                int total = jsonObject.getInteger("auctionInfoCount");
                int quotient = total / 10;
                int remainder = total % 10;
                if (remainder != 0) {
                    quotient++;
                }
                System.out.println("本次下载 ----> 共" + quotient + "页");
                saveUntil.Method_SaveFile_True("F:\\ZKZD\\Java项目\\Data-crawling-2024\\Second-hand-car\\src\\main\\resources\\cheyipaiSUCCESSLog.txt", "本次下载 ----> 共" + quotient + "页\n");
                // 开始循环下载数据
                for (int i = 2; i <= quotient; i++) {
                    System.out.println("第" + i + "页");
                    String jsonDataNew = jsonData.replace("\"pageIndex\":1", "\"pageIndex\":" + i);
                    String dataNew = controllerCheYiPai.Method_PostData(mainUrl, jsonDataNew, cookie);
                    JSONObject jsonObjecNew = JSONObject.parseObject(dataNew).getJSONObject("data");
                    JSONArray jsonArrayNew = jsonObjecNew.getJSONArray("auctionGoodsRoundVOList");
                    if (jsonArrayNew.size() != 0) {
                        saveUntil.Method_SaveFile(savePath + "车易拍_" + i + ".txt", dataNew);
                        List<BeanCheYiPaiCarInfo> beanListOthers = controllerCheYiPai.methodAnalysisData(dataNew);
                        beanList.addAll(beanListOthers);
                    }
                }
                beanList.forEach(bean -> {
                    bean.setC_wareHouseTime(currentDateStr);
                });

                Integer result = controllerCheYiPai.Method_SaveData(beanList);
                String endMessage = "定时任务结束执行 : \t" + DateUtil.format(DateUtil.date(), "yyyyMMdd-HH mm SS") + "\t" + result + "条数据入库\n" +
                        "========================================================================================\n";
                saveUntil.Method_SaveFile_True("F:\\ZKZD\\Java项目\\Data-crawling-2024\\Second-hand-car\\src\\main\\resources\\cheyipaiSUCCESSLog.txt", endMessage);
            }catch (Exception e){
                String message = "定时任务执行失败 : \t" + DateUtil.format(DateUtil.date(), "yyyyMMdd-HH mm SS") + "\t" + e.getMessage() + "\n";
                saveUntil.Method_SaveFile_True("F:\\ZKZD\\Java项目\\Data-crawling-2024\\Second-hand-car\\src\\main\\resources\\cheyipaiErrorLog.txt", message);
                SentEmail.Method_SentEmail(0,message);
                e.printStackTrace();
            }
        };

        // 初始延迟为0，之后每4小时执行一次
        scheduler.scheduleAtFixedRate(task, 0, 4, TimeUnit.HOURS);

        // 添加一个钩子在应用终止时关闭调度器
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down scheduler...");
            scheduler.shutdown();
            try {
                if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                    System.out.println("Scheduler did not terminate in the specified time.");
                    List<Runnable> droppedTasks = scheduler.shutdownNow();
                    System.out.println("Scheduler was abruptly shut down. " + droppedTasks.size() + " tasks will not be executed.");
                }
            } catch (InterruptedException e) {
                System.err.println("Shutdown interrupted: " + e.getMessage());
                scheduler.shutdownNow();
            }
            System.out.println("Scheduler shutdown complete.");
        }));
    }
}
