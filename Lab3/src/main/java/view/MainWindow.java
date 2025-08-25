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
    private final ImportHandler importHandler;
    private final JToolBar toolBar;
    

    public MainWindow() {
        setTitle("База данных чудовищ");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        importHandler = new JsonImportHandler();
        ImportHandler xmlHandler = new XmlImportHandler();
        ImportHandler yamlHandler = new YamlImportHandler();
        importHandler.setNextHandler(xmlHandler);
        xmlHandler.setNextHandler(yamlHandler);
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
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        fileChooser.setMultiSelectionEnabled(true);
        
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File[] files = fileChooser.getSelectedFiles();
            List<Monster> importedMonsters = new ArrayList<>();
            List<String> errorMessages = new ArrayList<>();
            for (File file : files) {
                try{
                List<Monster> monsters = importHandler.handleImportFile(file.getPath());
                if (!monsters.isEmpty()) {
                    monsterCollections.put(file.getName(), monsters);
                    importedMonsters.addAll(monsters);
                }
                }catch (FileTypeError e){
                    errorMessages.add(file.getName()+ ": " + e.getMessage());
                }
            }
            updateTree();
            if (!errorMessages.isEmpty()) {
            String message = "Успешно импортировано: " + importedMonsters.size() + " монстров\n\n" +
                           "Ошибки:\n" + String.join("\n", errorMessages);
            
            JOptionPane.showMessageDialog(this, message, "Результат импорта", 
                importedMonsters.isEmpty() ? JOptionPane.ERROR_MESSAGE : JOptionPane.WARNING_MESSAGE);
        } else if (!importedMonsters.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Успешно импортировано " + importedMonsters.size() + " монстров",
                "Импорт завершен", JOptionPane.INFORMATION_MESSAGE);
        }
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
        JOptionPane.showMessageDialog(this, 
            "No monsters to export!", 
            "Export Error", 
            JOptionPane.ERROR_MESSAGE);
        return;
    }
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
    
    FileNameExtensionFilter jsonFilter = new FileNameExtensionFilter("JSON files (*.json)", "json");
    FileNameExtensionFilter xmlFilter = new FileNameExtensionFilter("XML files (*.xml)", "xml");
    FileNameExtensionFilter yamlFilter = new FileNameExtensionFilter("YAML files (*.yml, *.yaml)", "yml", "yaml");
    
    fileChooser.addChoosableFileFilter(jsonFilter);
    fileChooser.addChoosableFileFilter(xmlFilter);
    fileChooser.addChoosableFileFilter(yamlFilter);
    fileChooser.setFileFilter(jsonFilter); 
    fileChooser.setAcceptAllFileFilterUsed(false);
    
if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
        File file = fileChooser.getSelectedFile();
        String path = file.getPath();
        
        String extension = "";
        if (fileChooser.getFileFilter() instanceof FileNameExtensionFilter) {
            FileNameExtensionFilter filter = (FileNameExtensionFilter) fileChooser.getFileFilter();
            extension = filter.getExtensions()[0];
            
            if (!path.toLowerCase().endsWith("." + extension)) {
                path += "." + extension;
            }
        }
            
        ExportHandler jsonHandler = new JsonExportHandler();
        ExportHandler xmlHandler = new XmlExportHandler();
        ExportHandler yamlHandler = new YamlExportHandler();
        
        jsonHandler.setNextHandler(xmlHandler);
        xmlHandler.setNextHandler(yamlHandler);
        
        List<Monster> allMonsters = new ArrayList<>();
        for (List<Monster> monsters : monsterCollections.values()) {
            allMonsters.addAll(monsters);
        }
        
         try {
            boolean success = jsonHandler.handleExportFile(path, allMonsters);
            
            if (success) {
                JOptionPane.showMessageDialog(this, 
                    "Export successful!\nFile: " + path, 
                    "Export", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                throw new Exception("Export handler returned false");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Export failed!\nError: " + e.getMessage(), 
                "Export Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainWindow().setVisible(true);
        });
    }
}