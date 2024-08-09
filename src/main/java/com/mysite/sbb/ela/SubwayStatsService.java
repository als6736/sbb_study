package com.mysite.sbb.ela;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class SubwayStatsService {

    private static final Logger logger = LoggerFactory.getLogger(SubwayStatsService.class);

    @Autowired
    private SubwayStatsRepository subwayStatsRepository;

    public List<SubwayStats> getAllStats(int page, int size) {
        Page<SubwayStats> statsPage = subwayStatsRepository.findAll(PageRequest.of(page, size));
        List<SubwayStats> statsList = statsPage.getContent();

        // 데이터가 제대로 조회되는지 로그로 확인
        logger.info("Subway Stats: {}", statsList);

        return statsList; // Page에서 List로 변환
    }
}
