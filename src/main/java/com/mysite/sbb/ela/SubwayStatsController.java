package com.mysite.sbb.ela;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

@Controller
public class SubwayStatsController {

    @Autowired
    private SubwayStatsService subwayStatsService;

    @GetMapping("/statsList")
    public String getSubwayStats(Model model,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "1000") int size) {
        List<SubwayStats> statsList = subwayStatsService.getAllStats(page, size);
        model.addAttribute("stats", statsList);
        return "statsList";
    }

    @PostMapping("/study/api")
    public String load_save(@RequestParam("date") String date) {
        try {
            String requestDate = date;
            URL url = new URL("http://openapi.seoul.go.kr:8088/4541754d45616c733130365059564846/xml/CardSubwayStatsNew/1/700/" + requestDate);

            // 데이터를 Logstash로 전송
            sendToLogstash(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/subway/list";
    }

    private void sendToLogstash(URL url) {
        try (BufferedReader bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"))) {
            String line;
            while ((line = bf.readLine()) != null) {
                // Logstash로 데이터 전송
                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_XML);
                HttpEntity<String> request = new HttpEntity<>(line, headers);

                String logstashUrl = "http://localhost:5000";  // Logstash가 수신하는 포트와 URL
                restTemplate.postForObject(logstashUrl, request, String.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/subway/list")
    public String getSubwayList(Model model) {
        // 예시로 전체 데이터를 가져옵니다. 필요에 따라 특정 조건으로 필터링할 수 있습니다.
        List<SubwayStats> infoList = subwayStatsService.getAllData();
        model.addAttribute("infoList", infoList);
        return "subway_list";  // 렌더링할 템플릿 이름 (subway_list.html)
    }
}
