package com.kayla.HermesRAG.controller;

import com.kayla.HermesRAG.dto.FaissLoadDTO;
import com.kayla.HermesRAG.dto.SearchRequestDTO;
import com.kayla.HermesRAG.service.FaissService;
import com.kayla.HermesRAG.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/faiss")
@RequiredArgsConstructor
public class FaissController {

    private final FaissService faissService;
    @GetMapping("/get")
    public List<FaissLoadDTO> getEmbeddings() {
        return faissService.getMonthFaissLoadDTO();
    }
}
