package zedata.com.runmain;

import zedata.com.controller.ControllerCheYiPai;
import zedata.com.entity.BeanCheYiPaiCarInfo;
import zedata.com.until.ReadFileUtil;
import zedata.com.until.ReadUntil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CheyipaiTempMain {

    public static void main(String[] args) {

        ReadUntil readFileUtil = new ReadUntil();
        ControllerCheYiPai controllerCheYiPai = new ControllerCheYiPai();

        String mainPath = "D:\\ZEDATA_2024\\汽车后市场数据\\二手车\\车易拍定时任务数据\\";
        List<String> list = Arrays.asList("20240803_02", "20240803_06", "20240803_10", "20240803_14", "20240803_18", "20240803_22", "20240804_02", "20240804_06", "20240804_10", "20240804_14");
        list.forEach(s -> {
            System.out.println("开始解析"+s);
            List<String> fileNames = readFileUtil.getFileNames(mainPath+s+"\\");
            List<BeanCheYiPaiCarInfo> beanList = new ArrayList<>();
            fileNames.forEach(fileName -> {
                String Content  = readFileUtil.Method_ReadFile(mainPath+s+"\\"+fileName);
                List<BeanCheYiPaiCarInfo> beanListOthers = controllerCheYiPai.methodAnalysisData(Content);
                beanList.addAll(beanListOthers);
            });
            beanList.forEach(bean -> {
                bean.setC_wareHouseTime(s);
            });
            Integer result = controllerCheYiPai.Method_SaveData(beanList);
            System.out.println(s+ "   本次存入数据-->"+result);
            beanList.clear();
        });

    }

}
