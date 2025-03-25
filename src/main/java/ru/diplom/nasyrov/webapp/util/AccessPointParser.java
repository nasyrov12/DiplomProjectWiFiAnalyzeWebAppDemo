package ru.diplom.nasyrov.webapp.util;

import org.springframework.stereotype.Component;
import ru.diplom.nasyrov.webapp.model.AccessPoint;
import ru.diplom.nasyrov.webapp.model.BSSID;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Component
public class AccessPointParser {

    private static final Pattern SSID_PATTERN = Pattern.compile("^SSID \\d+ :\\s*(\\S.*)?$");
    private static final Pattern BSSID_PATTERN = Pattern.compile("^BSSID \\d+\\s*:\\s*([0-9A-Fa-f:-]+)$");
    private static final Pattern SIGNAL_PATTERN = Pattern.compile("Signal\\s*:\\s*(\\d+)%");
    private static final Pattern RADIO_PATTERN = Pattern.compile("Radio type\\s*:\\s*([\\w.]+)");
    private static final Pattern CHANNEL_PATTERN = Pattern.compile("Channel\\s*:\\s*(\\d+)");
    private static final Pattern FREQUENCY_PATTERN = Pattern.compile("Frequency\\s*:\\s*([\\d.]+ GHz)"); // Добавлено для частоты

    public List<AccessPoint> parseNetworks(String rawData) {
        List<AccessPoint> accessPoints = new ArrayList<>();
        AccessPoint currentAP = null;
        BSSID currentBSSID = null;

        System.out.println("Исходные данные от netsh:\n" + rawData);

        String[] lines = rawData.split("\\R");
        for (String line : lines) {
            line = line.trim();
            System.out.println("Обрабатываем строку: " + line);

            Matcher ssidMatcher = SSID_PATTERN.matcher(line);
            if (ssidMatcher.matches()) {
                addLastBSSIDToAP(currentAP, currentBSSID);
                addAccessPointToList(accessPoints, currentAP);

                currentAP = new AccessPoint();
                currentBSSID = null;
                String ssid = ssidMatcher.group(1);
                currentAP.setName((ssid == null || ssid.isEmpty()) ? "<Без имени>" : ssid.trim());
                System.out.println("Найден SSID: " + currentAP.getName());
                continue;
            }

            Matcher bssidMatcher = BSSID_PATTERN.matcher(line);
            if (bssidMatcher.matches()) {
                addLastBSSIDToAP(currentAP, currentBSSID);

                if (currentAP == null) {
                    currentAP = new AccessPoint(); // Создаем, чтобы не потерять данные
                    currentAP.setName("<Без имени>");
                    System.err.println("Предупреждение: BSSID найден без SSID, создаём пустой SSID.");
                }
                currentBSSID = new BSSID(bssidMatcher.group(1), 0, "", 0, ""); // Частота пока неизвестна
                System.out.println("Найден BSSID: " + currentBSSID.getMac());
                continue;
            }

            Matcher signalMatcher = SIGNAL_PATTERN.matcher(line);
            if (signalMatcher.find() && currentBSSID != null) {
                try {
                    currentBSSID.setSignal(Integer.parseInt(signalMatcher.group(1)));
                    System.out.println("Найден уровень сигнала: " + currentBSSID.getSignal() + "%");
                } catch (NumberFormatException e) {
                    System.err.println("Ошибка разбора уровня сигнала: " + signalMatcher.group(1));
                }
                continue;
            }

            Matcher radioMatcher = RADIO_PATTERN.matcher(line);
            if (radioMatcher.find() && currentBSSID != null) {
                currentBSSID.setRadioType(radioMatcher.group(1));
                System.out.println("Найден тип радио: " + currentBSSID.getRadioType());
                continue;
            }

            Matcher channelMatcher = CHANNEL_PATTERN.matcher(line);
            if (channelMatcher.find() && currentBSSID != null) {
                try {
                    currentBSSID.setChannel(Integer.parseInt(channelMatcher.group(1)));
                    System.out.println("Найден канал: " + currentBSSID.getChannel());
                } catch (NumberFormatException e) {
                    System.err.println("Ошибка разбора номера канала: " + channelMatcher.group(1));
                }
                continue;
            }

            Matcher frequencyMatcher = FREQUENCY_PATTERN.matcher(line);
            if (frequencyMatcher.find() && currentBSSID != null) {
                currentBSSID.setFrequency(frequencyMatcher.group(1));
                System.out.println("Найдена частота: " + currentBSSID.getFrequency());
                continue;
            }
        }

        addLastBSSIDToAP(currentAP, currentBSSID);
        addAccessPointToList(accessPoints, currentAP);

        printParsingResult(accessPoints);
        return accessPoints;
    }

    // Вспомогательный метод для добавления BSSID к текущей точке доступа
    private void addLastBSSIDToAP(AccessPoint ap, BSSID bssid) {
        if (ap != null && bssid != null) {
            ap.addBSSID(bssid);
        }
    }

    // Вспомогательный метод для добавления AP в список
    private void addAccessPointToList(List<AccessPoint> list, AccessPoint ap) {
        if (ap != null && !ap.getBssids().isEmpty()) { // Добавляем только если есть BSSID
            list.add(ap);
        }
    }

    // Метод для вывода результата парсинга
    private void printParsingResult(List<AccessPoint> accessPoints) {
        System.out.println("\nРезультат парсинга:");
        for (AccessPoint ap : accessPoints) {
            System.out.println("SSID: " + ap.getName());
            for (BSSID b : ap.getBssids()) {
                System.out.println("  -> BSSID: " + b.getMac() +
                        ", Signal: " + b.getSignal() + "%" +
                        ", Channel: " + b.getChannel() +
                        ", Frequency: " + b.getFrequency());
            }
            System.out.println("-----------------------------");
        }
    }
}