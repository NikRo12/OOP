package ru.nsu.romanenko.elements.tests;

import elements.List;
import elements.MarkdownElement;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("List Tests")
public class ListTest {

    @Nested
    @DisplayName("List Constructor Tests")
    class ConstructorTest {
        @Test
        @DisplayName("Should create list with type and elements")
        void shouldCreateListWithTypeAndElements() {
            ArrayList<MarkdownElement> elements = new ArrayList<>();
            elements.add(new Text("Item 1"));
            elements.add(new Text("Item 2"));

            List list = new List(List.Types.MARKED, elements);

            assertEquals(List.Types.MARKED, list.getType());
            assertEquals(2, list.getChildrenCount());
        }

        @Test
        @DisplayName("Should create list with empty elements")
        void shouldCreateListWithEmptyElements() {
            ArrayList<MarkdownElement> elements = new ArrayList<>();
            List list = new List(List.Types.NUMERIC, elements);

            assertEquals(List.Types.NUMERIC, list.getType());
            assertEquals(0, list.getChildrenCount());
        }
    }

    @Nested
    @DisplayName("List Builder Tests")
    class BuilderTest {
        @Test
        @DisplayName("Should build list with default type")
        void shouldBuildListWithDefaultType() {
            List list = new List.Builder()
                    .addElement(new Text("Item 1"))
                    .addElement(new Text("Item 2"))
                    .build();

            assertEquals(List.Types.NUMERIC, list.getType());
            assertEquals(2, list.getChildrenCount());
        }

        @Test
        @DisplayName("Should build list with marked type")
        void shouldBuildListWithMarkedType() {
            List list = new List.Builder()
                    .setType(List.Types.MARKED)
                    .addElement(new Text("Item 1"))
                    .build();

            assertEquals(List.Types.MARKED, list.getType());
            assertEquals(1, list.getChildrenCount());
        }

        @Test
        @DisplayName("Should build empty list")
        void shouldBuildEmptyList() {
            List list = new List.Builder().build();

            assertEquals(List.Types.NUMERIC, list.getType());
            assertEquals(0, list.getChildrenCount());
        }
    }

    @Nested
    @DisplayName("List Render Tests")
    class RenderTest {
        @Test
        @DisplayName("Should render numeric list correctly")
        void shouldRenderNumericListCorrectly() {
            List list = new List.Builder()
                    .setType(List.Types.NUMERIC)
                    .addElement(new Text("First"))
                    .addElement(new Text("Second"))
                    .build();

            String result = list.render();

            assertTrue(result.contains("1. First"));
            assertTrue(result.contains("2. Second"));
        }

        @Test
        @DisplayName("Should render marked list correctly")
        void shouldRenderMarkedListCorrectly() {
            List list = new List.Builder()
                    .setType(List.Types.MARKED)
                    .addElement(new Text("Apple"))
                    .addElement(new Text("Banana"))
                    .build();

            String result = list.render();

            assertTrue(result.contains("* Apple"));
            assertTrue(result.contains("* Banana"));
        }

        @Test
        @DisplayName("Should render nested lists with indentation")
        void shouldRenderNestedListsWithIndentation() {
            List innerList = new List.Builder()
                    .setType(List.Types.MARKED)
                    .addElement(new Text("Subitem 1"))
                    .addElement(new Text("Subitem 2"))
                    .build();

            List mainList = new List.Builder()
                    .setType(List.Types.NUMERIC)
                    .addElement(new Text("Main item"))
                    .addElement(innerList)
                    .build();

            String result = mainList.render();

            assertTrue(result.contains("1. Main item"));
            assertTrue(result.contains("   * Subitem 1"));
            assertTrue(result.contains("   * Subitem 2"));
        }

        @Test
        @DisplayName("Should render deeply nested lists")
        void shouldRenderDeeplyNestedLists() {
            List deepestList = new List.Builder()
                    .setType(List.Types.MARKED)
                    .addElement(new Text("Deep item"))
                    .build();

            List middleList = new List.Builder()
                    .setType(List.Types.NUMERIC)
                    .addElement(new Text("Middle item"))
                    .addElement(deepestList)
                    .build();

            List mainList = new List.Builder()
                    .setType(List.Types.MARKED)
                    .addElement(new Text("Main item"))
                    .addElement(middleList)
                    .build();

            String result = mainList.render();

            assertTrue(result.contains("* Main item"));
            assertTrue(result.contains("   1. Middle item"));
            assertTrue(result.contains("      * Deep item"));
        }

        @Test
        @DisplayName("Should render empty list as empty string")
        void shouldRenderEmptyListAsEmptyString() {
            List list = new List.Builder().build();
            String result = list.render();
            assertEquals("", result);
        }
    }

    @Nested
    @DisplayName("List Types Enum Tests")
    class TypesTest {
        @Test
        @DisplayName("Should have correct mark symbols")
        void shouldHaveCorrectMarkSymbols() {
            assertEquals("", List.Types.NUMERIC.getMark());
            assertEquals("*", List.Types.MARKED.getMark());
        }

        @Test
        @DisplayName("Should have all enum values")
        void shouldHaveAllEnumValues() {
            List.Types[] values = List.Types.values();
            assertEquals(2, values.length);
            assertArrayEquals(new List.Types[]{List.Types.NUMERIC, List.Types.MARKED}, values);
        }
    }

    @Nested
    @DisplayName("List Equality Tests")
    class EqualityTest {
        @Test
        @DisplayName("Should be equal when same type and elements")
        void shouldBeEqualWhenSameTypeAndElements() {
            List list1 = new List.Builder()
                    .setType(List.Types.MARKED)
                    .addElement(new Text("Item"))
                    .build();

            List list2 = new List.Builder()
                    .setType(List.Types.MARKED)
                    .addElement(new Text("Item"))
                    .build();

            assertEquals(list1, list2);
            assertEquals(list1.hashCode(), list2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when different types")
        void shouldNotBeEqualWhenDifferentTypes() {
            List list1 = new List.Builder()
                    .setType(List.Types.NUMERIC)
                    .addElement(new Text("Item"))
                    .build();

            List list2 = new List.Builder()
                    .setType(List.Types.MARKED)
                    .addElement(new Text("Item"))
                    .build();

            assertNotEquals(list1, list2);
        }

        @Test
        @DisplayName("Should not be equal when different elements")
        void shouldNotBeEqualWhenDifferentElements() {
            List list1 = new List.Builder()
                    .setType(List.Types.MARKED)
                    .addElement(new Text("Item 1"))
                    .build();

            List list2 = new List.Builder()
                    .setType(List.Types.MARKED)
                    .addElement(new Text("Item 2"))
                    .build();

            assertNotEquals(list1, list2);
        }

        @Test
        @DisplayName("Should not be equal to null or different class")
        void shouldNotBeEqualToNullOrDifferentClass() {
            List list = new List.Builder().build();
            assertNotEquals(null, list);
            assertNotEquals("string", list);
        }
    }

    @Nested
    @DisplayName("MarkdownElement Base Class Tests")
    class MarkdownElementTest {
        @Test
        @DisplayName("Should create element with content")
        void shouldCreateElementWithContent() {
            Text element = new Text("Hello");
            assertEquals("Hello", element.getContent());
        }

        @Test
        @DisplayName("Should handle null content")
        void shouldHandleNullContent() {
            Text element = new Text(null);
            assertEquals("", element.getContent());
        }

        @Test
        @DisplayName("Should add and remove children")
        void shouldAddAndRemoveChildren() {
            Text parent = new Text("Parent");
            Text child = new Text("Child");

            parent.addChild(child);
            assertTrue(parent.hasChildren());
            assertEquals(1, parent.getChildrenCount());

            parent.removeChild(child);
            assertFalse(parent.hasChildren());
            assertEquals(0, parent.getChildrenCount());
        }

        @Test
        @DisplayName("Should add multiple children")
        void shouldAddMultipleChildren() {
            Text parent = new Text("Parent");
            ArrayList<MarkdownElement> children = new ArrayList<>();
            children.add(new Text("Child 1"));
            children.add(new Text("Child 2"));

            parent.addChildren(children);
            assertEquals(2, parent.getChildrenCount());
        }

        @Test
        @DisplayName("Should clear children")
        void shouldClearChildren() {
            Text parent = new Text("Parent");
            parent.addChild(new Text("Child"));

            parent.clearChildren();
            assertFalse(parent.hasChildren());
            assertEquals(0, parent.getChildrenCount());
        }

        @Test
        @DisplayName("Should return defensive copy of children")
        void shouldReturnDefensiveCopyOfChildren() {
            Text parent = new Text("Parent");
            parent.addChild(new Text("Child"));

            ArrayList<MarkdownElement> children = (ArrayList<MarkdownElement>) parent.getChildren();
            children.clear();

            assertEquals(1, parent.getChildrenCount());
        }

        @Test
        @DisplayName("Should implement equals and hashCode correctly")
        void shouldImplementEqualsAndHashCodeCorrectly() {
            Text element1 = new Text("Hello");
            Text element2 = new Text("Hello");
            Text element3 = new Text("World");

            assertEquals(element1, element2);
            assertNotEquals(element1, element3);
            assertEquals(element1.hashCode(), element2.hashCode());
        }

        @Test
        @DisplayName("Should return element name")
        void shouldReturnElementName() {
            Text element = new Text("Test");
            assertEquals("Text", element.getElementName());
        }

        @Test
        @DisplayName("Should use render in toString")
        void shouldUseRenderInToString() {
            Text element = new Text("Hello");
            assertEquals(element.render(), element.toString());
        }
    }

    static class Text extends MarkdownElement {
        public Text(String content) {
            super(content);
        }

        @Override
        public String render() {
            return getContent();
        }
    }
}