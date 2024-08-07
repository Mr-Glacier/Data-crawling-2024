package zedata.com.runmain;

import zedata.com.controller.ControllerYouXinPai;
import zedata.com.until.SaveUntil;

/**
 * @author Mr-Glacier
 */
public class YouXinPaiMain {
    public static void main(String[] args) {
        String mainPath = "D:\\ZEDATA_2024\\汽车后市场数据\\二手车\\优信拍\\";
        String firstUrl = "https://www.youxinpai.com/trade/getTradeList";
        String parmStr = "{\"entities\":\"{\\\"req\\\":{\\\"cityIds\\\":[],\\\"serialIds\\\":[],\\\"appearanceGrades\\\":[],\\\"skeletonGrades\\\":[],\\\"interiorGrades\\\":[],\\\"emissionStandards\\\":[],\\\"carPriceLevel\\\":[],\\\"carYearLevel\\\":[],\\\"carGearbox\\\":[],\\\"carOwners\\\":[],\\\"carUseTypes\\\":[],\\\"fuelTypes\\\":[],\\\"conditionPriceType\\\":[],\\\"transferCounts\\\":[],\\\"startPriceType\\\":[],\\\"isNotBubbleCar\\\":false,\\\"isNotBurnCar\\\":false,\\\"isNotSmallReport\\\":false,\\\"orderFields\\\":10},\\\"page\\\":[{\\\"page\\\":1,\\\"pageSize\\\":2,\\\"pageTab\\\":\\\"pc_circle\\\"},{\\\"page\\\":1,\\\"pageSize\\\":15,\\\"pageTab\\\":\\\"immediately\\\"},{\\\"page\\\":1,\\\"pageSize\\\":2,\\\"pageTab\\\":\\\"delay\\\"},{\\\"page\\\":1,\\\"pageSize\\\":2,\\\"pageTab\\\":\\\"fixedPrice\\\"},{\\\"page\\\":1,\\\"pageSize\\\":2,\\\"pageTab\\\":\\\"benz\\\"},{\\\"page\\\":1,\\\"pageSize\\\":2,\\\"pageTab\\\":\\\"attention\\\"}]}\"}";

        ControllerYouXinPai controllerYouXinPai = new ControllerYouXinPai();
        SaveUntil saveUntil = new SaveUntil();

        // 拼接Cookie-->存在几个 过期时间较长的字段,采取记录形式
        // 1.xxzlclientid 过期时间 2025-08-04T14:01:27.000Z
        String xxzlclientid = "7a923008-bbf8-4b78-b1f2-1717568308111";
        // 2. xxzlxxid 过期时间 2025-08-04T14:01:27.000Z
        String xxzlxxid = "pfmxwtDfh9JN7gAwBs330bDz99xCin6xXlLaiBRy0BNp5uBTsaB9cJPDll/1KrKTlE4J";
        // 3. xxzlbbid 过期时间 2025-08-04T15:08:56.000Z
        String xxzlbbid = "pfmbM3wxMDI5M3wxLjcuMHwxNzIyNzg0MTM2NTcwfDVwaEo4NlNXaWcrV1pSSzFlLytoTkdrZEsvbS9lK0UweCtHUWxSVHU0eG89fDNkNTdiMmQwNzI2NmU4N2MxNjcxMTFkZmRiMjAwZmRmXzE3MjI3ODQxMzMxMzdfOTRkYjQ0OTJkNDA3NDhhODkwNzlmOWVmMzQ5M2Q2OGRfMTk3NDQ0NzIwMXwwN2ExMmI0YThmZWEyMWMwMTYxZjM3OWI4ODczMjVmNF8xNzIyNzg0MTM1MDIzXzI1Ng==";
        // 4. id58 过期时间很长
        String id58 = "CocLxWZidCqUywmePIHHAg==";

        //xxzlclientid=7a923008-bbf8-4b78-b1f2-1717568308111;
        //xxzlxxid=pfmxwtDfh9JN7gAwBs330bDz99xCin6xXlLaiBRy0BNp5uBTsaB9cJPDll/1KrKTlE4J;
        //id58=CocLxWZidCqUywmePIHHAg==;
        //csrfToken_key=uXCwsyKHy4Li1AlIfV4Awu6n;
        //csrfToken=l3aRjjmA-4pag7HrlXWLp9-D8PtiRs_yVOq4; jwt_token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjoibDNhUmpqbUEtNHBhZzdIcmxYV0xwOS1EOFB0aVJzX3lWT3E0IiwiaWF0IjoxNzIyOTM2OTQ0LCJleHAiOjE3MjI5Mzg3NDR9.xZ59vtZzLNVNopsGAnCZIOmxxYVV7EH2cztPMe8BRmc; xxzlbbid=pfmbM3wxMDI5M3wxLjcuMXwxNzIyOTM2OTQ2NTIzfGVPQ2hBTjBLUmF3ZjQ1US9jR3FndTdyT1A2emlQMjFUZFM1TER0aWdEdGc9fDhhMWI1MDE3YWY1Y2Y0YmU1ZmI4N2FlM2I2NzIzYzU3XzE3MjI5MTEwNDg0NDZfMjY5YjJiMTM4MmEzNDU4YmExMDAxMDAwMjY3YjhjNjJfMTk3NDQ0NzIwMXwwN2RkY2FhY2FiNmE0MjE0Y2RmNTgzZGY2MDQwMmE1OF8xNzIyOTM2OTQ0NTEwXzEzOQ==

        String tokenStr = "xxzlclientid="+xxzlclientid+"; " +
                "xxzlxxid="+xxzlxxid+"; " +
                "id58="+id58+"; " +
                "xxzlbbid="+xxzlbbid+"; " +
                "csrfToken_key=csrfTokenKeyStr; " +
                "csrfToken=csrfTokenStr; " +
                "jwt_token=JwtTokenStr";

        // jwt_token 规则,前面 Base64,后面加密使用的是 HS256  密钥 10293

        String content = controllerYouXinPai.methodDownFirstHtml(firstUrl,parmStr);
        saveUntil.Method_SaveFile(mainPath+"firstHtml.html",content);
    }
}
