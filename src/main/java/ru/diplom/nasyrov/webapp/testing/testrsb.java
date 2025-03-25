package ru.diplom.nasyrov.webapp.testing;

import ru.diplom.nasyrov.webapp.model.AccessPoint;
import ru.diplom.nasyrov.webapp.model.BSSID;
import ru.diplom.nasyrov.webapp.util.AccessPointParser;

import java.util.List;

public class testrsb {
    public static void main(String[] args) {
                String testData = """
            SSID 1 : Ufanet_127
                Network type            : Infrastructure
                Authentication          : WPA2-Personal
                Encryption              : CCMP
                BSSID 1                 : 48:22:54:b6:66:04
                     Signal             : 78%
                     Radio type         : 802.11ac
                     Channel            : 11
                     Basic rates (Mbps) : 1 2 5.5 11
                     Other rates (Mbps) : 6 9 12 18 24 36 48 54

            SSID 2 : DIRECT-S5-BRAVIA
                Network type            : Infrastructure
                Authentication          : WPA2-Personal
                Encryption              : CCMP
                BSSID 1                 : c6:8e:8f:0e:98:27
                     Signal             : 29%
                     Radio type         : 802.11n
                     Channel            : 10
                     Basic rates (Mbps) : 6 12 24
                     Other rates (Mbps) : 6 9 12 18 24 36 48 48 54

            SSID 3 :
                Network type            : Infrastructure
                Authentication          : WPA2-Personal
                Encryption              : CCMP
                BSSID 1                 : 82:d8:1b:f5:18:91
                     Signal             : 53%
                     Radio type         : 802.11ax
                     Channel            : 2
                     Basic rates (Mbps) : 1 2 5.5 11
                     Other rates (Mbps) : 6 9 12 18 24 36 48 54
                BSSID 2                 : e6:18:6b:53:70:aa
                     Signal             : 46%
                     Radio type         : 802.11ac
                     Channel            : 52
                     Basic rates (Mbps) : 6 12 24
                     Other rates (Mbps) : 9 18 36 48 54
                BSSID 3                 : 4a:22:54:a6:66:04
                     Signal             : 83%
                     Radio type         : 802.11ac
                     Channel            : 11
                     Basic rates (Mbps) : 1 2 5.5 11
                     Other rates (Mbps) : 6 9 12 18 24 36 48 54

            SSID 4 : Kingdom of Narnia
                Network type            : Infrastructure
                Authentication          : WPA2-Personal
                Encryption              : CCMP
                BSSID 1                 : 82:d8:1b:f5:18:90
                     Signal             : 53%
                     Radio type         : 802.11ax
                     Channel            : 2
                     Basic rates (Mbps) : 1 2 5.5 11
                     Other rates (Mbps) : 6 9 12 18 24 36 48 54

            SSID 5 : Ufanet_127_5G
                Network type            : Infrastructure
                Authentication          : WPA2-Personal
                Encryption              : CCMP
                BSSID 1                 : 48:22:54:b6:66:06
                     Signal             : 31%
                     Radio type         : 802.11ac
                     Channel            : 44
                     Basic rates (Mbps) : 6 12 24
                     Other rates (Mbps) : 9 18 36 48 54

            SSID 6 : Keenetic-130
                Network type            : Infrastructure
                Authentication          : WPA2-Personal
                Encryption              : CCMP
                BSSID 1                 : e4:18:6b:55:70:aa
                     Signal             : 53%
                     Radio type         : 802.11ac
                     Channel            : 52
                     Basic rates (Mbps) : 6 12 24
                     Other rates (Mbps) : 9 18 36 48 54
            """;

                AccessPointParser parser = new AccessPointParser();
                List<AccessPoint> accessPoints = parser.parseNetworks(testData);

                System.out.println("Результат парсинга:");
                if (accessPoints.isEmpty()) {
                    System.out.println("❌ Ошибка: парсер вернул пустой список!");
                } else {
                    for (AccessPoint ap : accessPoints) {
                        System.out.println("SSID: " + ap.getName());
                        for (BSSID bssid : ap.getBssids()) {
                            System.out.println("  -> BSSID: " + bssid.getMac());
                            System.out.println("     Signal: " + bssid.getSignal() + "%");
                            System.out.println("     Channel: " + bssid.getChannel());
                            System.out.println("     Radio Type: " + bssid.getRadioType());
                            System.out.println();
                        }
                        System.out.println("-----------------------------");
                    }
                    System.out.println("✅ Парсер работает корректно!");
                }
            }
        }