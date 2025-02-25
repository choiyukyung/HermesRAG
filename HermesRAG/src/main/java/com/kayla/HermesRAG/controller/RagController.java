package com.kayla.HermesRAG.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kayla.HermesRAG.dto.RagResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@RestController
@RequestMapping("/api/rag")
@RequiredArgsConstructor
public class RagController {

    @PostMapping("/summarize")
    public ResponseEntity<RagResponseDTO> ragSummarize(@RequestParam(value = "query") String query) {
        try {
            // 가상 환경을 활성화 + Python 스크립트를 실행
            String command = "cmd /c \"src\\python\\venv\\Scripts\\activate"
                    + " && python src\\python\\rag_summarize.py \"" + query + "\"";

            // ProcessBuilder로 명령 실행 준비
            ProcessBuilder processBuilder = new ProcessBuilder("cmd", "/c", command);
            processBuilder.directory(new java.io.File("C:\\Users\\ykcho\\Desktop\\HermesRAG\\HermesRAG"));

            // Python 스크립트 실행
            Process process = processBuilder.start();

            // 결과 읽기
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            StringBuilder result = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                // JSON 형식으로 변환하여 반환
                String jsonOutput = result.toString();
                ObjectMapper objectMapper = new ObjectMapper();

                // JSON 출력 확인
                System.out.println("JSON Output: " + jsonOutput);

                RagResponseDTO ragResponseDTO = objectMapper.readValue(jsonOutput, RagResponseDTO.class);
                return ResponseEntity.ok(ragResponseDTO);
            } else {
                return ResponseEntity.status(500).body(new RagResponseDTO("error", null, "Error executing Python script"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new RagResponseDTO("error", null, "Internal Server Error"));
        }
    }

}
