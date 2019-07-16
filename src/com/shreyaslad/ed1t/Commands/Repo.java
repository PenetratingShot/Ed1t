package com.shreyaslad.ed1t.Commands;

import com.shreyaslad.ed1t.Components.PlaceholderTextField;
import com.shreyaslad.ed1t.Data.FileHandler;
import com.shreyaslad.ed1t.Ed1t;
import mdlaf.MaterialLookAndFeel;
import mdlaf.animation.MaterialUIMovement;
import mdlaf.utils.MaterialColors;
import org.apache.commons.validator.routines.UrlValidator;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class Repo {

    @SuppressWarnings("Duplicates")
    public static void cloneRemote() {
        EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(new MaterialLookAndFeel());
            } catch (UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }

            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setTitle("Clone Remote Repository");
            frame.setMinimumSize(new Dimension(600, 180));
            frame.setResizable(false);
            frame.setVisible(true);

            JPanel panel = new JPanel();
            panel.setOpaque(true);
            panel.setLayout(null);
            panel.setBackground(new Color(196, 196, 196));

            PlaceholderTextField url = new PlaceholderTextField();
            url.setPlaceholder("URL");
            url.setBounds(5, 30, 580, 50);

            JButton dirChoose = new JButton("Target Location");
            dirChoose.setBackground(MaterialColors.GRAY_600);
            dirChoose.setOpaque(true);
            dirChoose.setForeground(Color.WHITE);
            dirChoose.setBounds(5, 100, 200, 30);
            dirChoose.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFileChooser fileChooser = new JFileChooser();

                    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                    int option = fileChooser.showOpenDialog(null);
                    if (option == JFileChooser.APPROVE_OPTION) {
                        File file = fileChooser.getSelectedFile();
                        try {
                            FileHandler.setDirPath(file.getCanonicalPath());
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        System.out.println(FileHandler.getDirPath());
                        // TODO: uncomment this: FileHandler.serialize();
                    } else {
                        System.out.println("Event canceled");
                    }
                }
            });

            JButton cloneButton = new JButton("Clone");
            cloneButton.setBackground(MaterialColors.GRAY_600);
            cloneButton.setOpaque(true);
            cloneButton.setForeground(Color.WHITE);
            cloneButton.setBounds(420, 100, 160, 30);
            cloneButton.addActionListener(e -> {
                UrlValidator urlValidator = new UrlValidator();
                if (urlValidator.isValid(url.getText())) { // There is nothing to verify that the url entered is actually a git repository :(
                    // cool, verify directory has been chosen
                    if (FileHandler.getDirPath().isEmpty()) {
                        //nothing has been chosen, do nothing until they choose the directory
                    } else {
                        System.out.println("Ignore this error since it's not actually an error. I'm just on the latest alpha of SLF4J. Everything works :)");
                        File file = new File(FileHandler.getDirPath());
                        try {
                            Git.cloneRepository().setURI(url.getText())
                                    .setCloneAllBranches(true)
                                    .setDirectory(file)
                                    .call();
                            System.out.println(FileHandler.getDirPath());
//                            String[] y = {FileHandler.getDirPath()};
//                            System.out.println(y[0]);
//                            Ed1t.main(y);
//                            frame.setVisible(false);
//                            Ed1t.toggleMain(true);
                            try {
                                new Ed1t(FileHandler.getDirPath());
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            } catch (GitAPIException ex) {
                                ex.printStackTrace();
                            }
                            OS.setLastLogin(FileHandler.getDirPath());
                            // TODO: close this frame and call the main opener function to view the contents of the file
                        } catch (GitAPIException ex) {
                            ex.printStackTrace();
                        }
                    }
                } else {
                    //ignore
                }
            });

            MaterialUIMovement.add(dirChoose, MaterialColors.GRAY_500);

            //panel.add(title);
            panel.add(url);
            panel.add(dirChoose);
            panel.add(cloneButton);

            frame.add(panel);
            frame.pack();
        });
    }

    @SuppressWarnings("Duplicates")
    public static void openLocal() {
        EventQueue.invokeLater(() -> {
            JFrame frame = new JFrame();
            frame.setMinimumSize(new Dimension(395, 180));
            frame.setResizable(false);
            frame.setTitle("Open Local Repository");
            frame.setVisible(true);

            JPanel panel = new JPanel();
            panel.setVisible(true);
            panel.setLayout(null);
            panel.setBackground(new Color(196, 196, 196));

            JButton dirChoose = new JButton("Choose");
            dirChoose.setBackground(MaterialColors.GRAY_600);
            dirChoose.setOpaque(true);
            dirChoose.setForeground(Color.WHITE);
            dirChoose.setBounds(5, 60, 200, 30);
            dirChoose.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFileChooser fileChooser = new JFileChooser();

                    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                    int option = fileChooser.showOpenDialog(frame);
                    if (option == JFileChooser.APPROVE_OPTION) {
                        File file = fileChooser.getSelectedFile();
                        try {
                            FileHandler.setDirPath(file.getCanonicalPath());
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        System.out.println(FileHandler.getDirPath());
                        // TODO: uncomment this: FileHandler.serialize();
                    } else {
                        System.out.println("Event canceled");
                    }
                }
            });

            JButton openButton = new JButton("Open");
            openButton.setBackground(MaterialColors.GRAY_600);
            openButton.setOpaque(true);
            openButton.setForeground(Color.WHITE);
            openButton.setBounds(230, 60, 160, 30);
            openButton.addActionListener(e -> {
//                    Ed1t.toggleMain(true);
//                    String[] y = {FileHandler.getDirPath()};
//                    Ed1t.main(y);
                try {
                    new Ed1t(FileHandler.getDirPath());
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (GitAPIException ex) {
                    ex.printStackTrace();
                }
                frame.setVisible(false);
                OS.setLastLogin(FileHandler.getDirPath());
            });

            panel.add(dirChoose);
            panel.add(openButton);

            frame.add(panel);
            frame.pack();
        });
    }

    @SuppressWarnings("Duplicates")
    public static void createLocal() {
        EventQueue.invokeLater(() -> {
            JFrame frame = new JFrame();
            frame.setMinimumSize(new Dimension(600, 180));
            frame.setResizable(false);
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setTitle("Create Local Repository");

            JPanel panel = new JPanel();
            panel.setOpaque(true);
            panel.setBackground(new Color(196, 196, 196));
            panel.setLayout(null);

            /*JLabel title = new JLabel("Create Local Repository");
            title.setFont(new Font("Sans Serif", Font.BOLD, 18));
            title.setBounds(0, 0, 600, 30);
            title.setHorizontalAlignment(JLabel.CENTER);
            title.setHorizontalAlignment(JLabel.CENTER);*/

            PlaceholderTextField name = new PlaceholderTextField();
            name.setPlaceholder("Name");
            name.setBounds(5, 20, 580, 50);

            JButton dirChoose = new JButton("Parent Dir");
            dirChoose.setBackground(MaterialColors.GRAY_600);
            dirChoose.setOpaque(true);
            dirChoose.setForeground(Color.WHITE);
            dirChoose.setBounds(5, 100, 200, 30);
            dirChoose.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFileChooser fileChooser = new JFileChooser();

                    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                    int option = fileChooser.showOpenDialog(frame);
                    if (option == JFileChooser.APPROVE_OPTION) {
                        File file = fileChooser.getSelectedFile();
                        try {
                            FileHandler.setDirPath(file.getCanonicalPath());
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        System.out.println(FileHandler.getDirPath());
                        // TODO: uncomment this: FileHandler.serialize();
                    } else {
                        System.out.println("Event canceled");
                    }
                }
            });

            JButton createButton = new JButton("Create");
            createButton.setBackground(MaterialColors.GRAY_600);
            createButton.setOpaque(true);
            createButton.setForeground(Color.WHITE);
            createButton.setBounds(420, 100, 160, 30);
            createButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    FileHandler.setDirPath(new File(FileHandler.getDirPath() + "/" + name.getText()).toString());
                    System.out.println(FileHandler.getDirPath());
                    new File(FileHandler.getDirPath()).mkdir();
                    try {
                        File file = new File(FileHandler.getDirPath());
                        //FileRepositoryBuilder.create(new File(FileHandler.getDirPath() + "/.git"));
                        Git.init().setDirectory(file).call();
                        /*String[] y = {FileHandler.getDirPath()};
                        Ed1t.main(y); //open the thing
                        frame.setVisible(false);
                        Ed1t.toggleMain(true);*/
                        try {
                            new Ed1t(FileHandler.getDirPath());
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        } catch (GitAPIException ex) {
                            ex.printStackTrace();
                        }
                        OS.setLastLogin(FileHandler.getDirPath());
                    } catch (GitAPIException ex) {
                        ex.printStackTrace();
                    }
                    frame.setVisible(false);
                }
            });

            //panel.add(title);
            panel.add(dirChoose);
            panel.add(name);
            panel.add(createButton);

            frame.add(panel);
            frame.pack();
        });
    }
}
