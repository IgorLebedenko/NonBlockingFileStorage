package org.example;

import java.io.InputStream;

/**
 * TODO - необходимо изменить API для ассинхронного доступа через каналы
 * при сохранении не должен приниматься путь, так же и не должен возвращаться
 * взаимодействие с внешними источниками должно происходить через выдаваемый id
 * механизм сохранения и выбираемые пути должны быть скрыты для пользовательских классов
 */
public interface FileStorage {
    InputStream getContent(String path);

    String saveContent(InputStream content, String path);

    void deleteContent(String path);
}
