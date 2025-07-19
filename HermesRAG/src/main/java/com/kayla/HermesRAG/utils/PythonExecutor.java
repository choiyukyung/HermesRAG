package com.kayla.HermesRAG.utils;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

// 2025-05-28
// 이 PythonExecutor 기반 실행 로직은 필요 없어짐
// 대신 FastAPI 엔드포인트를 통해 Python 스크립트를 실행함
@Component
public class PythonExecutor {
    private final ProjectRootFinder projectRootFinder;

    public PythonExecutor(ProjectRootFinder projectRootFinder) {
        this.projectRootFinder = projectRootFinder;
    }

    public String runPythonScript(String scriptPath, String... args) {
        try {

            //가상 환경 준비(없으면 설치)
            setupVenv();

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
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                StringBuilder errorOutput = new StringBuilder();
                String errorLine;
                while ((errorLine = errorReader.readLine()) != null) {
                    errorOutput.append(errorLine).append("\n");
                }
                throw new RuntimeException("Error executing Python script:\n" + errorOutput);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to execute Python script", e);
        }
    }

    public void setupVenv() throws Exception {
        String projectRoot = projectRootFinder.getProjectRoot();
        File venvDir = new File(projectRoot, "src/python/venv");

        // venv 생성
        if (!venvDir.exists()) {
            ProcessBuilder createVenv = new ProcessBuilder("cmd", "/c", "python -m venv src\\python\\venv");
            createVenv.directory(new File(projectRoot));
            Process createProcess = createVenv.start();
            int exitCode = createProcess.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("Failed to create virtual environment");
            }

            // requirements.txt 설치
            String activateAndInstall = "src\\python\\venv\\Scripts\\activate && pip install -r src\\python\\requirements.txt";
            ProcessBuilder installRequirements = new ProcessBuilder("cmd", "/c", activateAndInstall);
            installRequirements.directory(new File(projectRoot));
            Process installProcess = installRequirements.start();

            // 설치중 로그 확인
            BufferedReader installReader = new BufferedReader(new InputStreamReader(installProcess.getInputStream()));
            String installLine;
            while ((installLine = installReader.readLine()) != null) {
                System.out.println("INSTALL: " + installLine);
            }

            BufferedReader installError = new BufferedReader(new InputStreamReader(installProcess.getErrorStream()));
            String errorLine;
            while ((errorLine = installError.readLine()) != null) {
                System.err.println("INSTALL ERROR: " + errorLine);
            }

            int installExit = installProcess.waitFor();
            if (installExit != 0) {
                throw new RuntimeException("Failed to install requirements.txt");
            }
        }


    }

}
