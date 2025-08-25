package handler;

import model.Bestiarum;
import model.Monster;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class XmlImportHandler extends AbstractImportHandler {
    @Override
    protected boolean canHandle(String filePath) {
        return filePath.toLowerCase().endsWith(".xml");
    }

    @Override
    protected List<Monster> importData(String filePath) {
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
    }
}