package com.kayla.HermesRAG.controller;

import com.kayla.HermesRAG.dto.SearchRequestDTO;
import com.kayla.HermesRAG.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;
    @GetMapping("/get")
    public List<SearchRequestDTO> getVectors() {
        return searchService.getMonthSearchRequestDTO();
    }

}
