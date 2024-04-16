package ru.mai.pvk.robot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.mai.pvk.robot.model.dto.SettingDto;
import ru.mai.pvk.robot.service.SettingService;

import java.util.Collections;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/settings")
public class SettingController {

    private final SettingService settingService;

    @GetMapping()
    public SettingDto getSettings() {
        return settingService.getUserSettings();
    }


    @PutMapping()
    public Map<String, String> saveSettings(@RequestBody SettingDto settingDto) {
        String response = settingService.saveSettings(settingDto);
        return Collections.singletonMap("200", response);
    }
}
