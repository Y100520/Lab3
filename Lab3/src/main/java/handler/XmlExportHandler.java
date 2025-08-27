/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package handler;

import model.Monster;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.util.List;

import model.Bestiarum;

public class XmlExportHandler implements ExportHandler {
    private ExportHandler nextHandler;

    @Override
    public void setNextHandler(ExportHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public boolean handleExportFile(String filePath, List<Monster> monsters) {
        if (filePath.toLowerCase().endsWith(".xml")) {
            try {
                Bestiarum bestiarum = new Bestiarum();
                bestiarum.setMonsters(monsters);

                JAXBContext context = JAXBContext.newInstance(Bestiarum.class);
                Marshaller marshaller = context.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                marshaller.marshal(bestiarum, new File(filePath));
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else if (nextHandler != null) {
            return nextHandler.handleExportFile(filePath, monsters);
        }
        return false;
    }
}
