package com.projectswg.tools.csc.conversationeditor;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.SwingUtilities;
import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.Widget;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

// Based off of http://bits.netbeans.org/dev/javadoc/org-netbeans-api-visual/org/netbeans/api/visual/widget/doc-files/documentation.html#Serialization
public class SceneSaver {
    private static final String NODE_ELEMENT = "SceneView"; // NOI18N
    private static final String VERSION_ATTR = "version"; // NOI18N
    private static final String NODE_NODE = "Node"; // NOI18N
    
    private static final String TYPE = "type"; // NOI18N
    private static final String NODEID_ATTR = "nodeID"; // NOI18N
    private static final String X_NODE = "posX"; // NOI18N
    private static final String Y_NODE = "posY"; // NOI18N
    private static final String SOURCE = "source";
    private static final String LOCKED = "locked";
    private static final String STF = "stf";
    private static final String OPTION_ID = "optionId";
    
    private static final String VERSION_VALUE_1 = "1.0"; // NOI18N

    public Node serializeData (SceneView scene, Document file) {

        Node root = file.createElement("Conversation");

        Node properties = file.createElement("Properties");
        root.appendChild(properties);
        
        setTextProperty(properties, file, "name", scene.getSceneName());
        
        Node sceneXMLNode = file.createElement(NODE_ELEMENT);
        root.appendChild(sceneXMLNode);
        
        setAttribute(file, sceneXMLNode, VERSION_ATTR, VERSION_VALUE_1); // NOI18N
        
        LinkedHashMap<ConversationNode, ArrayList<ConversationNode>> conversationLinks = scene.getConversationLinks();
        
        for (ConversationNode node : scene.getNodes ()) {
                Widget widget = scene.findWidget(node);
                if (widget != null) {
                    Point location = widget.getPreferredLocation();
                    if (location != null) {
                        Node dataXMLNode = file.createElement(NODE_NODE);
                        
                        if (node.isOption())
                            setAttribute(file, dataXMLNode, TYPE, "option");
                        else if (!node.isEndNode() && !node.isStartNode())
                            setAttribute(file, dataXMLNode, TYPE, "response");
                        else if (node.isEndNode())
                            setAttribute(file, dataXMLNode, TYPE, "end");
                        else
                            setAttribute(file, dataXMLNode, TYPE, "begin");
                        
                        if (!node.isStartNode())
                            setAttribute(file, dataXMLNode, SOURCE, String.valueOf(getSourceNode(conversationLinks, node).getId()));

                        setAttribute(file, dataXMLNode, NODEID_ATTR, String.valueOf(node.getId()));
                        setAttribute(file, dataXMLNode, X_NODE, Integer.toString(location.x));
                        setAttribute(file, dataXMLNode, Y_NODE, Integer.toString(location.y));
                        setAttribute(file, dataXMLNode, LOCKED, Boolean.toString(node.isLocked()));
                        setAttribute(file, dataXMLNode, STF, node.getStf());
                        setAttribute(file, dataXMLNode, OPTION_ID, Integer.toString(node.getOptionId()));
                        
                        sceneXMLNode.appendChild(dataXMLNode);
                    }
                }
            }
        return root;
    }

    private ConversationNode getSourceNode(LinkedHashMap<ConversationNode, ArrayList<ConversationNode>> conversationLinks, ConversationNode lookNode) {
        for (Map.Entry<ConversationNode, ArrayList<ConversationNode>> entry : conversationLinks.entrySet()) {
            if (entry.getValue().contains(lookNode))
                return entry.getKey();
        }
        return null;
    }
    
    private ConversationNode getTargetNode(LinkedHashMap<ConversationNode, ArrayList<ConversationNode>> conversationLinks, ConversationNode lookNode) {
         for (Map.Entry<ConversationNode, ArrayList<ConversationNode>> entry : conversationLinks.entrySet()) {
            if (entry.getValue().contains(lookNode))
                return entry.getValue().get(entry.getValue().indexOf(lookNode));
        }
        return null;       
    }
    
    // Returns true if deserialization is successfull
    // sceneXMLNode has to be found in the XML file first
    public boolean deserializeData(final SceneView scene, final Node sceneXMLNode) {
        if (!VERSION_VALUE_1.equals(getAttributeValue(sceneXMLNode, VERSION_ATTR)))
            return false;

        SwingUtilities.invokeLater (new Runnable() {
            @Override
            public void run() {
                deserializeDataVersion1(scene, sceneXMLNode);
                scene.validate ();
            }
        });

        return true;
    }

    private void deserializeDataVersion1(SceneView scene, Node data) {
        for (Node node : getChildNode (data)) {
            if(NODE_NODE.equals(node.getNodeName())) {
                String nodeID = getAttributeValue(node, NODEID_ATTR);
                int x = Integer.parseInt(getAttributeValue (node, X_NODE));
                int y = Integer.parseInt(getAttributeValue (node, Y_NODE));
                Widget widget = scene.findWidget(nodeID);
                if (widget != null)
                    widget.setPreferredLocation (new Point (x, y));
            }
        }
    }

    private static String getAttributeValue(Node node, String attr) {
        try {
            if (node != null) {
                NamedNodeMap map = node.getAttributes ();
                if (map != null) {
                    node = map.getNamedItem (attr);
                    if (node != null)
                        return node.getNodeValue ();
                }
            }
        } catch (DOMException e) {
            //Debug.warning (e);
        }
        return null;
    }

    private static void setAttribute(Document xml, Node node, String name, String value) {
        NamedNodeMap map = node.getAttributes ();
        Attr attribute = xml.createAttribute(name);
        attribute.setValue (value);
        map.setNamedItem (attribute);
    }

    private static void setTextProperty(Node root, Document xml, String name, String value) {
        Element txtProperty = xml.createElement(name);
        txtProperty.appendChild(xml.createTextNode(value));
        root.appendChild(txtProperty);
    }
    
    private static Node[] getChildNode (Node node) {
        NodeList childNodes = node.getChildNodes ();
        Node[] nodes = new Node[childNodes != null ? childNodes.getLength () : 0];
        for (int i = 0; i < nodes.length; i++)
            nodes[i] = childNodes.item (i);
        return nodes;
    }    
}
