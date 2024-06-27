/*
 * To change this license header, choose License Headers in Project RedmineConnectionProperties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.mai.pvk.robot.redmine.lints;

import java.util.Random;

/**
 * @author Politsyn
 */
public class PhrasesGenerator {

    private String[] phraseStorage
            = {"Great work!", "Awesome code!",
            "Nice attempt.", "Code accepted.", "Static test was OK. Can u go further?", "Robot: First phase is True",
            "Your know Lint. You can talk to Robot.",
            "Hope you can\'t play chess better than StockFish bot. But you passed the Linter.",
            "My god, one of the best styles.", "Stylish code. Accepted",
            "Outstanding. Not many can pass linter so easily.", "Gj! See u later!",
            "How do you do, Mr. Know-all?", "It\'s really worth to respect."};

    public String getSuccessRandomPhrase() {
        int i = new Random().nextInt(100);
        return phraseStorage[i % phraseStorage.length];
    }
}
