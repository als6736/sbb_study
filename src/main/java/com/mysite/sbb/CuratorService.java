package com.mysite.sbb;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
public class CuratorService {

    // 필요한 경우, @Scheduled 주석을 제거하여 주기적으로 실행되지 않도록 할 수 있습니다.
    // 예를 들어, 일회성 작업으로 실행하려면 주석 처리된 상태로 유지하세요.
     @Scheduled(cron = "0 0 0 * * ?") // 매 자정 마다 삭제
    public void runCurator() {
        try {
            // Curator 실행 파일의 절대 경로를 사용
            String curatorPath = "C:\\Users\\ttmso\\AppData\\Local\\Programs\\Python\\Python37-32\\Scripts\\curator.exe";
            ProcessBuilder processBuilder = new ProcessBuilder(curatorPath, "--config", "C:\\Users\\ttmso\\Desktop\\skill\\curator\\curator.yml", "C:\\Users\\ttmso\\Desktop\\skill\\curator\\delete_indices.yml");
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
