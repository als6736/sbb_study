package com.mysite.sbb.ela;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ElaDataService {
    @Value("${logstash.url}") //application.properties 파일에서 logstash.url 값을 읽어옴
    private String logstashUrl; //이 변수에 저장함

    private final RestTemplate restTemplate; //인스턴스 저장 변수, 변하지 않고, 주입시 초기화됨

    public ElaDataService(RestTemplate restTemplate) { //생성자 RestTemplate 인스턴스를 주입받아 restTemplate 변수에 할당
        this.restTemplate = restTemplate;
    }

    public void fetchDataAndSendToLogstash() {
        String apiUrl = "http://openapi.seoul.go.kr:8088/4541754d45616c733130365059564846/json/CardSubwayStatsNew/1/5/20200413";
        ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            sendDataToLogstash(response.getBody());
        } else {
            System.err.println("Failed to fetch data from API");
        }
    }

    public void sendDataToLogstash(Object data) { //logstash로 데이터 전송 메서드
        HttpHeaders headers = new HttpHeaders(); //해더설정을 위해 객체 생성
        headers.setContentType(MediaType.APPLICATION_JSON); //JSON으로 설정

        HttpEntity<Object> request = new HttpEntity<>(data, headers); //요청 본문과 헤더를 포함하는 HttpEntity 객체 생성
        ResponseEntity<String> response = restTemplate.exchange(logstashUrl, HttpMethod.POST, request, String.class); //url post httpEntity를 보내고  응답은 String 타입

        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("Data sent to Logstash successfully"); //성공시
        } else {
            System.err.println("Failed to send data to Logstash"); //실패시
        }
    }
}
