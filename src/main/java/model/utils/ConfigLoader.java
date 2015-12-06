package model.utils;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

public class ConfigLoader {
    private static final String SERVER_HOST = "ServerHost";
    private static final String SERVER = "server";
    private static final String PORT = "port";
    private static final int DEFAULT_PORT_NUMBER = 8000;

    public static Map<String, Object> loadXMLConfigsFromFile(File xmlFile) throws ParserConfigurationException, IOException, SAXException {
        Map<String, Object> configs = new HashMap<>();

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(xmlFile);
        String stringPortNumber = document.getElementsByTagName(SERVER).item(0).getAttributes().getNamedItem(PORT).getNodeValue();

        Integer portNumber = StringUtils.isEmpty(stringPortNumber) ? DEFAULT_PORT_NUMBER : Integer.parseInt(stringPortNumber);
        String serverAddrres = document.getElementsByTagName(SERVER_HOST).item(0).getChildNodes().item(0).getNodeValue();
        serverAddrres = StringUtils.isEmpty(serverAddrres) ? InetAddress.getLocalHost().toString() : serverAddrres;

        configs.put("port",portNumber);
        configs.put("ip",serverAddrres);

        return configs;
    }
}
