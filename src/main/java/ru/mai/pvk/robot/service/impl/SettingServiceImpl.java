package ru.mai.pvk.robot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mai.pvk.robot.model.dto.SettingDto;
import ru.mai.pvk.robot.service.SettingService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SettingServiceImpl implements SettingService {

    @Override
    public SettingDto getUserSettings() {

        String name = "Kathrin";
        String login = "KathrinBeaver";
        String apiKey = "4bb0d185-5de7-4dc3-b058-a426c9dea495";

        List<String> projectsList = new ArrayList();
        projectsList.add("ТП-2022");
        projectsList.add("ТП-2022");
        projectsList.add("ОПППС");

        SettingDto settingDto = SettingDto.of(
            name,
            login,
            apiKey,
            projectsList
        );
        return settingDto;
    }

}
