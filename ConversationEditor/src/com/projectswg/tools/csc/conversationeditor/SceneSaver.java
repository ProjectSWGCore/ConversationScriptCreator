package com.projectswg.tools.csc.conversationeditor;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.HashMap;
import javax.swing.SwingUtilities;
import org.netbeans.api.visual.anchor.AnchorFactory;
import org.netbeans.api.visual.anchor.AnchorShape;
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
    private static final String TARGETS = "targets";
    private static final String LOCKED = "locked";
    private static final String STF = "stf";
    private static final String OPTION_ID = "optionId";
    
    private static final String VERSION_VALUE_1 = "1.0"; // NOI18N

    public Node serializeData (SceneView scene, Document file) {

        Node root = file.createElement("Conversation");

        Node properties = file.createElement("Properties");
        root.appendChild(properties);
        
        setTextProperty(properties, file, "zoom", Double.toString(scene.getZoomFactor()));
        setTextProperty(properties, file, "highestId", String.valueOf(scene.getNextId()));
        
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
                        
                        setAttribute(file, dataXMLNode, TARGETS, getTargets(conversationLinks, node));

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

    private String getTargets(LinkedHashMap<ConversationNode, ArrayList<ConversationNode>> conversationLinks, ConversationNode source) {
        String targets = "";
        if (!conversationLinks.containsKey(source)) {
            return targets;
        }

        for (ConversationNode nodes : conversationLinks.get(source)) {
            targets += (targets.equals("") ? String.valueOf(nodes.getId()) : "," + String.valueOf(nodes.getId()));
        }

        return targets;       
    }
    
    public boolean deserializeData(final SceneView scene, final Node sceneXMLNode) {
        if (!VERSION_VALUE_1.equals(getAttributeValue(sceneXMLNode, VERSION_ATTR)))
            return false;

        SwingUtilities.invokeLater (new Runnable() {
            @Override
            public void run() {
                deserializeNodeDataVersion1(scene, sceneXMLNode);
            }
        });

        return true;
    }

    public void deserializeProperties(SceneView scene, Node node) {
        System.out.println("Got a node!" + node.getNodeType());
        Element properties = (Element) node;
        scene.setId(Integer.valueOf(properties.getElementsByTagName("highestId").item(0).getTextContent()));
    }
    
    private void deserializeNodeDataVersion1(SceneView scene, Node data) {
        HashMap<Integer, ArrayList<Integer>> widgetLinks = new HashMap<>(); // map of what this widget id targets
        HashMap<Integer, ConversationWidget> widgets = new HashMap<>(); // map of all loaded widget's K = id, V = widget
        
        for (Node node : getChildNode (data)) {
            if(NODE_NODE.equals(node.getNodeName())) {
                
                int nodeID = Integer.parseInt(getAttributeValue(node, NODEID_ATTR));
                int x = Integer.parseInt(getAttributeValue (node, X_NODE));
                int y = Integer.parseInt(getAttributeValue (node, Y_NODE));
                int optionId = Integer.parseInt(getAttributeValue(node, OPTION_ID));

                boolean locked = Boolean.parseBoolean(getAttributeValue(node, LOCKED));                
                
                String targets = getAttributeValue(node, TARGETS);
                String stf = getAttributeValue(node, STF);
                String type = getAttributeValue(node, TYPE);
                
                ConversationWidget widget = null;
                // ConversationNode(String stf, boolean isOption, int id, boolean isEndNode, boolean isStartNode, int optionId)
                switch (type) {
                    case "option":
                        widget = (ConversationWidget) scene.addNode(new ConversationNode(stf, true, nodeID, false, false, optionId));
                        break;
                    case "response":
                        widget = (ConversationWidget) scene.addNode(new ConversationNode(stf, false, nodeID, false, false, optionId));
                        break;
                    case "begin":
                        widget = (ConversationWidget) scene.addNode(new ConversationNode(stf, false, nodeID, false, true, optionId));
                        break;
                    case "end":
                        widget = (ConversationWidget) scene.addNode(new ConversationNode(stf, false, nodeID, true, false, optionId));
                        break;
                }

                if (widget != null) {
                    widget.getAttachedNode().setLocked(locked);
                    widget.setPreferredLocation (new Point (x, y));
                    widgets.put(nodeID, widget);
                    
                    if (!targets.equals("")) {
                        ArrayList<Integer> targetIds = new ArrayList<>();
                        if (targets.contains(",")) {
                            String[] split = targets.split(",");
                            for (String s : split) {
                                targetIds.add(Integer.valueOf(s));
                            }
                        } else {
                            targetIds.add(Integer.valueOf(targets));
                        }
                        widgetLinks.put(nodeID, targetIds);
                    }
                }
            }
        }
        createConnections(widgetLinks, widgets, scene);
    }
    
    private void createConnections(HashMap<Integer, ArrayList<Integer>> links, HashMap<Integer, ConversationWidget> widgets, SceneView scene) {
        for (Map.Entry<Integer, ArrayList<Integer>> entry : links.entrySet()) {
            //System.out.println("Creating links for " + widgets.get(entry.getKey()).getAttachedNode().getStf());
            for (Integer targetId : entry.getValue()) {

                ConnectionWidget conn = new ConnectionWidget(scene);
                conn.setTargetAnchorShape(AnchorShape.TRIANGLE_FILLED);
                
                conn.setTargetAnchor(AnchorFactory.createRectangularAnchor(widgets.get(targetId)));
                conn.setSourceAnchor(AnchorFactory.createRectangularAnchor(widgets.get(entry.getKey())));
                
                scene.getConnectionLayer().addChild(conn);
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
