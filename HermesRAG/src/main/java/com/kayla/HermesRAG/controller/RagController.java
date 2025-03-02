package com.kayla.HermesRAG.controller;

import com.kayla.HermesRAG.dto.RagResponseDTO;
import com.kayla.HermesRAG.service.RagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rag")
@RequiredArgsConstructor
public class RagController {
    private final RagService ragService;

    @PostMapping("/summarize")
    public ResponseEntity<RagResponseDTO> ragSummarize(@RequestParam(value = "query") String query) {
        RagResponseDTO response = ragService.summarize(query);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/answer")
    public ResponseEntity<RagResponseDTO> ragAnswer(@RequestParam(value = "query") String query) {
        RagResponseDTO response = ragService.answer(query);
        return ResponseEntity.ok(response);
    }

}
