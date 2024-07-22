package com.mysite.sbb.apistudy;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

@Controller
public class HomeController {

    @Autowired
    private SubstationInfoRepository infoRepository;

    @GetMapping("/study/api")
    public String index() {
        return "apistudy";
    }

    @PostMapping("/study/api")
    public String load_save(@RequestParam("date") String date, Model model){
        String result = "";

        try {
            String requestDate=date;
            URL url = new URL("http://openapi.seoul.go.kr:8088/" + "4541754d45616c733130365059564846/" + "json/CardSubwayStatsNew/1/100/"+requestDate);
            BufferedReader bf;
            bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            result = bf.readLine();

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject)jsonParser.parse(result);
//            System.out.println(result);
//            System.out.println(jsonObject);
            JSONObject cardSubwayStatsNew = (JSONObject)jsonObject.get("CardSubwayStatsNew");
//            System.out.println(cardSubwayStatsNew);
            JSONArray infoArr = (JSONArray)cardSubwayStatsNew.get("row");
//            System.out.println(infoArr);

            for(int i=0;i<infoArr.size();i++){
                JSONObject tmp = (JSONObject)infoArr.get(i);
                SubstationInfo infoObj = new SubstationInfo(
                        i+(long)1,
                        (String)tmp.get("USE_YMD"),
                        (String)tmp.get("SBWY_ROUT_LN_NM"),
                        (String)tmp.get("SBWY_STNS_NM"),
                        (double)tmp.get("GTON_TNOPE"),
                        (double)tmp.get("GTOFF_TNOPE"),
                        (String)tmp.get("REG_YMD"));
                infoRepository.save(infoObj);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/findname";
    }
}
