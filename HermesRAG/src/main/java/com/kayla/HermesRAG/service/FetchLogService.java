package com.kayla.HermesRAG.service;

import com.kayla.HermesRAG.entity.FetchLogEntity;
import com.kayla.HermesRAG.repository.FetchLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class FetchLogService {

    private final FetchLogRepository fetchLogRepository;

    public LocalDate getStartDate(LocalDate yesterday) { // 가져올 시작 날짜 착지
        FetchLogEntity lastLog = fetchLogRepository.findTopByOrderByLastFetchedDateDesc();
        LocalDate lastFetchedDate = (lastLog != null) ? lastLog.getLastFetchedDate() : LocalDate.MIN;
        // 한 달 전 vs 마지막 날짜 다음 날
        LocalDate oneMonthAgo = yesterday.minusMonths(1);
        return lastFetchedDate.isBefore(oneMonthAgo) ? oneMonthAgo : lastFetchedDate.plusDays(1);
    }

    public void saveFetchLog(LocalDate lastFetchedDate) {
        FetchLogEntity fetchLog = new FetchLogEntity();
        fetchLog.setLastFetchedDate(lastFetchedDate);
        fetchLogRepository.save(fetchLog);
    }
}
