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
import java.util.List;

@Controller
public class SubstationInfoController {

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
            URL url = new URL("http://openapi.seoul.go.kr:8088/" + "4541754d45616c733130365059564846/" + "json/CardSubwayStatsNew/1/700/"+requestDate);
            BufferedReader bf;
            bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            result = bf.readLine();

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject)jsonParser.parse(result);
            JSONObject cardSubwayStatsNew = (JSONObject)jsonObject.get("CardSubwayStatsNew");
            JSONArray infoArr = (JSONArray)cardSubwayStatsNew.get("row");

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

    @GetMapping("findname")
    public String input_name(Model model){
        return "findname";
    }

    @PostMapping("findname")
    public String stationName(@RequestParam(value = "station_name",required = true)  String stationName, Model model){
        List<SubstationInfo> infoList=infoRepository.findByName(stationName);

        if(infoList.isEmpty()==true){//없는 역이름이면 재입력 요구
            model.addAttribute("msg","역 이름을 찾을 수 없습니다. 확인 후 재입력해주세요.");
            return "findname";
        }else {//유효한 역이름이면 결과 반환
            model.addAttribute("station_name",stationName);
            return "result";
        }
    }

    @GetMapping("findline")
    public String input_line(Model model) {
        return "findline";
    }

    @PostMapping("findline")
    public String linenum(@RequestParam(value = "line_num",required = true) String linenum, Model model) {
        List<SubstationInfo> infoList=infoRepository.findByLine(linenum);

        if(infoList.isEmpty()==true){
            model.addAttribute("msg", "호선의 이름을 찾을 수 없습니다. 확인 후 재입력해주세요");
            return "findline";
        } else {
            model.addAttribute("line_num",linenum);
            return "resultline";
        }
    }
}
