package ru.diplom.nasyrov.webapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.diplom.nasyrov.webapp.model.AccessPoint;
import ru.diplom.nasyrov.webapp.service.AccessPointService;

import java.util.List;

@Controller
@RequestMapping("/access_points")
public class AccessPointController {
private final AccessPointService accessPointService;
    @Autowired
    public AccessPointController(AccessPointService accessPointService) {
        this.accessPointService = accessPointService;
    }


    // Отображение списка точек доступа
    @GetMapping
    public String showAccessPoints(Model model) {
        accessPointService.scanAndUpdateAccessPoints();
        model.addAttribute("accessPoints", accessPointService.getAccessPoints());
        System.out.println("Отдаем в представление"+ accessPointService.getAccessPoints());
        return "index"; // Возвращаем шаблон index.html
    }

    // Запуск сканирования и обновления данных
    @PostMapping("/scan")
    public String scanAccessPoints() {
        // Сканируем сети и обновляем данные
        accessPointService.scanAndUpdateAccessPoints();

        // Выполняем редирект на страницу с обновленными данными
        return "redirect:/access_points";
    }

}



