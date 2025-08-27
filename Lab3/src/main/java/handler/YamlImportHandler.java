/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package handler;

import model.Monster;
import model.Monster.Recipe;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class YamlImportHandler implements ImportHandler {
    private ImportHandler nextHandler;

    @Override
    public void setNextHandler(ImportHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public List<Monster> handleImportFile(String filePath) throws FileTypeError {
        String lowerPath = filePath.toLowerCase();
        if (lowerPath.endsWith(".yml") || lowerPath.endsWith(".yaml")) {
            System.out.println("Обрабатывается YamlImportHandler: " + filePath);
            List<Monster> monsters = new ArrayList<>();
            Yaml yaml = new Yaml();
            
            try (FileInputStream inputStream = new FileInputStream(filePath)) {
                Map<String, Object> root = yaml.load(inputStream);
                
                // Правильная обработка структуры YAML
                Map<String, Object> bestiarumMap = (Map<String, Object>) root.get("bestiarum");
                if (bestiarumMap == null) {
                    System.err.println("Не найдена корневая структура 'bestiarum' в YAML файле");
                    return new ArrayList<>();
                }
                
                List<Map<String, Object>> monsterList = (List<Map<String, Object>>) bestiarumMap.get("monster");
                if (monsterList == null) {
                    System.err.println("Не найдена структура 'monster' в YAML файле");
                    return new ArrayList<>();
                }

                for (Map<String, Object> monsterMap : monsterList) {
                    Monster monster = new Monster();

                    // Обработка основных полей
                    monster.setId((Integer) monsterMap.get("id"));
                    monster.setName((String) monsterMap.get("name"));
                    monster.setDescription((String) monsterMap.get("description"));
                    monster.setFunction((String) monsterMap.get("function"));
                    
                    // Обработка danger (может быть Integer или String)
                    Object danger = monsterMap.get("danger");
                    if (danger instanceof Integer) {
                        monster.setDanger((Integer) danger);
                    } else if (danger instanceof String) {
                        try {
                            monster.setDanger(Integer.parseInt((String) danger));
                        } catch (NumberFormatException e) {
                            monster.setDanger(0);
                        }
                    }
                    
                    monster.setHabitat((String) monsterMap.get("habitat"));
                    monster.setFirstMention((String) monsterMap.get("first_mention"));
                    
                    // Обработка immunities (может быть список или строка)
                    Object immunitiesObj = monsterMap.get("immunity");
                    if (immunitiesObj instanceof List) {
                        monster.setImmunities((List<String>) immunitiesObj);
                    } else if (immunitiesObj instanceof String) {
                        List<String> immunitiesList = new ArrayList<>();
                        immunitiesList.add((String) immunitiesObj);
                        monster.setImmunities(immunitiesList);
                    }
                    
                    // Обработка height
                    Object heightObj = monsterMap.get("height");
                    if (heightObj instanceof Integer) {
                        monster.setHeight((Integer) heightObj);
                    } else if (heightObj instanceof String) {
                        try {
                            monster.setHeight(Integer.parseInt((String) heightObj));
                        } catch (NumberFormatException e) {
                            monster.setHeight(0);
                        }
                    }
                    
                    // Обработка weight
                    Object weightObj = monsterMap.get("weight");
                    if (weightObj != null) {
                        monster.setWeight(weightObj.toString());
                    }
                    
                    monster.setActivityTime((String) monsterMap.get("activity_time"));
                    monster.setSource(filePath);

                    // Обработка рецепта (используем правильный ключ "recipe")
                    if (monsterMap.containsKey("recipe")) {
                        Map<String, Object> recipeMap = (Map<String, Object>) monsterMap.get("recipe");
                        Recipe recipe = new Recipe();
                        
                        // Обработка ingredients
                        Object ingredientsObj = recipeMap.get("ingredients");
                        if (ingredientsObj instanceof List) {
                            recipe.setIngredients((List<String>) ingredientsObj);
                        } else if (ingredientsObj instanceof String) {
                            List<String> ingredientsList = new ArrayList<>();
                            ingredientsList.add((String) ingredientsObj);
                            recipe.setIngredients(ingredientsList);
                        }
                        
                        // Обработка preparation_time
                        Object prepTimeObj = recipeMap.get("preparation_time");
                        if (prepTimeObj instanceof Integer) {
                            recipe.setPreparationTime((Integer) prepTimeObj);
                        } else if (prepTimeObj instanceof String) {
                            try {
                                recipe.setPreparationTime(Integer.parseInt((String) prepTimeObj));
                            } catch (NumberFormatException e) {
                                recipe.setPreparationTime(0);
                            }
                        }
                        
                        recipe.setEffectiveness((String) recipeMap.get("effectiveness"));
                        monster.setRecipe(recipe);
                    }

                    monsters.add(monster);
                }

            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Ошибка при чтении YAML файла: " + e.getMessage());
                return new ArrayList<>();
            }
            
            System.out.println("Успешно загружено " + monsters.size() + " монстров из YAML");
            return monsters;
            
        } else if (nextHandler != null) {
            return nextHandler.handleImportFile(filePath);
        } else {
            throw new FileTypeError("Формат файла не поддерживается: " + filePath);
        }
    }
}
