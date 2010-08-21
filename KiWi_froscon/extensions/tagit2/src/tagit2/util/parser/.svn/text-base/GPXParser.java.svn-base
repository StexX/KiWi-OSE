package tagit2.util.parser;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import tagit2.util.parser.gpx.Track;
import tagit2.util.parser.gpx.Trackpoint;

/**
 *
 * @author perrym
 */
public class GPXParser extends DefaultHandler {
	
    private static final DateFormat TIME_FORMAT
            = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private Track track;
    
    //parsing buffer
    private StringBuffer buf = new StringBuffer();
    private Trackpoint trackpoint;

    public Track parse(InputStream in) throws IOException {
        try {
        	//new Track
        	track = new Track();
        	
        	//create parser
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(true);
            SAXParser parser = factory.newSAXParser();
            //parse
            parser.parse(in,this);
            
            return track;
            
        } catch (ParserConfigurationException e) {
            throw new IOException(e.getMessage());
        } catch (SAXException e) {
            throw new IOException(e.getMessage());
        }
    }

    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {
        buf.setLength(0);
        if (qName.equals("trkpt")) {
        	trackpoint = new Trackpoint(Double.parseDouble(attributes.getValue("lat")),
        			Double.parseDouble(attributes.getValue("lon")));
        }
    }

    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        if (qName.equals("trkpt")) {
            track.add(trackpoint);
        } else if (qName.equals("ele")) {
            trackpoint.setAltitude(Double.parseDouble(buf.toString()));
        } else if (qName.equals("")) {
            try {
                trackpoint.setTime(TIME_FORMAT.parse(buf.toString()));
            } catch (ParseException e) {
                throw new SAXException("Invalid time " + buf.toString());
            }
        }
    }

    @Override
    public void characters(char[] chars, int start, int length)
            throws SAXException {
        buf.append(chars, start, length);
    }

}
