/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.border.EmptyBorder;
import model.Monster.Recipe;

public class MonsterInfoDialog extends JDialog {
    private final Monster monster;

    public MonsterInfoDialog(Frame owner, Monster monster) {
        super(owner, "Monster Info", true);
        this.monster = monster;
        setupDialog();
    }

    private void setupDialog() {
        setSize(1000, 600);
        setLocationRelativeTo(getParent());
        
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 5));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        addInfoField(panel, "ID:", String.valueOf(monster.getId()));
        addEditableField(panel, "Имя:", monster.getName(), false);
        addEditableField(panel, "Описание:", monster.getDescription(), false);
        addEditableField(panel, "Функция:", monster.getFunction(), false);
        addDangerField(panel, "Уровень опасности:", monster.getDanger(), 0, 10);
        addEditableField(panel, "Место обитания:", monster.getHabitat(), false);
        addDateField(panel,  "Первое упоминание:", monster.getFirstMention());
        addEditableField(panel, "Иммунитет:", String.join(", ", monster.getImmunities()), false);
        addHeightField(panel, "Рост:", monster.getHeight(), 0);
        addEditableField(panel, "Вес:", monster.getWeight(), true);
        addEditableField(panel, "Время активности:", monster.getActivityTime(), false);
        
        addRecipeInfo(panel, monster.getRecipe());

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        
        panel.add(new JLabel());
        panel.add(closeButton);
        JLabel hintLabel = new JLabel("Нажмите Enter для сохранения");
        panel.add(hintLabel, BorderLayout.NORTH);

        add(new JScrollPane(panel));
    }

    private void addEditableField(JPanel panel, String label, String value, boolean editable) {
        panel.add(new JLabel(label));
        JTextField field = new JTextField(value != null ? value : "", 20);
        field.setEditable(editable);
        if (editable) {
            field.addActionListener(e -> updateField(field, label));
        }
        panel.add(field);
    }

    private void addHeightField(JPanel panel, String label, int value, int min) {
        panel.add(new JLabel(label));
        JTextField field = new JTextField(String.valueOf(value), 5);
        field.addActionListener(e -> {
            try {
                int num = Integer.parseInt(field.getText());
                if (num >= min) {
                    updateField(field, label);
                } else {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, 
                    "Введите число от " + min,
                    "Invalid Input", 
                    JOptionPane.ERROR_MESSAGE);
                field.setText(String.valueOf(value));
            }
        });
        panel.add(field);
    }
    
    private void addDangerField(JPanel panel, String label, int value, int min, int max) {
        panel.add(new JLabel(label));
        JTextField field = new JTextField(String.valueOf(value), 5);
        field.addActionListener(e -> {
            try {
                int num = Integer.parseInt(field.getText());
                if (num >= min && num <= max) {
                    updateField(field, label);
                } else {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, 
                    "Введите число от " + min + "и до " + max,
                    "Invalid Input", 
                    JOptionPane.ERROR_MESSAGE);
                field.setText(String.valueOf(value));
            }
        });
        panel.add(field);
    }

    private void addInfoField(JPanel panel, String label, String value) {
        panel.add(new JLabel(label));
        panel.add(new JLabel(value != null ? value : "Unknown"));
    }

    private void addRecipeInfo(JPanel panel, Recipe recipe) {
        if (recipe == null) {
            addInfoField(panel, "Рецепт:", "No recipe available");
            return;
        }

        panel.add(new JLabel("Рецепт:"));
        panel.add(new JLabel(""));
        
        addInfoField(panel, "Ингредиенты:", String.join(", ", recipe.getIngredients()));
        addInfoField(panel, "Время приготовления:", recipe.getPreparationTime() + " мин");
        addInfoField(panel, "Эффективность:", recipe.getEffectiveness());
    }
    
private void addDateField(JPanel panel, String label, String dateValue) {
    panel.add(new JLabel(label));
    
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    format.setLenient(false);
    
    JFormattedTextField dateField = new JFormattedTextField(format);
    
    try {
        if (dateValue != null && !dateValue.isEmpty()) {
            Date date = format.parse(dateValue);
            dateField.setValue(date);
        }
    } catch (ParseException e) {
        dateField.setValue(null);
    }
    
    dateField.setColumns(10);
    
    dateField.addFocusListener(new FocusAdapter() {
    public void focusLost(FocusEvent e) {
        String text = dateField.getText();
        try {
            Date parsedDate = format.parse(text);
            String formatted = format.format(parsedDate);
            if (!text.equals(formatted)) {
                throw new ParseException("Invalid format", 0);
            }
            monster.setFirstMention(formatted);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(MonsterInfoDialog.this,
                "Неверный формат даты. Используйте ГГГГ-ММ-ДД",
                "Ошибка ввода",
                JOptionPane.ERROR_MESSAGE);
            dateField.setValue(null);
        }
    }
});

    
    panel.add(dateField);
}

    private void updateField(JTextField field, String fieldName) {
        String value = field.getText();
        switch (fieldName) {
            case "Имя:" -> monster.setName(value);
            case "Описание:" -> monster.setDescription(value);
            case "Функция:" -> monster.setFunction(value);
            case "Уровень опасности:" -> monster.setDanger(Integer.parseInt(value));
            case "Место обитания:" -> monster.setHabitat(value);
            case "Первое упоминание:" -> monster.setFirstMention(value);
            case "Иммунитет:" -> {String[] immunitiesArray = value.split(",");
            monster.getImmunities().clear();
            for (String immunity : immunitiesArray) {
                String trimmed = immunity.trim();
                if (!trimmed.isEmpty()) {
                    monster.getImmunities().add(trimmed);
                }
            }
            }
            case "Рост:" -> monster.setHeight(Integer.parseInt(value));
            case "Вес:" -> monster.setWeight(value);
            case "Время активности:" -> monster.setActivityTime(value);
        }
    }
}
 