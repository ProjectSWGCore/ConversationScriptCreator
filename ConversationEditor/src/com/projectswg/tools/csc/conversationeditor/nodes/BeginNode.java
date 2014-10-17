package com.projectswg.tools.csc.conversationeditor.nodes;

public class BeginNode extends ConversationNode {

    public BeginNode(String stf, int id) {
        super(getImgDirectory() + "conversation_begin.png", ConversationNode.BEGIN, stf, id);
    }

    @Override
    public boolean doesLink(ConversationNode target) {
        switch (target.getType()) {
            case ConversationNode.OPTION:
                return true;
            case ConversationNode.RESPONSE:
                return false;
            case ConversationNode.END:
                return false;
            case ConversationNode.BEGIN:
                return false;
        }
        return true;
    }
}
