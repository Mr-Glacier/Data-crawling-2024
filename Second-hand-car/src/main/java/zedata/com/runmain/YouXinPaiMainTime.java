package zedata.com.runmain;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import zedata.com.controller.ControllerYouXinPai;
import zedata.com.entity.BeanYouXinPaiCarInfo;
import zedata.com.until.HelpCreateFile;
import zedata.com.until.SaveUntil;
import zedata.com.until.SentEmail;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Mr-Glacier
 * @apiNote 优信拍定时任务
 */
public class YouXinPaiMainTime {

    /**
     * 在外层定义 部分参数
     */

    public static void main(String[] args) {
        // 创建一个调度线程池，大小为1
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        ControllerYouXinPai controllerYouXinPai = new ControllerYouXinPai();
        SaveUntil saveUntil = new SaveUntil();

        // mainUrl 主要请求Url
        String mainUrl = "https://www.youxinpai.com/trade/getTradeList";

        // 项目主要存储路径
        String mainPath = "D:\\ZEDATA_2024\\汽车后市场数据\\二手车\\优信拍定时任务数据\\";

        // 项目日志记录路径
        String LogPath = "F:\\ZKZD\\Java项目\\Data-crawling-2024\\Second-hand-car\\src\\main\\resources\\youxinpaiLog\\";

        // 定时任务内执行逻辑
        Runnable task = () -> {

            // 拼接Cookie-->存在几个 过期时间较长的字段,采取记录形式
            // 1.xxzlclientid 过期时间 2025-08-04T
            String xxzlclientid = "7a923008-bbf8-4b78-b1f2-1717568308111";
            // 2. xxzlxxid 过期时间 2025-08-04T
            String xxzlxxid = "pfmxwtDfh9JN7gAwBs330bDz99xCin6xXlLaiBRy0BNp5uBTsaB9cJPDll/1KrKTlE4J";
            // 3. xxzlbbid 过期时间 2025-08-04T
            String xxzlbbid = "pfmbM3wxMDI5M3wxLjcuMHwxNzIyNzg0MTM2NTcwfDVwaEo4NlNXaWcrV1pSSzFlLytoTkdrZEsvbS9lK0UweCtHUWxSVHU0eG89fDNkNTdiMmQwNzI2NmU4N2MxNjcxMTFkZmRiMjAwZmRmXzE3MjI3ODQxMzMxMzdfOTRkYjQ0OTJkNDA3NDhhODkwNzlmOWVmMzQ5M2Q2OGRfMTk3NDQ0NzIwMXwwN2ExMmI0YThmZWEyMWMwMTYxZjM3OWI4ODczMjVmNF8xNzIyNzg0MTM1MDIzXzI1Ng==";
            // 4. id58 过期时间很长
            String id58 = "CocLxWZidCqUywmePIHHAg==";

            String csrfTokenKeyStr = "SLBf-bn7grfdPIOwVwcZHeDa";
            String csrfTokenStr = "Bbel06e5-nyW7m_EiyZ6Hbwa7GnZhyQoYItk";
            String jwtTokenStr = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjoiQmJlbDA2ZTUtbnlXN21fRWl5WjZIYndhN0duWmh5UW9ZSXRrIiwiaWF0IjoxNzIzMTk3Nzg5LCJleHAiOjE3MjMxOTk1ODl9.e1SChG7ux31RqNYLp6hBYlnPFnNqlvlyMQdNjJtSzQE";

            // cookie 模板
            String tokenStrModel = "xxzlclientid="+xxzlclientid+"; " +
                    "xxzlxxid="+xxzlxxid+"; " +
                    "id58="+id58+"; " +
                    "xxzlbbid="+xxzlbbid+"; " +
                    "csrfToken_key=csrfTokenKeyStr; " +
                    "csrfToken=csrfTokenStr; " +
                    "jwt_token=JwtTokenStr";
            // 构造 数据汇集
            List<BeanYouXinPaiCarInfo> beanList = new ArrayList<>();

            try{
                String currentDateStr = DateUtil.format(DateUtil.date(), "yyyyMMdd_HH");
                saveUntil.Method_SaveFile_True(LogPath+"youxinpaiSUCCESS.txt", "本次执行时间---> "+currentDateStr+"\n");
                String savePath = mainPath + currentDateStr+"\\";
                HelpCreateFile.Method_Creat_folder(savePath);
                int pageNum = 1;
                while (true){
                    String parmStr = "{\"entities\":\"{\\\"req\\\":{\\\"cityIds\\\":[],\\\"serialIds\\\":[],\\\"appearanceGrades\\\":[],\\\"skeletonGrades\\\":[],\\\"interiorGrades\\\":[],\\\"emissionStandards\\\":[],\\\"carPriceLevel\\\":[],\\\"carYearLevel\\\":[],\\\"carGearbox\\\":[],\\\"carOwners\\\":[],\\\"carUseTypes\\\":[],\\\"fuelTypes\\\":[],\\\"conditionPriceType\\\":[],\\\"transferCounts\\\":[],\\\"startPriceType\\\":[],\\\"isNotBubbleCar\\\":false,\\\"isNotBurnCar\\\":false,\\\"isNotSmallReport\\\":false,\\\"orderFields\\\":10},\\\"page\\\":[{\\\"page\\\":1,\\\"pageSize\\\":2,\\\"pageTab\\\":\\\"pc_circle\\\"},{\\\"page\\\":" + pageNum + ",\\\"pageSize\\\":15,\\\"pageTab\\\":\\\"immediately\\\"},{\\\"page\\\":1,\\\"pageSize\\\":2,\\\"pageTab\\\":\\\"delay\\\"},{\\\"page\\\":1,\\\"pageSize\\\":2,\\\"pageTab\\\":\\\"fixedPrice\\\"},{\\\"page\\\":1,\\\"pageSize\\\":2,\\\"pageTab\\\":\\\"benz\\\"},{\\\"page\\\":1,\\\"pageSize\\\":2,\\\"pageTab\\\":\\\"attention\\\"}]}\"}";
                    String tokenStr = tokenStrModel.replace("csrfTokenKeyStr",csrfTokenKeyStr)
                            .replace("csrfTokenStr",csrfTokenStr)
                            .replace("JwtTokenStr",jwtTokenStr);
                    Map<String,String> map = controllerYouXinPai.postRequestByHttpClient(mainUrl, parmStr, tokenStr);
                    if ("200".equals(map.get("httpStatus"))){
                        String responseBody = map.get("responseBody");
                        JSONObject jsonObject = JSONObject.parseObject(responseBody).getJSONObject("data").getJSONObject("entities").getJSONObject("immediately");
                        JSONArray jsonArray = jsonObject.getJSONArray("auctionListEntity");
                        if (jsonArray.size()==0){
                            break;
                        }else {
                            saveUntil.Method_SaveFile(savePath+pageNum+".txt",responseBody);
                            List<BeanYouXinPaiCarInfo> oneBeanList =  controllerYouXinPai.methodAnalysisData(responseBody,currentDateStr);
                            beanList.addAll(oneBeanList);
                            csrfTokenKeyStr = map.get("csrfTokenKey");
                            csrfTokenStr = map.get("csrfToken");
                            jwtTokenStr = map.get("jwtToken");
                            pageNum++;
                        }
                    }else{
                        break;
                    }
                }
                int insertNum = controllerYouXinPai.methodInsertData(beanList);
                saveUntil.Method_SaveFile_True(LogPath+"youxinpaiSUCCESS.txt", "本次共有 --->"+pageNum+"页数据\n" +
                        "本次入库数据共 --->"+insertNum+" 条数据\n" +
                        "===========================================================================================================");
            }catch (Exception e){
                e.printStackTrace();
                // 设置异常通知器
                SentEmail.Method_SentEmail(0,e.getMessage());
            }
        };

        // 初始延迟为0，之后每4小时执行一次 (优信拍的单个Token 过期时间未知 ,初步按照cookie 中设计的值来说 ,应该是4个小时) 设置定时时间为 3 个小时
        scheduler.scheduleAtFixedRate(task, 0, 3, TimeUnit.HOURS);

        // 添加一个钩子在应用终止时关闭调度器
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down scheduler...");
            scheduler.shutdown();
            try {
                if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                    List<Runnable> droppedTasks = scheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                System.err.println("Shutdown interrupted: " + e.getMessage());
                scheduler.shutdownNow();
            }
            System.out.println("Scheduler shutdown complete.-->调度任务结束");
        }));


    }
}
