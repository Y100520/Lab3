/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package handler;

import java.util.List;
import model.Monster;

public interface ExportHandler {
    void setNextHandler(ExportHandler nextHandler);
    boolean handleExportFile(String filePath, List<Monster> monsters);
}
