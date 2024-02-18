package ru.mai.pvk.robot.controlller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mai.pvk.robot.model.dto.SettingDto;
import ru.mai.pvk.robot.service.SettingService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/setting")
public class SettingController {

    private final SettingService settingService;
    @GetMapping(value = "")
    public SettingDto getSetting () {
        return settingService.getUserSettings();
    }

}
