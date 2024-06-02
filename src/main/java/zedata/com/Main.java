package zedata.com;

import zedata.com.Controller.etmocController;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        etmocController etmocController = new etmocController();
        String main_Url = "http://www.etmoc.com/Firms/BrandAll";
        String allBrandHtml =etmocController.Method_1_RequestBrandHtml(main_Url);
        etmocController.Method_2_AnalysisAllBrandHtml(allBrandHtml);
    }
}