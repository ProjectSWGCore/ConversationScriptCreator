/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.projectswg.tools.csc.conversationeditor.wizard;

import java.awt.event.ItemEvent;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public final class CompileVisualPanel2 extends JPanel {

    /**
     * Creates new form CompileVisualPanel2
     */
    public CompileVisualPanel2() {
        initComponents();
    }

    @Override
    public String getName() {
        return "Set Options";
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        radialIndentation = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        radioSpaces = new javax.swing.JRadioButton();
        radioTabs = new javax.swing.JRadioButton();
        chkbxComments = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtAreaHeader = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        tbScriptName = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(CompileVisualPanel2.class, "CompileVisualPanel2.jLabel1.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(CompileVisualPanel2.class, "CompileVisualPanel2.jLabel2.text")); // NOI18N

        radialIndentation.add(radioSpaces);
        org.openide.awt.Mnemonics.setLocalizedText(radioSpaces, org.openide.util.NbBundle.getMessage(CompileVisualPanel2.class, "CompileVisualPanel2.radioSpaces.text")); // NOI18N

        radialIndentation.add(radioTabs);
        radioTabs.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(radioTabs, org.openide.util.NbBundle.getMessage(CompileVisualPanel2.class, "CompileVisualPanel2.radioTabs.text")); // NOI18N

        chkbxComments.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(chkbxComments, org.openide.util.NbBundle.getMessage(CompileVisualPanel2.class, "CompileVisualPanel2.chkbxComments.text")); // NOI18N
        chkbxComments.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chkbxCommentsItemStateChanged(evt);
            }
        });

        txtAreaHeader.setColumns(20);
        txtAreaHeader.setRows(5);
        txtAreaHeader.setText(org.openide.util.NbBundle.getMessage(CompileVisualPanel2.class, "CompileVisualPanel2.txtAreaHeader.text")); // NOI18N
        jScrollPane1.setViewportView(txtAreaHeader);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(CompileVisualPanel2.class, "CompileVisualPanel2.jLabel3.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel4, org.openide.util.NbBundle.getMessage(CompileVisualPanel2.class, "CompileVisualPanel2.jLabel4.text")); // NOI18N

        tbScriptName.setText(org.openide.util.NbBundle.getMessage(CompileVisualPanel2.class, "CompileVisualPanel2.tbScriptName.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel5, org.openide.util.NbBundle.getMessage(CompileVisualPanel2.class, "CompileVisualPanel2.jLabel5.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(radioSpaces)
                        .addGap(18, 18, 18)
                        .addComponent(radioTabs))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(chkbxComments))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tbScriptName, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5)
                        .addGap(0, 144, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(31, 31, 31)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(radioSpaces)
                    .addComponent(radioTabs))
                .addGap(18, 18, 18)
                .addComponent(chkbxComments)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(tbScriptName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void chkbxCommentsItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chkbxCommentsItemStateChanged
        switch(evt.getStateChange()) {
            case ItemEvent.DESELECTED:
                txtAreaHeader.setEditable(false);
                txtAreaHeader.setEnabled(false);
                break;
            case ItemEvent.SELECTED:
                txtAreaHeader.setEditable(true);
                txtAreaHeader.setEnabled(true);
                break;
        }
    }//GEN-LAST:event_chkbxCommentsItemStateChanged

    public JRadioButton getRadioSpaces() {
        return radioSpaces;
    }

    public JRadioButton getRadioTabs() {
        return radioTabs;
    }

    public JCheckBox getChkbxComments() {
        return chkbxComments;
    }

    public ButtonGroup getRadioGroupIndentation() {
        return radialIndentation;
    }

    public JTextArea getTxtAreaHeader() {
        return txtAreaHeader;
    }

    public JTextField getTbScriptName() {
        return tbScriptName;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox chkbxComments;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.ButtonGroup radialIndentation;
    private javax.swing.JRadioButton radioSpaces;
    private javax.swing.JRadioButton radioTabs;
    private javax.swing.JTextField tbScriptName;
    private javax.swing.JTextArea txtAreaHeader;
    // End of variables declaration//GEN-END:variables
}
