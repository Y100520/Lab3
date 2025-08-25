/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import model.Monster;
import java.io.File;
import java.util.List;

public class JsonExportHandler implements ExportHandler {
    private ExportHandler nextHandler;
    private final ObjectMapper mapper;

    public JsonExportHandler() {
        this.mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT); 
    }

    @Override
    public void setNextHandler(ExportHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public boolean handleExportFile(String filePath, List<Monster> monsters) {
        if (filePath.toLowerCase().endsWith(".json")) {
            try {
                mapper.writeValue(new File(filePath), monsters);
                return true;
            } catch (Exception e) {
                throw new RuntimeException("Не удалось сохранить файл: " + e.getMessage());
            }
        } else if (nextHandler != null) {
            return nextHandler.handleExportFile(filePath, monsters);
        } else{
            throw new IllegalArgumentException(
                    "Неподдерживаемый формат файла.\n" +
                    "Поддерживается только .json\n\n" +
                    "Выбранный файл: " + filePath
                );
        }
    }
}