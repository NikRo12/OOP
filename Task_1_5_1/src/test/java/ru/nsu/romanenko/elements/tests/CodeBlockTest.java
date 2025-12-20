package ru.nsu.romanenko.elements.tests;

import elements.CodeBlock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CodeBlockTest {

    @Test
    @DisplayName("Проверка базового рендеринга с языком")
    void testBasicRender() {
        CodeBlock cb = new CodeBlock("System.out.println(\"Hello\");", "java");

        String expected =
                "```java\n" +
                        "System.out.println(\"Hello\");\n" +
                        "```";

        assertEquals(expected, cb.render());
    }

    @Test
    @DisplayName("Проверка Builder и многострочного кода")
    void testBuilderMultiLine() {
        CodeBlock cb = new CodeBlock.Builder()
                .setLanguage("python")
                .addLine("def main():")
                .addLine("    print('test')")
                .build();

        String expected =
                "```python\n" +
                        "def main():\n" +
                        "    print('test')\n" +
                        "```";

        assertEquals(expected, cb.render());
    }

    @Test
    @DisplayName("Проверка корректного разбиения строк (split)")
    void testLineParsing() {
        CodeBlock cb = new CodeBlock("line1\n\nline3", "txt");

        assertEquals(3, cb.getLineCount());

        String expected =
                "```txt\n" +
                        "line1\n" +
                        "\n" +
                        "line3\n" +
                        "```";
        assertEquals(expected, cb.render());
    }

    @Test
    @DisplayName("Проверка рендеринга без указания языка")
    void testNoLanguage() {
        CodeBlock cb = new CodeBlock("plain text", null);

        String expected = "```\nplain text\n```";
        assertEquals(expected, cb.render());
    }
}