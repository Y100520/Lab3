package handler;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import model.Bestiarum;
import model.Monster;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class JsonImportHandler extends AbstractImportHandler {
    private final ObjectMapper mapper;

    public JsonImportHandler() {
        this.mapper = JsonMapper.builder()
                 .enable(DeserializationFeature.UNWRAP_ROOT_VALUE)
                 .build();
    }

    @Override
    protected boolean canHandle(String filePath) {
        return filePath.toLowerCase().endsWith(".json");
    }

    @Override
    protected List<Monster> importData(String filePath) {
        try {
            Bestiarum bestiarum = mapper.readValue(new File(filePath), Bestiarum.class);
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