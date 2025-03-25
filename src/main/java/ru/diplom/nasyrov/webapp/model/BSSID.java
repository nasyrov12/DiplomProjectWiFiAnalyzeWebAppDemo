package ru.diplom.nasyrov.webapp.model;

public class BSSID {
    private String mac; // MAC-адрес радиоинтерфейса
    private int signal; // Уровень сигнала (в % или dBm)
    private String radioType; // Тип радио (например, 802.11ac)
    private int channel; // Канал
    private String frequency; // Частота (например, 2.4 GHz или 5 GHz)

    // Конструктор, геттеры и сеттеры
    public BSSID(String mac, int signal, String radioType, int channel, String frequency) {
        this.mac = mac;
        this.signal = signal;
        this.radioType = radioType;
        this.channel = channel;
        this.frequency = frequency;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public int getSignal() {
        return signal;
    }

    public void setSignal(int signal) {
        this.signal = signal;
    }

    public String getRadioType() {
        return radioType;
    }

    public void setRadioType(String radioType) {
        this.radioType = radioType;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public BSSID() {
    }
}