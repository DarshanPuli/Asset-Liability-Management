package org.example.repository;

import org.example.model.Scenario;
import java.util.List;

public interface ScenarioDAO {
    void saveScenario(Scenario scenario);
    List<Scenario> getAllScenarios();
    void deleteScenario(String name);
    void updateScenario(Scenario scenario);
}

