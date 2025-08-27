/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package handler;

import model.Monster;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class YamlExportHandler implements ExportHandler {
    private ExportHandler nextHandler;
    private final Yaml yaml;

    public YamlExportHandler() {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);
        options.setIndent(2);
        this.yaml = new Yaml(options);
    }

    @Override
    public void setNextHandler(ExportHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public boolean handleExportFile(String filePath, List<Monster> monsters) {
        if (filePath.toLowerCase().endsWith(".yml") || filePath.toLowerCase().endsWith(".yaml")) {
            try (FileWriter writer = new FileWriter(filePath)) {

                Map<String, Object> bestiarumMap = new LinkedHashMap<>();
                List<Map<String, Object>> monsterList = new ArrayList<>();

                for (Monster m : monsters) {
                    Map<String, Object> monsterMap = new LinkedHashMap<>();
                    monsterMap.put("id", m.getId());
                    monsterMap.put("name", m.getName());
                    monsterMap.put("description", m.getDescription());
                    monsterMap.put("function", m.getFunction());
                    monsterMap.put("danger", m.getDanger());
                    monsterMap.put("habitat", m.getHabitat());
                    monsterMap.put("first_mention", m.getFirstMention());
                    
                    // Используем правильный ключ для иммунитетов
                    monsterMap.put("immunity", m.getImmunities());
                    
                    monsterMap.put("height", m.getHeight());
                    monsterMap.put("weight", m.getWeight());
                    monsterMap.put("activity_time", m.getActivityTime());
                    monsterMap.put("source", m.getSource());
                    
                    if (m.getRecipe() != null) {
                        Map<String, Object> recipeMap = new LinkedHashMap<>();
                        recipeMap.put("ingredients", m.getRecipe().getIngredients());
                        recipeMap.put("preparation_time", m.getRecipe().getPreparationTime());
                        recipeMap.put("effectiveness", m.getRecipe().getEffectiveness());
                        monsterMap.put("recipe", recipeMap);
                    }
                    monsterList.add(monsterMap);
                }
                
                bestiarumMap.put("monster", monsterList);

                Map<String, Object> root = new LinkedHashMap<>();
                root.put("bestiarum", bestiarumMap);

                yaml.dump(root, writer);
                System.out.println("Успешно экспортировано в YAML: " + monsters.size() + " монстров");
                return true;
                
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Ошибка при экспорте в YAML: " + e.getMessage());
                return false;
            }
        } else if (nextHandler != null) {
            return nextHandler.handleExportFile(filePath, monsters);
        }
        return false;
    }
}

