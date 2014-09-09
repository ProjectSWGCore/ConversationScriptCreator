/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.projectswg.tools.csc.conversationeditor.actions;

import com.projectswg.tools.csc.conversationeditor.EditorTopComponent;
import com.projectswg.tools.csc.conversationeditor.SceneSaver;
import com.projectswg.tools.csc.conversationeditor.SceneView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.SwingUtilities;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.filesystems.FileChooserBuilder;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

@ActionID(
        category = "File",
        id = "com.projectswg.tools.csc.conversationeditor.actions.SaveConversation"
)
@ActionRegistration(
        iconBase = "com/projectswg/tools/csc/conversationeditor/actions/conversation_tb_save.png",
        displayName = "Save Conversation"
)
@ActionReferences({
    @ActionReference(path = "Menu/File", position = -90, separatorBefore = -140),
    @ActionReference(path = "Toolbars/File", position = 500),
    @ActionReference(path = "Shortcuts", name = "D-S")
})
@Messages("CTL_SaveConversation=Save Conversation")
public final class SaveConversation implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        TopComponent component = TopComponent.getRegistry().getActivated();
        if (component == null || !(component instanceof EditorTopComponent))
            return;
        
        EditorTopComponent editor = (EditorTopComponent) component;
        final SceneView scene = editor.getScene();
        
        if (scene == null)
            return;
        
        if (scene.getSceneName().equals("")) {
            File home = new File(System.getProperty("user.home"));

            FileChooserBuilder builder = new FileChooserBuilder("user-dir").setTitle("Save Conversation").setDefaultWorkingDirectory(home).setApproveText("Save");
            builder.setAcceptAllFileFilterUsed(true);
            
            final File file = builder.showSaveDialog();
            
            if (file != null) {
                scene.setSceneName(file.getName().split(".xml")[0]);
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            save(scene, file.getAbsolutePath());
                        } catch (ParserConfigurationException | IOException | SAXException | TransformerException ex) {
                            Exceptions.printStackTrace(ex);
                        }
                    }
                });
                editor.setName("Conversation Editor - " + scene.getSceneName());
            }
        }
    }
    
    public static void save(SceneView scene, String path) throws ParserConfigurationException, IOException, SAXException, TransformerConfigurationException, TransformerException {
        SceneSaver saver = new SceneSaver();
        
        File xmlFile = new File(path + ".xml");
        if (!xmlFile.exists())
            xmlFile.createNewFile();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        Node xmlNode = saver.serializeData(scene, document);
        document.appendChild(xmlNode);
        
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
	Transformer transformer = transformerFactory.newTransformer();
	
        DOMSource source = new DOMSource(document);
	StreamResult result = new StreamResult(xmlFile);
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        
        transformer.transform(source, result);
    }
}
