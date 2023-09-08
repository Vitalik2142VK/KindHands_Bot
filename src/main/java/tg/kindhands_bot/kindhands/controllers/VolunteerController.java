package tg.kindhands_bot.kindhands.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tg.kindhands_bot.kindhands.services.VolunteerService;

@RestController
@RequestMapping("volunteer")
public class VolunteerController {

    private final VolunteerService volunteerService;

    public VolunteerController(VolunteerService volunteerService) {
        this.volunteerService = volunteerService;
    }

    /**
     * Выводит не проверенные доклады пользователей о животных.
     * -----||-----
     * Displays unverified user reports about animals.
     */
    @GetMapping("/reports") // GET http://localhost:8080/volunteer/reports
    public ResponseEntity<?> getReports() {
        return ResponseEntity.ok().build();
    }



}
