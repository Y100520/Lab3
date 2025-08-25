package handler;

import java.util.List;
import model.Monster;

public abstract class AbstractImportHandler implements ImportHandler {
    protected ImportHandler nextHandler;

    @Override
    public void setNextHandler(ImportHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public List<Monster> handleImportFile(String filePath) throws FileTypeError {
        if (canHandle(filePath)) {
            return importData(filePath);
        } else if (nextHandler != null) {
            return nextHandler.handleImportFile(filePath);
        }
        throw new FileTypeError("Формат файла не поддерживается: " + filePath);
    }

    protected abstract boolean canHandle(String filePath);
    protected abstract List<Monster> importData(String filePath);
}