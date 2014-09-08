package com.projectswg.tools.csc.conversationeditor;

import java.lang.reflect.InvocationTargetException;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.Lookup;

public class ConversationNode extends AbstractNode implements Lookup.Provider {
    private final int id;
    private int optionId;
    private String displayText;
    private String stf;
    private boolean option;
    private boolean locked;
    private boolean compiled;
    private boolean endNode;
    private boolean startNode;
    
    public ConversationNode(String stf, boolean isOption, int id, boolean isEndNode, boolean isStartNode, int optionId) {
        super(Children.LEAF);
        
        this.stf = stf;
        this.option = isOption;
        this.displayText = "";
        this.id = id;
        this.endNode = isEndNode;
        this.startNode = isStartNode;
        this.optionId = optionId;
        
        // Properties Window
        if (!isStartNode)
            setDisplayName("Conversation " + ((isOption) ? "Option "  + String.valueOf(id) : ((isEndNode) ? "End" : "Response "  + String.valueOf(id))));
        else
            setDisplayName("Begin Conversation Node");
        
        setShortDescription("Properties for this Conversation Node");        
    }

    public String getStf() {
        return stf;
    }

    public void setStf(String stf) {
        firePropertyChange("stf", this.stf, stf);
        this.stf = stf;
    }

    public String getDisplayText() {
        return displayText;
    }

    public void setDisplayText(String displayText) {
        firePropertyChange("display", this.displayText, displayText);
        this.displayText = displayText;
    }
    
    public boolean isOption() {
        return option;
    }

    public void setOption(boolean option) {
        this.option = option;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public int getId() {
        return id;
    }
    
    public boolean isCompiled() {
        return compiled;
    }
    
    public void setCompiled(boolean compiled) {
        this.compiled = compiled;
    }

    public boolean isEndNode() {
        return endNode;
    }

    public void setEndNode(boolean endNode) {
        this.endNode = endNode;
    }
    
    public boolean isStartNode() {
        return this.startNode;
    }
    
    public void setStartNode(boolean startNode) {
        this.startNode = startNode;
    }
    
    public int getOptionId() {
        return this.optionId;
    }
    
    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }
    
    @Override
    protected Sheet createSheet() {
        Sheet sheet = super.createSheet();
        Sheet.Set set = Sheet.createPropertiesSet();
        
        set.put(new IdProperty(this));
        set.put(new StfProperty(this)); 
        set.put(new DisplayProperty(this));        
        if (!endNode) {
            set.put(new TypeProperty(this));
            
            if (option) {
                set.put(new OptionIdProperty(this));
            }
        }
        set.put(new LockedProperty(this));
        
        sheet.put(set);
        return sheet;
    }
}

class OptionIdProperty extends PropertySupport.ReadWrite<Integer> {

    private final ConversationNode node;
    
    public OptionIdProperty(ConversationNode node) {
        super("optionId", Integer.class, "Option ID", "ID used for displaying the order of this option in relation to the response (what option should show first in"
                + "list of options)");
        this.node = node;
    }
    
    @Override
    public Integer getValue() throws IllegalAccessException, InvocationTargetException {
        return node.getOptionId();
    }

    @Override
    public void setValue(Integer val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        node.setOptionId(val);
    }
    
}

class IdProperty extends PropertySupport.ReadOnly<Integer> {
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
class StfProperty extends PropertySupport.ReadWrite<String> {

    private final ConversationNode node;
    
    public StfProperty(ConversationNode node) {
        super("stf", String.class, "STF", "STF file for this conversation node. Ex: conversation/c_newbie_mentor:s_109");
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

class TypeProperty extends PropertySupport.ReadOnly<Boolean> {

    private final ConversationNode node;
    
    public TypeProperty(ConversationNode node) {
        super("option", Boolean.class, "Option", "The conversation node is a response or a option");
        this.node = node;
    }
    
    @Override
    public Boolean getValue() throws IllegalAccessException, InvocationTargetException {
        return node.isOption();
    }
    
    @Override
    public void setValue(Boolean val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        node.setOption(val);
    }
}

class DisplayProperty extends PropertySupport.ReadWrite<String> {
    private final ConversationNode node;
    
    public DisplayProperty(ConversationNode node) {
        super ("display", String.class, "Text", "Optional text. This won't affect anything and can be left blank.\n" +
                "You can use this for entering the value of a key from the coresponding STF for quick reference.");
        this.node = node;
    }

    @Override
    public String getValue() throws IllegalAccessException, InvocationTargetException {
        return node.getDisplayText();
    }

    @Override
    public void setValue(String val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        node.setDisplayText(val);
    }
}

class LockedProperty extends PropertySupport.ReadWrite<Boolean> {
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
