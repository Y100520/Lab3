/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import handler.*;
import model.Monster;
import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MainWindow extends JFrame {

    private final Map<String, List<Monster>> monsterCollections = new HashMap<>();

    private final JTree tree;

    private final JToolBar toolBar;

    public MainWindow() {
        setTitle("База данных чудовищ");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        toolBar = new JToolBar();

        JButton exportButton = new JButton("Экспорт");
        exportButton.addActionListener(e -> exportMonsters());
        toolBar.add(exportButton);

        JButton importButton = new JButton("Импорт");
        importButton.addActionListener(e -> importFiles());
        toolBar.add(importButton);

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Чудовища");
        tree = new JTree(root);

        tree.addTreeSelectionListener(e -> showMonsterInfo());

        setLayout(new BorderLayout());
        add(toolBar, BorderLayout.NORTH);
        add(new JScrollPane(tree), BorderLayout.CENTER);
    }

    private void importFiles() {
        ImportHandler jsonHandler = new JsonImportHandler();
        ImportHandler xmlHandler = new XmlImportHandler();
        ImportHandler yamlHandler = new YamlImportHandler();

        jsonHandler.setNextHandler(xmlHandler);
        xmlHandler.setNextHandler(yamlHandler);

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        fileChooser.setMultiSelectionEnabled(true); // Разрешаем выбирать несколько файлов

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File[] files = fileChooser.getSelectedFiles();

            for (File file : files) {
                try {
                    List<Monster> monsters = jsonHandler.handleImportFile(file.getPath());
                    if (monsters != null && !monsters.isEmpty()) {
                        monsterCollections.put(file.getName(), monsters);
                        System.out.println("Загружено " + monsters.size() + " монстров из файла " + file.getName());
                    }
                } catch (FileTypeError e) {
                    JOptionPane.showMessageDialog(this, e.getMessage(), "Ошибка импорта", JOptionPane.ERROR_MESSAGE);
                }
            }
            updateTree();
        }
    }

    private void updateTree() {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();
        root.removeAllChildren();

        for (Map.Entry<String, List<Monster>> entry : monsterCollections.entrySet()) {
            DefaultMutableTreeNode fileNode = new DefaultMutableTreeNode(entry.getKey());
            for (Monster monster : entry.getValue()) {
                fileNode.add(new DefaultMutableTreeNode(monster));
            }
            root.add(fileNode);
        }

        ((DefaultTreeModel) tree.getModel()).reload();
    }

    private void showMonsterInfo() {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        if (node != null && node.getUserObject() instanceof Monster) {
            Monster monster = (Monster) node.getUserObject();
            new MonsterInfoDialog(this, monster).setVisible(true);
        }
    }

    private void exportMonsters() {
        if (monsterCollections.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Нет монстров для экспорта!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ExportHandler jsonHandler = new JsonExportHandler();
        ExportHandler xmlHandler = new XmlExportHandler();
        ExportHandler yamlHandler = new YamlExportHandler();

        jsonHandler.setNextHandler(xmlHandler);
        xmlHandler.setNextHandler(yamlHandler);

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));

        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("JSON files (*.json)", "json"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("XML files (*.xml)", "xml"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("YAML files (*.yml, *.yaml)", "yml", "yaml"));
        fileChooser.setAcceptAllFileFilterUsed(false); // Запрещаем опцию "Все файлы"

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            String path = fileChooser.getSelectedFile().getPath();

            List<Monster> allMonsters = new ArrayList<>();
            for (List<Monster> monsters : monsterCollections.values()) {
                allMonsters.addAll(monsters);
            }

            try {
                boolean success = jsonHandler.handleExportFile(path, allMonsters);

                if (success) {
                    JOptionPane.showMessageDialog(this, "Экспорт успешен!", "Экспорт", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Не удалось экспортировать. Неподдерживаемый формат файла.", "Ошибка экспорта", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Ошибка экспорта: " + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainWindow().setVisible(true);
        });
    }
}
