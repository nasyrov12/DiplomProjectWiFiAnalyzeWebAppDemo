package ru.diplom.nasyrov.webapp.util;


import ru.diplom.nasyrov.webapp.model.AccessPoint;
import java.util.Comparator;

public class AccessPointComparator implements Comparator<AccessPoint> {
    @Override
    public int compare(AccessPoint ap1, AccessPoint ap2) {
        Integer channel1 = ap1.getBssids().stream()
                .map(b -> b.getChannel())
                .min(Integer::compare)
                .orElse(0);

        Integer channel2 = ap2.getBssids().stream()
                .map(b -> b.getChannel())
                .min(Integer::compare)
                .orElse(0);

        return channel1.compareTo(channel2);
    }
}