/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package handler;

import java.util.List;

import model.Monster;

public interface ImportHandler {
    void setNextHandler(ImportHandler nextHandler);

    List<Monster> handleImportFile(String filePath) throws FileTypeError;
}
