package Utilities;

import org.apache.commons.lang.WordUtils;
import org.jetbrains.annotations.NotNull;

public class Constant {

    public Constant keyConstant;

    public String raw;
    public String pascalCase;
    public String camelCase;
    public String snakeCase;
    public String snakeAllCapsCase;
    public String kebabCase;

    public Constant(@NotNull String name) {
        raw = WordUtils.capitalizeFully(name.replaceAll("[^A-Za-z0-9]+", " "));
        pascalCase = raw.replaceAll("\\s", "");
        camelCase = Character.toLowerCase(pascalCase.charAt(0)) + pascalCase.substring(1);
        snakeCase = raw.toLowerCase().replaceAll("\\s", "_");
        snakeAllCapsCase = snakeCase.toUpperCase();
        kebabCase = raw.toLowerCase().replaceAll("\\s", "-");
    }

    public Constant(@NotNull String key, @NotNull String value) {
        this(value);
        keyConstant = new Constant(key);
    }

    public String format(String text) {
        if (keyConstant == null) return text;
        text = text.replace(String.format("$%s$", keyConstant.raw), raw);
        text = text.replace(String.format("$%s$", keyConstant.pascalCase), pascalCase);
        text = text.replace(String.format("$%s$", keyConstant.camelCase), camelCase);
        text = text.replace(String.format("$%s$", keyConstant.snakeCase), snakeCase);
        text = text.replace(String.format("$%s$", keyConstant.snakeAllCapsCase), snakeAllCapsCase);
        text = text.replace(String.format("$%s$", keyConstant.kebabCase), kebabCase);
        return text;
    }
}
