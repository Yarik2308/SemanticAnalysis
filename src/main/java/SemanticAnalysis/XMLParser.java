package SemanticAnalysis;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class XMLParser {
    private static ArrayList<Comment> comments = new ArrayList<>();

    public static ArrayList<Comment> parse() throws ParserConfigurationException,
            SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();

        int[] scores = new int[11];
        for (int i = 0; i<10; i++){
            scores[i] = 0;
        }

        XMLHandler handler = new XMLHandler();
        parser.parse(new File(
                "src/main/resources/MyComments.xml"), handler);

        int size = 0;
        for (Comment comment: comments){
            System.out.print(comment.getScore() + " " + comment.getText() + "\n");
            size++;
            scores[comment.getScore()]++;
        }

        System.out.print("size = " + size + '\n');
        for (int i = 0; i<11; i++){
            System.out.print(i + " = " + scores[i] + '\n');
        }
        return comments;
    }

    private static class XMLHandler extends DefaultHandler {
        private Integer score, content_id, element_id, user_id;
        private String text = "", lastElementName;
        private String attributesForCharacters;

        @Override
        public void startDocument() throws SAXException {
            // Тут будет логика реакции на начало документа
        }

        @Override
        public void endDocument() throws SAXException {
            // Тут будет логика реакции на конец документа
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes)
                throws SAXException {
            // Тут будет логика реакции на начало элемента
            lastElementName = qName;
            attributesForCharacters = attributes.getValue("columnNumber");
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            // Тут будет логика реакции на конец элемента
            if( score != null && content_id != null && element_id != null && user_id != null &&
                    !text.equals("") ){
                comments.add(new Comment(score, content_id, element_id, user_id, text));
                score = null;
                content_id = null;
                element_id = null;
                user_id = null;
                text = "";
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            String information = new String(ch, start, length);

            information = information.replace("\n", "").trim();

            if (!information.isEmpty()) {
                if (lastElementName.equals("value")
                        && attributesForCharacters.equals("0"))
                    score = Integer.parseInt(information);
                if (lastElementName.equals("value")
                        && attributesForCharacters.equals("1"))
                    content_id = Integer.parseInt(information);
                if (lastElementName.equals("value")
                        && attributesForCharacters.equals("2"))
                    element_id = Integer.parseInt(information);
                if (lastElementName.equals("value")
                        && attributesForCharacters.equals("3"))
                    user_id = Integer.parseInt(information);
                if (lastElementName.equals("value")
                        && attributesForCharacters.equals("4"))
                    text = text + information;
            }
        }

        @Override
        public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
            // Тут будет логика реакции на пустое пространство внутри элементов (пробелы, переносы строчек и так далее).
        }

    }
}
