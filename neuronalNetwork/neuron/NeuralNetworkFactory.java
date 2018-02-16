package neuron;

import java.io.InputStream;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;

public class NeuralNetworkFactory {
	private NeuralNetworkFactory(){};
	
	public static NeuralNetwork from( InputStream inputStream ){
		XMLInputFactory xmlFactory = XMLInputFactory.newInstance();
		try {
			XMLStreamReader reader = xmlFactory.createXMLStreamReader(inputStream);
			
			while(reader.hasNext()){
				int event = reader.next();
				
				if (event == XMLStreamConstants.START_ELEMENT) {
					System.out.println(reader.getLocalName());
			    }else if( event == XMLStreamConstants.END_ELEMENT ){
			    	
			    }else
			    	System.out.println(reader.getText());
				
				
			}
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
		
		return new NeuralNetwork(1,2);
	}
	
	private NeuralNetworkConfig readConfig(XMLStreamReader reader) throws XMLStreamException{
		Neuron neuron = new Neuron();
	}
	
	private String readCharacters(XMLStreamReader reader) throws XMLStreamException {
        StringBuilder result = new StringBuilder();
        while (reader.hasNext()) {
            int eventType = reader.next();
            switch (eventType) {
                case XMLStreamReader.CHARACTERS:
                case XMLStreamReader.CDATA:
                    result.append(reader.getText());
                    break;
                case XMLStreamReader.END_ELEMENT:
                    return result.toString();
            }
        }
        throw new XMLStreamException("Premature end of file");
    }
	
	public final static String getEventTypeString(int eventType) {
	    switch (eventType) {
	        case XMLEvent.START_ELEMENT:
	            return "START_ELEMENT";

	        case XMLEvent.END_ELEMENT:
	            return "END_ELEMENT";

	        case XMLEvent.PROCESSING_INSTRUCTION:
	            return "PROCESSING_INSTRUCTION";

	        case XMLEvent.CHARACTERS:
	            return "CHARACTERS";

	        case XMLEvent.COMMENT:
	            return "COMMENT";

	        case XMLEvent.START_DOCUMENT:
	            return "START_DOCUMENT";

	        case XMLEvent.END_DOCUMENT:
	            return "END_DOCUMENT";

	        case XMLEvent.ENTITY_REFERENCE:
	            return "ENTITY_REFERENCE";

	        case XMLEvent.ATTRIBUTE:
	            return "ATTRIBUTE";

	        case XMLEvent.DTD:
	            return "DTD";

	        case XMLEvent.CDATA:
	            return "CDATA";

	        case XMLEvent.SPACE:
	            return "SPACE";
	    }
	    return "UNKNOWN_EVENT_TYPE , " + eventType;
	}
}
