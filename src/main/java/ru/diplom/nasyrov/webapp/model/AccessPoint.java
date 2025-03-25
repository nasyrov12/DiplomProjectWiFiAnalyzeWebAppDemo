package ru.diplom.nasyrov.webapp.model;


import java.util.ArrayList;
import java.util.List;

//TODO Lombok

public class AccessPoint {
    private String name; // SSID (имя точки доступа)
    private List<BSSID> bssids; // Список BSSID, связанных с этой точкой доступа

    public AccessPoint() {
        this.bssids = new ArrayList<>();
    }

    public void addBSSID(BSSID bssid) {
        this.bssids.add(bssid);
    }

    // Геттеры и сеттеры
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BSSID> getBssids() {
        return bssids;
    }
}