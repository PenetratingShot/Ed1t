/*
 * Ed1t.java
 * Copyright Shreyas Lad (PenetratingShot) 2019
 *
 * Main file for creating the main frame
 * Honestly I don't even know if the first start thing will even work. (Shreyas from Future: It doesn't)
 *
 * Good luck reading this. I forgot what half the code does already
 */


/*
 * Things changed from pseudocode:
 *
 * - First Start didn't quite work
 *  - The file was being read and all, but JGit wasn't recognizing the directory as a valid git repository
 *  - I couldn't find a way to get around this unless I fundamentally reworked all of the code, which I obviously did not have time to do
 *  - So for now, the OS detection code will still be there, it just wont be implemented
 * - Tree Refresh
 *  - Instead of having to refresh the entire panel, nodes are dynamically added, renamed removed when a file is operated upon
 *  - Then it refreshes only the parent node of the child node that was modified, but not all of the child nodes inside of the child node inside of the parent node
 */

package com.shreyaslad.ed1t;

import com.shreyaslad.ed1t.Commands.Events;
import com.shreyaslad.ed1t.Components.LineNumberingTextArea;
import com.shreyaslad.ed1t.Components.PlaceholderTextField;
import com.shreyaslad.ed1t.Components.Tree.CreateChildNodes;
import com.shreyaslad.ed1t.Components.Tree.FileNode;
import com.shreyaslad.ed1t.Data.FileContents;
import com.shreyaslad.ed1t.Data.FileHandler;
import com.shreyaslad.ed1t.Data.RemoteList;
import com.shreyaslad.ed1t.Data.SelectedFile;

import mdlaf.MaterialLookAndFeel;
import mdlaf.animation.MaterialUIMovement;
import mdlaf.utils.MaterialColors;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.StoredConfig;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.ColorUIResource;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;

public class Ed1t {
    private static DefaultMutableTreeNode root;
    private static DefaultTreeModel treeModel;
    private static JTree tree;
    private static File fileRoot;
    private static JScrollPane scrollPane;
    private static JScrollPane ideScrollPane;

    private static JFrame frame = new JFrame();

    private static JPanel mainWindow = new JPanel(new GridBagLayout());
    private static GridBagConstraints c = new GridBagConstraints();

    private static JButton refreshButton = new JButton("Refresh");
    private static JButton openButton = new JButton("Open");
    private static JButton saveButton = new JButton("Save");
    private static JButton commitButton = new JButton("Commit");
    private static JButton pullButton = new JButton("Pull");

    Git git = Git.open(new File(FileHandler.getDirPath())); // this saved my life
    Repository repository = git.getRepository();

    public Ed1t(String dir) throws IOException, GitAPIException {

        fileRoot = new File(FileHandler.getDirPath());
        root = new DefaultMutableTreeNode(new FileNode(fileRoot));

        CreateChildNodes ccn = new CreateChildNodes(fileRoot, root);
        new Thread(ccn).start();

        try {
            UIManager.setLookAndFeel(new MaterialLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setMinimumSize(new Dimension(821, 516)); //minimum size possible to prevent everything from breaking :)
        frame.setVisible(true);
        frame.setTitle("Ed1t");

        List<Ref> call = git.branchList().call(); //shouldn't need this but doesn't hut to have it :/

        JPanel upPanel = new JPanel();

        openButton.setOpaque(true);
        openButton.setBackground(MaterialColors.GRAY_700);
        openButton.setForeground(Color.WHITE);
        openButton.addActionListener(e -> Events.createFirstStartWindow());
        c.gridx = 1;
        //c.weightx = 0.5;
        //c.weighty = 0.0;
        c.gridy = 0;
        upPanel.add(openButton);

        commitButton.setOpaque(true);
        commitButton.setBackground(MaterialColors.GRAY_600);
        commitButton.setForeground(Color.WHITE);
        commitButton.addActionListener(e -> Events.createCommitFrame());

        c.gridx = 3;
        c.gridy = 0;
        //c.weightx = 0.5;
        upPanel.add(commitButton);

        /*pushButton.setOpaque(true);
        pushButton.setBackground(MaterialColors.GRAY_600);
        pushButton.setForeground(Color.WHITE);
        pushButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object[] branches = new Object[call.size()];

                for (int i = 0; i < call.size(); i++) {
                    branches[i] = call.get(i).getName();
                }

                Events.createPushWindow(branches);

            }
        });
        c.gridx = 5;
        c.gridy = 0;
        //c.weightx = 0.5;
        upPanel.add(pushButton);*/

        //moved fileRoot up
        treeModel = new DefaultTreeModel(root);

        tree = new JTree(treeModel);
        tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    TreePath path = tree.getPathForLocation(e.getX(), e.getY());

                    if (path != null) {
                        String pathString = path.toString();
                        pathString = pathString.substring(1, pathString.length() - 1);
                        String[] formatted = pathString.split(", ");
                        formatted[0] = "";
                        pathString = String.join("/", formatted);
                        String finalString = FileHandler.getDirPath() + pathString;

                        SelectedFile.setSelectedFilePath(finalString);
                        System.out.println(finalString);


                    } else {
                        //ignore
                    }

                    Rectangle pathBounds = tree.getUI().getPathBounds(tree, path);
                    if (pathBounds != null && pathBounds.contains(e.getX(), e.getY())) {
                        JPopupMenu menu = new JPopupMenu();
                        JMenuItem itemRename = new JMenuItem("Rename");
                        JMenuItem itemDelete = new JMenuItem("Delete");
                        JMenuItem itemAdd = new JMenuItem("Add");

                        itemRename.addActionListener(e1 -> EventQueue.invokeLater(() -> { //super cool lambda :)
                            try {
                                UIManager.setLookAndFeel(new MaterialLookAndFeel());
                            } catch (UnsupportedLookAndFeelException e11) {
                                e11.printStackTrace();
                            }

                            System.out.println("[DEBUG] Rename file: " + SelectedFile.getSelectedFilePath());
                            Path source = Paths.get(SelectedFile.getSelectedFilePath());

                            JFrame renameFrame = new JFrame();
                            renameFrame.setVisible(true);
                            renameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            renameFrame.setTitle("Rename");
                            renameFrame.setMinimumSize(new Dimension(600, 280));
                            renameFrame.setResizable(false);

                            JPanel renamePanel = new JPanel();
                            renamePanel.setLayout(null);
                            renamePanel.setBackground(new Color(196, 196, 196));
                            renamePanel.setOpaque(true);

                            PlaceholderTextField renameField = new PlaceholderTextField();
                            renameField.setPlaceholder("New Name");
                            renameField.setBounds(10, 70, 570, 55);

                            JButton renameButton = new JButton("Rename");
                            renameButton.setBounds(475, 150, 100, 30);
                            renameButton.setOpaque(true);
                            renameButton.setBackground(MaterialColors.GRAY_600);
                            renameButton.setForeground(Color.WHITE);
                            renameButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e11) {
                                    File origFile = new File(SelectedFile.getSelectedFilePath());

                                    String[] array = SelectedFile.getSelectedFilePath().split("/");
                                    array[array.length - 1] = "";
                                    array[array.length - 1] = renameField.getText();

                                    String path1 = String.join("/", array);
                                    SelectedFile.setSelectedFilePath(path1);

                                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                                    node.setUserObject(renameField.getText());

                                    boolean didRename = origFile.renameTo(new File(path1));

                                    /*StringBuilder stringBuilder = new StringBuilder();
                                    try (Stream<String> stream = Files.lines(Paths.get(SelectedFile.getSelectedFilePath()), StandardCharsets.UTF_8)) {
                                        stream.forEach(s -> stringBuilder.append(s).append("\n"));
                                    } catch (IOException ex) {
                                        ex.printStackTrace();
                                    }

                                    try {
                                        FileWriter fileWriter = new FileWriter(newFile);
                                        fileWriter.write(stringBuilder.toString());
                                        fileWriter.close();

                                        boolean didDelete = origFile.delete(); //Die

                                        if (didDelete) {
                                            System.out.println("Successfully deleted original file");
                                        } else {
                                            System.out.println("Could not delete original file");
                                        }
                                    } catch (IOException ex) {
                                        ex.printStackTrace();
                                    }*/

                                    renameFrame.setVisible(false);
                                }
                            });

                            renamePanel.add(renameField);
                            renamePanel.add(renameButton);

                            renameFrame.add(renamePanel);
                            renameFrame.pack();
                        }));

                        itemDelete.addActionListener(e112 -> {
                            File file = new File(SelectedFile.getSelectedFilePath());

                            if (file.delete()) {
                                System.out.println("Deleted file");
                            } else {
                                System.out.println("File wasn't deleted");
                            }

                            DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) path.getLastPathComponent();
                            treeModel.removeNodeFromParent(treeNode);
                        });
                        itemDelete.addActionListener(e12 -> System.out.println("[DEBUG] Delete file: " + path));

                        itemAdd.addActionListener(e12 -> {
                            String s = (String) JOptionPane.showInputDialog(frame, "File name (including folders)", "Add Items", JOptionPane.PLAIN_MESSAGE, null, null, "file.txt");
                            if ((s != null) && (s.length() > 0)) {
                                String[] array = SelectedFile.getSelectedFilePath().split("/");
                                array[array.length - 1] = s;

                                //new File(String.join("/", array));
                                File file = new File(String.join("/", array));
                                try {
                                    if (file.createNewFile()) {
                                        System.out.println("Created file");
                                        root.add(new DefaultMutableTreeNode(new FileNode(file)));
                                        treeModel.reload(root);
                                    } else {
                                        System.out.println("Could not create file");
                                    }
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                                System.out.println(String.join("/", array));
                            }
                        });

                        menu.add(itemRename);
                        menu.add(itemDelete);
                        menu.add(itemAdd);
                        menu.show(tree, pathBounds.x, pathBounds.y + pathBounds.height);
                    }
                }
            }
        });

        tree.setShowsRootHandles(true);
        scrollPane = new JScrollPane(tree);
        tree.setBackground(new Color(41, 41, 41));
        tree.setBorder(null);
        tree.setForeground(new Color(228, 228, 228));
        DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) tree.getCellRenderer();
        //renderer.setBackground(new Color(41, 41, 41));
        //renderer.setForeground(new Color(228, 228, 228));
        renderer.setBackgroundNonSelectionColor(new Color(41, 41, 41));
        renderer.setTextNonSelectionColor(new Color(228, 228, 228));
        renderer.setBackgroundSelectionColor(new Color(61, 61, 61));
        renderer.setTextSelectionColor(new Color(228, 228, 228));
        renderer.setBorderSelectionColor(new Color(61,61,61));

        //scrollPane.setPreferredSize(new Dimension(300, 400));
        scrollPane.setPreferredSize(new Dimension(320, 450));
        c.gridx = 0;
        c.gridy = 1;
        /*c.weightx = 0.0;
        c.weighty = 1;*/
        c.fill = GridBagConstraints.VERTICAL;
        c.weighty = 1;

        mainWindow.add(scrollPane, c);
        mainWindow.setBackground(MaterialColors.GRAY_600);
        upPanel.setBackground(MaterialColors.GRAY_600);

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        c.weighty = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        mainWindow.add(upPanel, c);
        JEditorPane area = new JEditorPane();
        area.setFont(new Font("Sans Serif", Font.PLAIN, 12));
        area.setBackground(Color.DARK_GRAY);
        area.setForeground(new Color(239, 239, 239));
        area.setCaretColor(Color.WHITE);
        area.setBorder(null);
        area.setEditable(false);
        LineNumberingTextArea lineNumberingTextArea = new LineNumberingTextArea(area);
        // area.setPreferredSize(new Dimension(300, 300));

        area.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                lineNumberingTextArea.updateLineNumbers();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                lineNumberingTextArea.updateLineNumbers();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                lineNumberingTextArea.updateLineNumbers();
            }
        });

        ideScrollPane = new JScrollPane(area);
        ideScrollPane.setRowHeaderView(lineNumberingTextArea);
        ideScrollPane.setPreferredSize(new Dimension(500, 450));
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 0.65;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        mainWindow.add(ideScrollPane, c);

        JPanel labelPanel = new JPanel();
        labelPanel.setBackground(Color.BLUE);
        labelPanel.setOpaque(true);
        labelPanel.setVisible(true);
        frame.add(labelPanel);

        JLabel fileLabel = new JLabel();
        c.gridx = 3;
        c.gridy = 0;
        labelPanel.add(fileLabel);

        tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                TreePath tp = tree.getPathForLocation(e.getX(), e.getY());
                if (tp != null) {

                    area.setEditable(true);

                    String tpString = tp.toString();
                    fileLabel.setText(tpString);
                    String format = tpString.substring(1, tpString.length() - 1);
                    String[] formatted = format.split(", ");
                    formatted[0] = "";
                    String array = String.join("/", formatted);
                    String _final = FileHandler.getDirPath() + array;
                    System.out.println(_final);

                    SelectedFile.setSelectedFilePath(_final);
                    //area.setText(SelectedFile.getFileContents());
                    try {
                        area.setText("Editing: " + array + "   Branch: " + repository.getBranch() + "\n\n" + SelectedFile.getFileContents());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    area.setText("");
                }
            }
        });

        saveButton.setOpaque(true);
        saveButton.setBackground(MaterialColors.GRAY_600);
        saveButton.setForeground(Color.WHITE);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (SelectedFile.getSelectedFilePath() == null) {
                    //no
                } else {
                    FileContents.setFileContents(area.getText());
                    try {
                        FileWriter fileWriter = new FileWriter(SelectedFile.getSelectedFilePath());

                        /*int end = area.getLineEndOffset(0);
                        area.replaceRange("", 0, end);*/

                        Element root = area.getDocument().getDefaultRootElement();
                        Element firstLine = root.getElement(0);
                        Element secondLine = root.getElement(1);
                        area.getDocument().remove(firstLine.getStartOffset(), firstLine.getEndOffset());
                        area.getDocument().remove(secondLine.getStartOffset(), secondLine.getEndOffset());
                        fileWriter.write(area.getText());
                        fileWriter.close();
                        System.out.println("[DEBUG] Successfully wrote to file");
                    } catch (IOException | BadLocationException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        c.gridx = 2;
        c.gridy = 0;
        //c.weightx = 0.5;
        upPanel.add(saveButton);

        refreshButton.setOpaque(true);
        refreshButton.setBackground(MaterialColors.GRAY_600);
        refreshButton.setForeground(Color.WHITE);
        refreshButton.addActionListener(e -> {
            new Thread(ccn).start();
        });
        c.gridx = 0;
        //c.weightx = 0.5;
        //c.weighty = 0.0;
        c.gridy = 0;
        upPanel.add(refreshButton);

        pullButton.setOpaque(true);
        pullButton.setBackground(MaterialColors.GRAY_600);
        pullButton.setForeground(Color.WHITE);
        pullButton.addActionListener(e -> {
            String string;

            if (RemoteList.getSelectedRemote() == null) {
                string = null;
            } else {
                string = RemoteList.getSelectedRemote();
            }

            String s = (String) JOptionPane.showInputDialog(frame, "Remote URL", "Pull From Remote", JOptionPane.PLAIN_MESSAGE, null, null, string);

            if ((s != null) && s.length() > 0) {
                StoredConfig config = git.getRepository().getConfig();
                config.setString("remote", "origin", "url", s);
                try {
                    config.save();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                RemoteList.setSelectedRemote(s);
                try {
                    PullResult result = git.pull()
                            .setRemote("origin")
                            .setRemoteBranchName("master")
                            .call();
                    if (result.isSuccessful()) {
                        System.out.println("Pull successful");
                        root.removeAllChildren(); // remove all of the children if the pull went through
                        treeModel.reload();
                        new Thread(new CreateChildNodes(fileRoot, root)).start(); //recreate the child nodes just in case there have been any changes
                    } else {
                        System.out.println("Pull was not successful");
                    }
                } catch (GitAPIException ex) {
                    ex.printStackTrace();
                }
            } else {

            }
        });
        c.gridx = 6;
        c.gridy = 0;
        upPanel.add(pullButton, c);

        /*branchBox = new JComboBox();
        for (Ref ref : call) {
            branchBox.addItem(ref.getName());
        }
        branchBox.addItem("Edit Branches");
        branchBox.setSelectedIndex(0);

        c.gridx = 7;
        c.gridy = 0;
        upPanel.add(branchBox, c);*/


        Events.setFirstStartWindowClose(false);
        Events.setFirstStartWindowClose(false);
        Events.setFirstStartWindowClose(false);

        MaterialUIMovement.add(refreshButton, MaterialColors.GRAY_500);
        MaterialUIMovement.add(openButton, MaterialColors.GRAY_500);
        MaterialUIMovement.add(saveButton, MaterialColors.GRAY_500);
        MaterialUIMovement.add(commitButton, MaterialColors.GRAY_500);
        MaterialUIMovement.add(pullButton, MaterialColors.GRAY_500);

        frame.add(mainWindow);
        frame.pack();
    }

    public static void main(String[] args) {
        //OS.init(); debug mode on for now (and probably forever)
        Events.createFirstStartWindow();

        /*try {
            new Ed1t(args[0]);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (GitAPIException ex) {
            ex.printStackTrace();
        }*/

        // Moved this process to Ed1t
    }

    public static void toggleMain(boolean b) {
        frame.setVisible(b);
    }

    public static void show() {
        frame.show(true);
    }
}