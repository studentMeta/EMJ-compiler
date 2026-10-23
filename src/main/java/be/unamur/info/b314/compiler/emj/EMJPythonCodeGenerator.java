package be.unamur.info.b314.compiler.emj;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class EMJPythonCodeGenerator {

    // Variable de représentation
    private StringBuilder codeBuilder;
    private int indentationLevel;

    // Constructeur
    public EMJPythonCodeGenerator() {
        this.codeBuilder = new StringBuilder();
        this.indentationLevel = 0;
    }

    // Méthodes du domaine

    /**
     * Increase the indentation level of one unit
     * @modifies indentationLevel
     * @effects indentationLevel_post = indentationLevel_pre + 1
     */
    public void increaseIndentation() {
        this.indentationLevel++;
    }

    /**
     * Decrease the indentation level of one unit
     * @modifies indentationLevel
     * @effects indentationLevel_post = indentationLevel_pre - 1
     */
    public void decreaseIndentation() {
        this.indentationLevel--;
    }

    /**
     * Append to the generated code: one tab per indentation level,
     *                               an additional line of code,
     *                               a new line character
     * @param line The line to be added to the code
     * @requires line != null
     * @modifies codeBuilder
     * @effects codeBuilder_post contains the contents of codeBuilder_pre with
     *          - indentationLevel tabs,
     *          - line,
     *          - the new line escape character
     *          appended
     */
    public void addLine(String line) {
        for (int i = 0; i < this.indentationLevel; i++) {
            codeBuilder.append("    ");
        }

        codeBuilder.append(line);
        codeBuilder.append("\n");
    }

    /**
     * Get the generated code
     * @return A string containing the generated code
     */
    public String getCode() {
        return codeBuilder.toString();
    }

}