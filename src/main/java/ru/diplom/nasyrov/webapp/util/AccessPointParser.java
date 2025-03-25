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

    // Шаблоны для разбора разных полей
    private static final Pattern SSID_PATTERN = Pattern.compile("^SSID \\d+ :\\s*(\\S.*)?$");
    private static final Pattern BSSID_PATTERN = Pattern.compile("^BSSID \\d+\\s*:\\s*([0-9A-Fa-f:]+)$");
    private static final Pattern SIGNAL_PATTERN = Pattern.compile("Signal\\s*:\\s*(\\d+)%");
    private static final Pattern RADIO_PATTERN = Pattern.compile("Radio type\\s*:\\s*([\\w.]+)");
    private static final Pattern CHANNEL_PATTERN = Pattern.compile("Channel\\s*:\\s*(\\d+)");

    /**
     * Основной метод парсинга сырых данных о WiFi сетях
     * @param rawData - сырые данные из команды netsh
     * @return список точек доступа с BSSID
     */
    public List<AccessPoint> parseNetworks(String rawData) {
        List<AccessPoint> accessPoints = new ArrayList<>();
        AccessPoint currentAP = null;
        BSSID currentBSSID = null;

        // Разбиваем данные на строки для обработки
        String[] lines = rawData.split("\\R");

        for (String line : lines) {
            line = line.trim();

            // Обработка SSID (имя сети)
            Matcher ssidMatcher = SSID_PATTERN.matcher(line);
            if (ssidMatcher.matches()) {
                // Сохраняем предыдущие данные перед обработкой нового SSID
                addLastBSSIDToAP(currentAP, currentBSSID);
                addAccessPointToList(accessPoints, currentAP);

                // Создаем новую точку доступа
                currentAP = new AccessPoint();
                currentBSSID = null;
                String ssid = ssidMatcher.group(1);
                currentAP.setName((ssid == null || ssid.isEmpty()) ? "<Без имени>" : ssid.trim());
                continue;
            }

            // Обработка BSSID (MAC-адрес точки доступа)
            Matcher bssidMatcher = BSSID_PATTERN.matcher(line);
            if (bssidMatcher.matches()) {
                // Сохраняем предыдущий BSSID
                addLastBSSIDToAP(currentAP, currentBSSID);

                // Создаем новый BSSID
                currentBSSID = new BSSID();
                currentBSSID.setMac(bssidMatcher.group(1));
                continue;
            }

            // Обработка остальных параметров (только если есть текущий BSSID)
            if (currentBSSID != null) {
                // Уровень сигнала
                Matcher signalMatcher = SIGNAL_PATTERN.matcher(line);
                if (signalMatcher.find()) {
                    currentBSSID.setSignal(Integer.parseInt(signalMatcher.group(1)));
                    continue;
                }

                // Тип радио (802.11ac/n и т.д.)
                Matcher radioMatcher = RADIO_PATTERN.matcher(line);
                if (radioMatcher.find()) {
                    currentBSSID.setRadioType(radioMatcher.group(1));
                    continue;
                }

                // Номер канала (и определение частоты)
                Matcher channelMatcher = CHANNEL_PATTERN.matcher(line);
                if (channelMatcher.find()) {
                    int channel = Integer.parseInt(channelMatcher.group(1));
                    currentBSSID.setChannel(channel);
                    // Определяем частоту по номеру канала
                    currentBSSID.setFrequency(getFrequencyFromChannel(channel));
                    continue;
                }
            }
        }

        // Добавляем последние обработанные данные
        addLastBSSIDToAP(currentAP, currentBSSID);
        addAccessPointToList(accessPoints, currentAP);

        return accessPoints;
    }

    /**
     * Определяет частоту по номеру канала
     * @param channel - номер канала
     * @return строка с частотой (2.4 GHz, 5 GHz и т.д.)
     */
    private String getFrequencyFromChannel(int channel) {
        if (channel >= 1 && channel <= 14) {
            return "2.4 GHz";  // Классический диапазон 2.4 GHz
        } else if (channel >= 36 && channel <= 165) {
            return "5 GHz";     // Диапазон 5 GHz
        } else if (channel >= 169 && channel <= 196) {
            return "6 GHz";     // Новый диапазон WiFi 6E
        }
        return "Unknown";       // Для нестандартных каналов
    }

    /**
     * Добавляет BSSID к текущей точке доступа
     */
    private void addLastBSSIDToAP(AccessPoint ap, BSSID bssid) {
        if (ap != null && bssid != null) {
            ap.addBSSID(bssid);
        }
    }

    /**
     * Добавляет точку доступа в список (если она содержит BSSID)
     */
    private void addAccessPointToList(List<AccessPoint> list, AccessPoint ap) {
        if (ap != null && !ap.getBssids().isEmpty()) {
            list.add(ap);
        }
    }
}