package ru.diplom.nasyrov.webapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.diplom.nasyrov.webapp.service.AccessPointService;

@Controller
@RequestMapping("/access_points")
public class AccessPointController {
    private final AccessPointService accessPointService;

    public AccessPointController(AccessPointService accessPointService) {
        this.accessPointService = accessPointService;
    }

    @GetMapping
    public String showAccessPoints(Model model) {
        model.addAttribute("groupedNetworks",
                accessPointService.getAccessPointsGroupedByFrequency());
        return "index";
    }

    @PostMapping("/scan")
    public String scanAccessPoints() {
        accessPointService.scanAndUpdateAccessPoints();
        return "redirect:/access_points";
    }
}