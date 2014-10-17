package com.projectswg.tools.csc.conversationeditor.nodes;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.Lookup;

public class ConversationNode extends AbstractNode implements Lookup.Provider {

    private final int id;
    private String stf;
    private boolean locked;
    
    private final Sheet sheet;
    private final String imageStr;
    private final String type;
    
    public static final String OPTION = "Option";
    public static final String RESPONSE = "Response";
    public static final String END = "End";
    public static final String BEGIN = "Begin";
    
    HashMap<String, Object> specificAttrs = new HashMap<>();
    
    public ConversationNode(String imageStr, String type, String stf, int id) {
        super(Children.LEAF);
        
        this.id = id;
        this.stf = stf;
        this.imageStr = imageStr;
        this.sheet = super.createSheet();
        
        Sheet.Set properties = Sheet.createPropertiesSet();

        properties.setDisplayName("General");
        properties.setShortDescription("Basic properties for all conversation nodes.");
        
        properties.put(new IdProperty(this));
        properties.put(new StfProperty(this));
        properties.put(new LockedProperty(this));
        
        sheet.put(properties);
        
        this.type = type;
        
        this.setDisplayName(stf);
    }
    
    public final int getId() {
        return id;
    }
    
    public final String getStf() {
        return stf;
    }
    
    public final void setStf(String stf) {
        this.stf = stf;
    }

    public final boolean isLocked() {
        return locked;
    }
    
    public final void setLocked(boolean isLocked) {
        this.locked = isLocked;
    }
    
    public final String getImageStr() {
        return imageStr;
    }
    
    public final String getType() {
        return type;
    }
    
    public final void addNodeProperties(Sheet.Set properties) {
        sheet.put(properties);
    }

    public boolean doesLink(ConversationNode target) {
        return true;
    }
    
    @Override
    protected final Sheet createSheet() {
        return sheet;
    }
    
    public final static String getImgDirectory() {
        return "com/projectswg/tools/csc/conversationeditor/nodes/imgs/";
    }
    
    public final Object addSpecificAttribute(String name, Object value) {
        return specificAttrs.put(name, value);
    }
    
    public HashMap<String, Object> getAttributes() {
        return specificAttrs;
    }
}

final class LockedProperty extends PropertySupport.ReadWrite<Boolean> {
    private final ConversationNode node;

    public LockedProperty(ConversationNode node) {
        super("locked", Boolean.class, "Locked", "Determines if this node can be moved.");
        this.node = node;
    }

    @Override
    public Boolean getValue() throws IllegalAccessException, InvocationTargetException {
        return node.isLocked();
    }

    @Override
    public void setValue(Boolean val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        node.setLocked(val);
    }
    
}

final class StfProperty extends PropertySupport.ReadWrite<String> {

    private final ConversationNode node;
    
    public StfProperty(ConversationNode node) {
        super("stf", String.class, "STF", "STF file for this conversation node, excluding conversation/. Ex: conversation/c_newbie_mentor:s_109 would be c_newbie_mentor:s_109");
        this.setValue("oneline", true);
        this.node = node;
    }
    
    @Override
    public String getValue() throws IllegalAccessException, InvocationTargetException {
        return node.getStf();
    }

    @Override
    public void setValue(String val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        node.setStf(String.valueOf(val));
    }
}

final class IdProperty extends PropertySupport.ReadOnly<Integer> {
    private final ConversationNode node;

    public IdProperty(ConversationNode node) {
        super("id", Integer.class, "Id", "Conversation Node Id");
        this.node = node;
    }

    @Override
    public Integer getValue() throws IllegalAccessException, InvocationTargetException {
        return node.getId();
    }   
}
