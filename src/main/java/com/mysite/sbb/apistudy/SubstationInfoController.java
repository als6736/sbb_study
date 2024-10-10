package com.mysite.sbb.apistudy;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class SubstationInfoController {

    @Autowired
    private SubstationInfoRepository infoRepository;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    @GetMapping("/study/api")
    public String index() {
        return "apistudy";
    }

    //@PostMapping("/study/api")
    public String load_save(@RequestParam("date") String date, Model model) {
        String result = "";

        try {
            String requestDate = date;
            URL url = new URL("http://openapi.seoul.go.kr:8088/4541754d45616c733130365059564846/json/CardSubwayStatsNew/1/700/" + requestDate);
            BufferedReader bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            result = bf.readLine();

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
            JSONObject cardSubwayStatsNew = (JSONObject) jsonObject.get("CardSubwayStatsNew");
            JSONArray infoArr = (JSONArray) cardSubwayStatsNew.get("row");

            for (int i = 0; i < infoArr.size(); i++) {
                JSONObject tmp = (JSONObject) infoArr.get(i);
                Long useYmd = tmp.get("USE_YMD") != null ? dateFormat.parse((String) tmp.get("USE_YMD")).getTime() : null;
                Long regYmd = tmp.get("REG_YMD") != null ? dateFormat.parse((String) tmp.get("REG_YMD")).getTime() : null;
                String id = UUID.randomUUID().toString();
                SubstationInfo infoObj = new SubstationInfo(
                        id,
                        useYmd,
                        (String) tmp.get("SBWY_ROUT_LN_NM"),
                        (String) tmp.get("SBWY_STNS_NM"),
                        Double.parseDouble(tmp.get("GTON_TNOPE").toString()),
                        Double.parseDouble(tmp.get("GTOFF_TNOPE").toString()),
                        regYmd
                );
//                System.out.println(infoObj);
//                infoRepository.save(infoObj);
                // Logstash에 데이터 전송
                sendToLogstash(infoObj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/study/api/all";
    }

    private void sendToLogstash(SubstationInfo infoObj) {
        try {
            // ObjectMapper를 사용해 SubstationInfo 객체를 JSON 문자열로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonPayload = objectMapper.writeValueAsString(infoObj);

            // Logstash에 데이터 전송
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> request = new HttpEntity<>(jsonPayload, headers);
            String logstashUrl = "http://localhost:5000";  // Logstash가 수신하는 포트와 URL

            ResponseEntity<String> response = restTemplate.exchange(logstashUrl, HttpMethod.POST, request, String.class);

            // 전송 후 응답 확인
            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("Data sent successfully to Logstash.");
            } else {
                System.err.println("Failed to send data to Logstash. Status Code: " + response.getStatusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/study/api/all")
    public String getAllData(Model model) {
        List<SubstationInfo> infoList = infoRepository.findByUseYmdIsNotNullAndRegYmdIsNotNull();

        List<SubstationInfoDTO> dtoList = infoList.stream().map(info -> new SubstationInfoDTO(
                info.getId(),
                new SimpleDateFormat("yyyy-MM-dd").format(new Date(info.getUseYmd())),
                info.getSbwyRoutLnNm(),
                info.getSbwyStnsNm(),
                info.getGtonTnope(),
                info.getGtoffTnope(),
                new SimpleDateFormat("yyyy-MM-dd").format(new Date(info.getRegYmd()))
        )).collect(Collectors.toList());

        model.addAttribute("infoList", dtoList);
        return "allData";
    }

    @PostMapping("/search")
    public String searchByStationName(@RequestParam("stationName") String stationName, Model model) {
        List<SubstationInfo> infoList = infoRepository.findBySbwyStnsNm(stationName).stream()
                .filter(info -> info.getUseYmd() != null && info.getRegYmd() != null)
                .collect(Collectors.toList());

        List<SubstationInfoDTO> dtoList = infoList.stream().map(info -> new SubstationInfoDTO(
                info.getId(),
                new SimpleDateFormat("yyyy-MM-dd").format(new Date(info.getUseYmd())),
                info.getSbwyRoutLnNm(),
                info.getSbwyStnsNm(),
                info.getGtonTnope(),
                info.getGtoffTnope(),
                new SimpleDateFormat("yyyy-MM-dd").format(new Date(info.getRegYmd()))
        )).collect(Collectors.toList());

        model.addAttribute("infoList", dtoList);
        return "searchResults";
    }
}
