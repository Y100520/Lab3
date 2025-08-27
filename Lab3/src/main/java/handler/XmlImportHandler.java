/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package handler;

import model.Bestiarum;
import model.Monster;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class XmlImportHandler implements ImportHandler {
    private ImportHandler nextHandler;

    @Override
    public void setNextHandler(ImportHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public List<Monster> handleImportFile(String filePath) throws FileTypeError {
        if (filePath.toLowerCase().endsWith(".xml")) {
            System.out.println("Обрабатывается XmlImportHandler: " + filePath);
            try {
                JAXBContext context = JAXBContext.newInstance(Bestiarum.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                Bestiarum bestiarum = (Bestiarum) unmarshaller.unmarshal(new File(filePath));
                List<Monster> monsters = bestiarum.getMonsters();
                for (Monster m : monsters) {
                    m.setSource(filePath);
                }
                return monsters;
            } catch (Exception e) {
                e.printStackTrace();
                return new ArrayList<>();
            }
        } else if (nextHandler != null) {
            return nextHandler.handleImportFile(filePath);
        } else {
            throw new FileTypeError("Формат файла не поддерживается: " + filePath);
        }
    }
}
