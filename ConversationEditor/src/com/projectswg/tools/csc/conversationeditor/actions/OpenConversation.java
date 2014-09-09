package com.projectswg.tools.csc.conversationeditor.actions;

import com.projectswg.tools.csc.conversationeditor.EditorTopComponent;
import com.projectswg.tools.csc.conversationeditor.SceneSaver;
import com.projectswg.tools.csc.conversationeditor.SceneView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.filesystems.FileChooserBuilder;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@ActionID(category = "File", id = "org.mycore.OpenFileAction")
@ActionRegistration(displayName = "Open")
@ActionReferences({
    @ActionReference(path = "Menu/File", position = 10, separatorAfter = 11),
    @ActionReference(path = "Shortcuts", name = "D-O")
})
@Messages("CTL_OpenFileAction=Open")
public final class OpenConversation implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        //The default dir to use if no value is stored
        File home = new File(System.getProperty("user.home"));

        File toAdd = new FileChooserBuilder("user-dir").setTitle("Open Conversation").
                setDefaultWorkingDirectory(home).setApproveText("Open").showOpenDialog();
        //Result will be null if the user clicked cancel or closed the dialog w/o OK
        if (toAdd != null) {
            EditorTopComponent editor = new EditorTopComponent();
            open(toAdd, editor);
            editor.open();
            editor.requestActive();
        }
    }
    
    public static void open(File openFile, EditorTopComponent editor) {
        try {
            if (!openFile.getName().endsWith(".xml")) {
                System.out.println("Not a valid Conversation Editor file!");
                return;
            }

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(openFile);
            doc.getDocumentElement().normalize();

            if (!doc.getDocumentElement().getNodeName().equals("Conversation")) {
                System.out.println("Not a valid Conversation Editor file!");
                return;
            }

            SceneSaver saver = new SceneSaver();
            
            SceneView scene = editor.getScene();
            
            Node propertiesNode = doc.getElementsByTagName("Properties").item(0);
            saver.deserializeProperties(scene, propertiesNode);
            
            Node sceneNode = doc.getElementsByTagName("SceneView").item(0);
            if (sceneNode == null) {
                System.out.println("NULL SCENE");
                return;
            }
            scene.setSceneName(openFile.getName().split(".xml")[0]);
            scene.setScenePath(openFile.getAbsolutePath());
            
            saver.deserializeData(scene, sceneNode);
            editor.setName(scene.getSceneName());
            
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Exceptions.printStackTrace(ex);
        }   
    }
}
