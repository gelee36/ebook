
package com.gizrak.ebook.utils;

import android.graphics.Bitmap;

import com.gizrak.ebook.model.BookItem;
import com.gizrak.ebook.model.BookItem.Type;
import com.gizrak.ebook.model.ChapterItem;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.zip.ZipFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class EpubParser {

    private EpubParser() {

    }

    public static BookItem parse(File file) {
        ZipFile zip = null;
        BookItem book = null;
        try {
            zip = new ZipFile(file);

            // Find opf file from container file
            InputStream is = zip.getInputStream(zip.getEntry("META-INF/container.xml"));
            String path = getOpfFilePath(is);
            String base = new File(path).getParent();
            is.close();

            // Read book information from opf file
            is = zip.getInputStream(zip.getEntry(path));
            book = readOpf(is, base);
            book.setPath(file.getAbsolutePath());
            book.setType(Type.EPUB.toInteger());
            is.close();

            // Read table of contents from ncx file
            is = zip.getInputStream(zip.getEntry(book.getTocFilePath()));
            book.setToc(readNcx(is, base));
            is.close();

            // Read cover image
            Bitmap cover = ImageUtils.decodeSampledBitmap(zip, book.getCoverFilePath(), 100, 100);
            if (cover != null) {
                book.setCover(ImageUtils.toByteArray(cover));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (zip != null) {
                    zip.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return book;
    }

    private static String getOpfFilePath(InputStream in) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(in);

            NodeList items = doc.getElementsByTagName("rootfile");
            for (int i = 0, size = items.getLength(); i < size; i++) {
                Node item = items.item(i);
                NamedNodeMap attrs = item.getAttributes();
                if ("application/oebps-package+xml".equals(attrs.getNamedItem("media-type")
                        .getTextContent())) {
                    return attrs.getNamedItem("full-path").getTextContent();
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static BookItem readOpf(InputStream in, String base) {
        BookItem book = new BookItem();
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(in, "utf-8");
            parser.nextTag();

            while (parser.next() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();
                if (name.equals("title")) {
                    book.setTitle(readText(parser, name));
                } else if (name.equals("creator")) {
                    book.setAuthor(readText(parser, name));
                } else if (name.equals("language")) {
                    book.setLanguage(readText(parser, name));
                } else if (name.equals("identifier")) {
                    book.setIsbn(readText(parser, name));
                } else if (name.equals("item")) {
                    String value = parser.getAttributeValue(null, "media-type");
                    if ("application/x-dtbncx+xml".equals(value)) {
                        String href = parser.getAttributeValue(null, "href");
                        book.setTocFilePath(new File(base, href).toString());
                    } else if ("image/jpeg".equals(value)) {
                        String href = parser.getAttributeValue(null, "href");
                        book.setCoverFilePath(new File(base, href).toString());
                    }
                }
            }
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return book;
    }

    private static String readText(XmlPullParser parser, String name)
            throws XmlPullParserException, IOException {
        String text = null;
        if (parser.next() == XmlPullParser.TEXT) {
            text = parser.getText();
            parser.nextTag();
        }
        parser.require(XmlPullParser.END_TAG, null, name);
        return text;
    }

    private static ArrayList<ChapterItem> readNcx(InputStream in, String parent) {
        ArrayList<ChapterItem> list = new ArrayList<ChapterItem>();
        ChapterItem item = null;
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(in, "utf-8");
            parser.nextTag();

            while (parser.next() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();
                if ("navPoint".equals(name)) {
                    String id = parser.getAttributeValue(null, "id");
                    item = new ChapterItem(id);
                    String order = parser.getAttributeValue(null, "playOrder");
                    item.setOrder(Integer.valueOf(order));

                    while (!(parser.next() == XmlPullParser.END_TAG && "navPoint".equals(parser
                            .getName()))) {
                        if (parser.getEventType() != XmlPullParser.START_TAG) {
                            continue;
                        }
                        name = parser.getName();
                        if ("navLabel".equals(name)) {
                            parser.nextTag();
                            name = parser.getName();
                            item.setLabel(readText(parser, name));
                        } else if ("content".equals(name)) {
                            item.setContent(new File(parent,
                                    parser.getAttributeValue(null, "src")).toString());
                        }
                    }
                    list.add(item);
                }
            }
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;
    }
}
