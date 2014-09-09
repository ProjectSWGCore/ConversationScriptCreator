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
import java.io.PrintWriter;
import javax.swing.JOptionPane;
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
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

@ActionID(
        category = "File",
        id = "com.projectswg.tools.csc.conversationeditor.actions.SaveConversation"
)
@ActionRegistration(
        iconBase = "com/projectswg/tools/csc/conversationeditor/actions/conversation_tb_save.png",
        displayName = "Save"
)
@ActionReferences({
    @ActionReference(path = "Menu/File", position = -90, separatorBefore = -140),
    @ActionReference(path = "Toolbars/File", position = 500),
    @ActionReference(path = "Shortcuts", name = "D-S")
})
@Messages("CTL_SaveConversation=Save")
public final class SaveConversation implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        TopComponent component = TopComponent.getRegistry().getActivated();
        if (component == null || !(component instanceof EditorTopComponent))
            return;
        
        final EditorTopComponent editor = (EditorTopComponent) component;
        final SceneView scene = editor.getScene();
        
        if (scene == null)
            return;
        
        File existingFile = null;
        
        if (!scene.getScenePath().equals(""))
            existingFile = new File(scene.getScenePath());
        
        if (existingFile == null || !existingFile.exists()) {
            saveAs(scene, editor);
        } else {
            final String path = existingFile.getAbsolutePath();
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    try {
                        handleSave(scene, editor, path);
                    } catch (ParserConfigurationException | IOException | SAXException | TransformerException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            });
        }
    }
    
    public static void handleSave(final SceneView scene, final EditorTopComponent editor, String path) 
            throws ParserConfigurationException, IOException, SAXException, TransformerConfigurationException, TransformerException {
        SceneSaver saver = new SceneSaver();
        
        if (!path.endsWith(".xml"))
            path = path + ".xml";
        
        File xmlFile = new File(path);
        if (!scene.getScenePath().equals(path) && xmlFile.exists()) {
            int result = JOptionPane.showConfirmDialog(null, "This file already exists. Do you wish to overwrite it?", 
                    "Conversation Script Creator", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (result == JOptionPane.YES_OPTION) {
                PrintWriter writer = new PrintWriter(xmlFile);
                writer.print("");
                writer.flush();
            } else {
                return;
            }
        }
        else
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
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        
        transformer.transform(source, result);
        
        if (!scene.getSceneName().equals(xmlFile.getName().split(".xml")[0]))
            scene.setSceneName(xmlFile.getName().split(".xml")[0]);
 
        scene.setScenePath(xmlFile.getAbsolutePath());
        editor.setName(scene.getSceneName());
    }
    
    public static void saveAs(final SceneView scene, final EditorTopComponent editor) {
        File home = new File(System.getProperty("user.home"));

        FileChooserBuilder builder = new FileChooserBuilder("user-dir").setTitle("Save Conversation").setDefaultWorkingDirectory(home).setApproveText("Save");
        builder.setAcceptAllFileFilterUsed(true);
            
        final File file = builder.showSaveDialog();
            
        if (file != null) {
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    try {
                        handleSave(scene, editor, file.getAbsolutePath());
                    } catch (ParserConfigurationException | IOException | SAXException | TransformerException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            });
        }
    }
}
