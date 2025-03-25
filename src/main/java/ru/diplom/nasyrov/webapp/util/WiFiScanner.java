package ru.diplom.nasyrov.webapp.util;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
@Component
public class WiFiScanner {
    public List<String> scanWifiNetworks() {
        List<String> networks = new ArrayList<>();
        try {
            Process process = Runtime.getRuntime().exec("cmd /c chcp 65001 & netsh wlan show networks mode=bssid"); // Для Windows
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("Считано: " + line); // Выводим каждую строку
                networks.add(line.trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return networks;
    }

}
