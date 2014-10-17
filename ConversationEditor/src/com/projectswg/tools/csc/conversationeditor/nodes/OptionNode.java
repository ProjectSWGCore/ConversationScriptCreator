package com.projectswg.tools.csc.conversationeditor.nodes;

import java.lang.reflect.InvocationTargetException;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;

public class OptionNode extends ConversationNode {

    private int number;
    private String displayText = "";
    
    public OptionNode(String stf, int id, int optionNumber) {
        super(getImgDirectory() + "conversation_option.png", ConversationNode.OPTION, stf, id);
        
        Sheet.Set properties = Sheet.createPropertiesSet();
        
        properties.setName("optionNode"); // Name must be set or properties will not show properly
        properties.setDisplayName("Conversation Option");
        properties.setShortDescription("Properties specific to Option Node");
        
        properties.put(new OptionIdProperty(this));
        properties.put(new DisplayTextProperty(this));
        
        addNodeProperties(properties);
        
        this.number = optionNumber;
    }
    
    public OptionNode(String stf, int id) {
        this(stf, id, 0);
    }
    
    public void setNumber(int num) {
        this.number = num;
    }
    
    public int getNumber() {
        return number;
    }
    
    public void setDisplayText(String txt) {
        this.displayText = txt;
    }
    
    public String getDisplayText() {
        return displayText;
    }
    
    @Override
    public boolean doesLink(ConversationNode target) {
        switch (target.getType()) {
            case ConversationNode.OPTION:
                return false;
            case ConversationNode.RESPONSE:
                return true;
            case ConversationNode.END:
                return true;
            case ConversationNode.BEGIN:
                return false;
        }
        return true;
    }
}

class OptionIdProperty extends PropertySupport.ReadWrite<Integer> {

    private final OptionNode node;
    
    public OptionIdProperty(OptionNode node) {
        super("optionId", Integer.class, "Option ID", "ID used for displaying the order of this option in relation to the response (what option should show first in"
                + "list of options)");
        this.node = node;
    }
    
    @Override
    public Integer getValue() throws IllegalAccessException, InvocationTargetException {
        return node.getNumber();
    }

    @Override
    public void setValue(Integer val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        node.setNumber(val);
    }
}

class DisplayTextProperty extends PropertySupport.ReadWrite<String> {

    private final OptionNode node;
    
    public DisplayTextProperty(OptionNode node) {
        super("displayTxt", String.class, "Display Text", "Text displayed in comment for this node at compile time");
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
