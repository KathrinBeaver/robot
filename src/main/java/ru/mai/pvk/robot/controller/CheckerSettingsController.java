package ru.mai.pvk.robot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.mai.pvk.robot.error.exception.ProjectProccessException;
import ru.mai.pvk.robot.model.dto.IssueCheckerSettingsDto;
import ru.mai.pvk.robot.service.RobotSettingsService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/robotSettings")
public class CheckerSettingsController {

    private final RobotSettingsService robotSettingsService;
    @GetMapping()
    public IssueCheckerSettingsDto getSettings() {
        return robotSettingsService.getRobotSettings();
    }

    @PutMapping(consumes = "application/json", produces = "application/json")
    public String updateCheckerSettings (@RequestBody IssueCheckerSettingsDto checkerSettings) {
        try {
            robotSettingsService.setRobotSettings(checkerSettings);
        }
        catch (Exception e) {
            throw new ProjectProccessException();
        }

        return "200 - настройки проверки применены";
    }

}
