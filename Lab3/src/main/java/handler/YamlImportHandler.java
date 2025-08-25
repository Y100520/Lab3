package handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.Bestiarum;
import model.Monster;
import org.yaml.snakeyaml.Yaml;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class YamlImportHandler extends AbstractImportHandler {
    @Override
    protected boolean canHandle(String filePath) {
        String lower = filePath.toLowerCase();
        return lower.endsWith(".yml") || lower.endsWith(".yaml");
    }

    @Override
    protected List<Monster> importData(String filePath) {
        try (FileInputStream inputStream = new FileInputStream(filePath)) {
            Yaml yaml = new Yaml();
            Map<String, Object> root = yaml.load(inputStream);
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(root.get("bestiarum"));
            Bestiarum bestiarum = mapper.readValue(json, Bestiarum.class);
            
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