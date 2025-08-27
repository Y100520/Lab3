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
        super(owner, "Информация о чудовище", true);
        this.monster = monster;
        setupDialog();
    }

    private void setupDialog() {
        setSize(1000, 600);
        setLocationRelativeTo(getParent());

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 5));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        panel.add(new JLabel("ID:"));
        panel.add(new JLabel(String.valueOf(monster.getId())));

        panel.add(new JLabel("Имя:"));
        JTextField nameField = new JTextField(monster.getName());
        nameField.addActionListener(e -> monster.setName(nameField.getText()));
        panel.add(nameField);

        panel.add(new JLabel("Описание:"));
        JTextField descField = new JTextField(monster.getDescription());
        descField.addActionListener(e -> monster.setDescription(descField.getText()));
        panel.add(descField);

        panel.add(new JLabel("Вес:"));
        JTextField weightField = new JTextField(monster.getWeight());
        weightField.addActionListener(e -> monster.setWeight(weightField.getText()));
        panel.add(weightField);

        panel.add(new JLabel("Уровень опасности:"));
        JTextField dangerField = new JTextField(String.valueOf(monster.getDanger()));
        dangerField.addActionListener(e -> {
            try {
                monster.setDanger(Integer.parseInt(dangerField.getText()));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Введите число!");
                dangerField.setText(String.valueOf(monster.getDanger()));
            }
        });
        panel.add(dangerField);

        panel.add(new JLabel("Место обитания:"));
        panel.add(new JLabel(monster.getHabitat()));

        panel.add(new JLabel("Источник:"));
        panel.add(new JLabel(monster.getSource()));

        if (monster.getRecipe() != null) {
            panel.add(new JLabel("Рецепт:"));
            panel.add(new JLabel(""));
            panel.add(new JLabel("  Ингредиенты:"));
            panel.add(new JLabel(String.join(", ", monster.getRecipe().getIngredients())));
            panel.add(new JLabel("  Время приготовления:"));
            panel.add(new JLabel(monster.getRecipe().getPreparationTime() + " мин"));
            panel.add(new JLabel("  Эффективность:"));
            panel.add(new JLabel(monster.getRecipe().getEffectiveness()));
        }

        add(new JScrollPane(panel));
    }
}

