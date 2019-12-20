package CreateProcess.models;

import org.apache.commons.lang.WordUtils;
import org.jetbrains.annotations.NotNull;

public class Variable {
    public String raw;
    public String pascalCase;
    public String camelCase;
    public String snakeCase;
    public String snakeAllCapsCase;
    public String kebabCase;

    protected Variable(@NotNull String name) {
        raw = WordUtils.capitalizeFully(name.replaceAll("[^A-Za-z0-9]+", " "));
        pascalCase = raw.replaceAll("\\s", "");
        camelCase = Character.toLowerCase(pascalCase.charAt(0)) + pascalCase.substring(1);
        snakeCase = raw.toLowerCase().replaceAll("\\s", "_");
        snakeAllCapsCase = snakeCase.toUpperCase();
        kebabCase = raw.toLowerCase().replaceAll("\\s", "-");
    }
}
