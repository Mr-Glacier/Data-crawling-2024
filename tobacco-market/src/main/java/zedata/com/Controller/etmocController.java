package zedata.com.Controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import zedata.com.Main;
import zedata.com.Until.HttpUntil;
import zedata.com.Until.ReadFileUtil;
import zedata.com.Until.ReadUntil;
import zedata.com.Until.SaveUntil;

import javax.jws.soap.SOAPBinding;
import java.util.Map;

public class etmocController {
    private SaveUntil saveUntil = new SaveUntil();
    private ReadUntil readUntil = new ReadUntil();
    private HttpUntil httpUntil = new HttpUntil();

    public String Method_1_RequestBrandHtml(String brandUrl){
        Map<String, String> Map = httpUntil.Method_Jsoup_request_get(brandUrl, null);
        return Map.get("result");
    }

    public void Method_2_AnalysisAllBrandHtml(String allBrandHtml){
        Document elements = Jsoup.parse(allBrandHtml);
        Elements allBrand = elements.select("div.col-8");

        Elements allBrands = allBrand.select(".list-inline.detail").select("li");
        for (int i = 0; i < allBrands.size(); i++){
            Element brand = allBrands.get(i);
            String brandUrl = brand.select("a").attr("href");
            System.out.println( brand.select("a").text());
            System.out.println(brandUrl);
        }

    }

}
