package com.projectswg.tools.csc.conversationeditor.nodes;

public class ResponseNode extends ConversationNode {

    public ResponseNode(String stf, int id) {
        super(getImgDirectory() + "conversation_response.png", ConversationNode.RESPONSE, stf, id);
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
