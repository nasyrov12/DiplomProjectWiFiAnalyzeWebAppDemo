package ru.diplom.nasyrov.webapp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.diplom.nasyrov.webapp.model.AccessPoint;
import ru.diplom.nasyrov.webapp.util.AccessPointComparator;
import ru.diplom.nasyrov.webapp.util.AccessPointParser;
import ru.diplom.nasyrov.webapp.util.WiFiScanner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class AccessPointService {
    private static final Logger logger = LoggerFactory.getLogger(AccessPointService.class);
    private final WiFiScanner wifiScanner;
    private final AccessPointParser parser;
    private List<AccessPoint> accessPoints = new ArrayList<>(); // Хранение данных в памяти

    public AccessPointService(WiFiScanner wifiScanner, AccessPointParser parser) {
        this.wifiScanner = wifiScanner;
        this.parser = parser;
    }
    public Map<String, List<AccessPoint>> getAccessPointsGroupedByFrequency() {
        Map<String, List<AccessPoint>> grouped = accessPoints.stream()
                .collect(Collectors.groupingBy(this::getDominantFrequency));

        grouped.forEach((frequency, list) ->
                list.sort(new AccessPointComparator()));

        return grouped;
    }

    private String getDominantFrequency(AccessPoint ap) {
        return ap.getBssids().stream()
                .filter(b -> b.getFrequency() != null)
                .findFirst()
                .map(b -> b.getFrequency())
                .orElse("Unknown");
    }

    // Метод для получения всех точек доступа
    public List<AccessPoint> getAccessPoints() {
        return new ArrayList<>(accessPoints); // Возвращаем копию списка для безопасности
    }

    // Метод для сканирования и обновления данных

    public void scanAndUpdateAccessPoints() {
        System.out.println("Запуск сканирования...");

        // Получаем список строк из сканера
        List<String> rawData = wifiScanner.scanWifiNetworks();

        // Объединяем их в одну строку для передачи в парсер
        String joinedData = String.join("\n", rawData);

        // Парсим данные и обновляем список точек доступа
        accessPoints = parser.parseNetworks(joinedData);

        System.out.println("Обновлённые точки доступа: " + accessPoints);



}}
