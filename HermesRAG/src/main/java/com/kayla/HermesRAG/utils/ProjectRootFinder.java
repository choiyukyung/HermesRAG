package com.kayla.HermesRAG.utils;

import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class ProjectRootFinder {

    public String getProjectRoot() {
        //ide에서는 실행 중인 애플리케이션의 working dir
        File currentDir = new File(System.getProperty("user.dir"));

        File[] subdirectories = currentDir.listFiles(file -> file.isDirectory() && file.getName().equals("HermesRAG"));

        assert subdirectories != null;
        return subdirectories[0].getAbsolutePath();
    }
}
