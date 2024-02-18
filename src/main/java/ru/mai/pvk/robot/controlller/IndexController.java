package ru.mai.pvk.robot.controlller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mai.pvk.robot.property.RobotProperties;
import ru.mai.pvk.robot.service.SettingService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class IndexController {

    private final RobotProperties robotProperties;

	/**
	 * Проверка статуса сервиса
	 *
	 * @return "ОК"
	 */
	@GetMapping(value="status")
	public String getServiceStatus() {
		return "OK";
	}

    @GetMapping(value="version")
    public String getServiceVersion() {
        return robotProperties.getVersion();
    }
}
