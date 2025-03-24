package com.kayla.HermesRAG.utils;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Component
public class PythonExecutor {
    private final ProjectRootFinder projectRootFinder;

    public PythonExecutor(ProjectRootFinder projectRootFinder) {
        this.projectRootFinder = projectRootFinder;
    }

    public String runPythonScript(String scriptPath, String... args) {
        try {
            // 가상 환경 활성화 + Python 스크립트
            String command = "cmd /c \"src\\python\\venv\\Scripts\\activate"
                    + " && python " + scriptPath;

            for (String arg : args) {
                command += " \"" + arg + "\"";
            }

            // ProcessBuilder로 명령 실행 준비
            ProcessBuilder processBuilder = new ProcessBuilder("cmd", "/c", command);
            processBuilder.directory(new java.io.File(projectRootFinder.getProjectRoot()));

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
                return result.toString();
            } else {
                throw new RuntimeException("Error executing Python script: " + scriptPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to execute Python script", e);
        }
    }
}
