package com.mysite.sbb.apistudy;

import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;


@RequiredArgsConstructor
@RestController
public class OpenApiController {

    private final OpenApiRepository openApiRepository;

    @RequestMapping("/api")
    public String save() throws IOException {
        String result = "";

        try {
            String urlStr = "http://apis.data.go.kr/B551011/KorWithService1/OpenApiEntity?serviceKey=4LqSiJ43rR1R3tILUsQH5DhjRuggzTiNcWOB%2FIEaR5%2F19iVHNvYo2dbLe%2BETZxa%2BztkxvrTrlwWtt03mMkGu5Q%3D%3D&numOfRows=25&pageNo=1&MobileOS=ETC&MobileApp=AppTest&areaCode=1&_type=json";
            URL url = new URL(urlStr);
            BufferedReader br;
            br = new BufferedReader(new InputStreamReader(url.openStream(),"UTF-8"));

            result = br.readLine();

            //JSON 파싱 객체를 생성
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
            //response를 받아옴
            JSONObject parseResponse = (JSONObject) jsonObject.get("response");
            //parseResponse에는 response 내부의 데이터들이 들어있음

            //body를 받아옴
            JSONObject parseBody = (JSONObject) parseResponse.get("body");
            //parseBody에는 body 내부의 데이터들이 들어있음

            //Items를 받아옴
            JSONObject parseItems = (JSONObject) parseBody.get("items");
            //parseItems에는 items 내부의 데이터들이 들어있음

            //item 안쪽의 데이터는 [] 즉 배열의 형태이기에 제이슨 배열로 받아온다.
            JSONArray array = (JSONArray) parseItems.get("item");

            for(int i=0; i<array.size(); i++) {
                JSONObject tmp = (JSONObject)array.get(i);
                OpenApiEntity openApiEntity = new OpenApiEntity(
                        i+(long)1,
                        (long)tmp.get("rnum"),
                        (String)tmp.get("code"),
                        (String)tmp.get("name"));
                openApiRepository.save(openApiEntity);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "index";

    }


}
