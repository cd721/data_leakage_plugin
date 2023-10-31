package com.github.cd721.data_leakage_plugin;

import com.github.cd721.data_leakage_plugin.listeners.FileChangeDetector;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class OverlapLeakageDetector {

    public FileChangeDetector fileChangeDetector = new FileChangeDetector();

    public boolean OverlapLeakageDetected(){
        int count = 0;
        try {
            String currentFile = "";
            File FinalOverlapLeak = new File(currentFile);
            Scanner scanner = new Scanner(FinalOverlapLeak);
            while (scanner.hasNextLine()){
                count ++;
            }
        } catch (FileNotFoundException e){
            //TODO
        }
        return count > 0;
    }



}
