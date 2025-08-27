/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.*;

@XmlRootElement(name = "monster")
@XmlAccessorType(XmlAccessType.FIELD)
public class Monster {
    private int id;
    private String name;
    private String description;
    private String function;
    private int danger;
    private String habitat;

    private String source;

    @XmlElement(name = "first_mention")
    @JsonProperty("first_mention")
    private String firstMention;

    @XmlElement(name = "immunity")
    @JsonProperty("immunity")
    private List<String> immunities;

    private int height;
    private String weight;

    @XmlElement(name = "activity_time")
    @JsonProperty("activity_time")
    private String activityTime;

    @XmlElement(name = "recept")
    @JsonProperty("recept")
    private Recipe recipe;

    public Monster() {
        this.immunities = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public int getDanger() {
        return danger;
    }

    public void setDanger(int danger) {
        this.danger = danger;
    }

    public String getHabitat() {
        return habitat;
    }

    public void setHabitat(String habitat) {
        this.habitat = habitat;
    }

    public String getFirstMention() {
        return firstMention;
    }

    public void setFirstMention(String date) {
        this.firstMention = date;
    }

    public List<String> getImmunities() {
        return immunities;
    }

    public void setImmunities(List<String> immunities) {
        this.immunities = immunities;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getActivityTime() {
        return activityTime;
    }

    public void setActivityTime(String activityTime) {
        this.activityTime = activityTime;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Recipe {
        @XmlElement(name = "ingredient")
        @JsonProperty("ingredient")
        private List<String> ingredients = new ArrayList<>();

        @XmlElement(name = "preparation_time")
        @JsonProperty("preparation_time")
        private int preparationTime;

        private String effectiveness;

        public List<String> getIngredients() {
            return ingredients;
        }

        public void setIngredients(List<String> ingredients) {
            this.ingredients = ingredients;
        }

        public int getPreparationTime() {
            return preparationTime;
        }

        public void setPreparationTime(int preparationTime) {
            this.preparationTime = preparationTime;
        }

        public String getEffectiveness() {
            return effectiveness;
        }

        public void setEffectiveness(String effectiveness) {
            this.effectiveness = effectiveness;
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
