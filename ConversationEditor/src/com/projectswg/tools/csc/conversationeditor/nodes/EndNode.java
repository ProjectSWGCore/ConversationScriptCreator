package com.projectswg.tools.csc.conversationeditor.nodes;

public class EndNode extends ConversationNode {

    public EndNode(String stf, int id) {
        super(getImgDirectory() + "conversation_end.png", ConversationNode.END, stf, id);
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
