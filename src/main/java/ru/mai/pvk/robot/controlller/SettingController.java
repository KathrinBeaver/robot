package ru.mai.pvk.robot.controlller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mai.pvk.robot.model.dto.SettingDto;
import ru.mai.pvk.robot.service.SettingService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/settings/{id}")
public class SettingController {

    private final SettingService settingService;
    @GetMapping()
    public SettingDto getSettings(@PathVariable String id) {
        return settingService.getUserSettings(id);
    }

}
