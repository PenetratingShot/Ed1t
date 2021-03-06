package com.shreyaslad.ed1t.Commands;

import com.shreyaslad.ed1t.Components.PlaceholderTextField;
import com.shreyaslad.ed1t.Data.FileHandler;
import com.shreyaslad.ed1t.Data.RemoteList;
import mdlaf.MaterialLookAndFeel;
import mdlaf.utils.MaterialColors;
import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class Events {

    private static boolean isVisible;
    private static boolean isPushWindowVisible;
    private static JFrame pushWindowFrame = new JFrame();

    // JFrame for committing things
    public static void createCommitFrame() {
        EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(new MaterialLookAndFeel());
            } catch (UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }

            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setTitle("Committing Changes");
            frame.setVisible(true);
            frame.setMinimumSize(new Dimension(600, 160));
            frame.setResizable(false);

            JPanel panel = new JPanel();
            panel.setLayout(null);
            panel.setOpaque(true);
            panel.setBackground(new Color(41, 41, 41));

            /*JLabel titleLabel = new JLabel("Committing Changes");
            titleLabel.setBounds(220, 0, 550, 40);
            titleLabel.setFont(new Font("Sans Serif", Font.BOLD, 18));*/

            PlaceholderTextField commitMessageField = new PlaceholderTextField("");
            commitMessageField.setPlaceholder("Commit Message");
            commitMessageField.setBounds(new Rectangle(10, 10, 570, 55));

            JButton commitButton = new JButton("Commit");
            commitButton.setOpaque(true);
            commitButton.setBackground(MaterialColors.GRAY_600);
            commitButton.setForeground(Color.WHITE);
            commitButton.setBounds(475, 90, 100, 30);
            commitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (commitButton.getText().isEmpty()) {
                        // ignore
                    } else {
                        try {
                            Git git = Git.open(new File(FileHandler.getDirPath()));

                            AddCommand add = git.add();
                            add.addFilepattern(".").call();

                            CommitCommand commit = git.commit();
                            commit.setMessage(commitMessageField.getText()).call();

                            frame.dispose();
                        } catch (IOException | GitAPIException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            });
            panel.add(commitMessageField);
            panel.add(commitButton);

            frame.add(panel);
            frame.pack();
        });
    }

    private static JFrame firstWindow = new JFrame();
    public static void createFirstStartWindow() {

        EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(new MaterialLookAndFeel());
            } catch (UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }


            firstWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            firstWindow.setMinimumSize(new Dimension(600, 200));
            firstWindow.setResizable(false);
            firstWindow.setTitle("Ed1t");

            JPanel panel = new JPanel();
            panel.setOpaque(true);
            panel.setLayout(null);

            JLabel title = new JLabel("Hello There");
            title.setFont(new Font("Sans Serif", Font.BOLD, 18));
            title.setBounds(0, 0, 600, 30);
            title.setHorizontalAlignment(JLabel.CENTER);
            title.setHorizontalAlignment(JLabel.CENTER);

            JLabel welcomeLabel = new JLabel("Clone a repository or Open one to get started");
            welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
            welcomeLabel.setVerticalAlignment(JLabel.CENTER);
            welcomeLabel.setFont(new Font("Sans Serif", Font.PLAIN, 12));
            welcomeLabel.setBounds(5, 30, 600, 30);

            JButton cloneRepo = new JButton("Clone from Remote");
            cloneRepo.setOpaque(true);
            cloneRepo.setBackground(MaterialColors.GRAY_600);
            cloneRepo.setForeground(Color.WHITE);
            cloneRepo.setBounds(15, 80, 165, 30);
            cloneRepo.addActionListener(e -> Repo.cloneRemote());

            JButton openRepo = new JButton("Open Local");
            openRepo.setBackground(MaterialColors.GRAY_600);
            openRepo.setOpaque(true);
            openRepo.setForeground(Color.WHITE);
            openRepo.setBounds(200, 80, 165, 30);
            openRepo.addActionListener(e -> Repo.openLocal());

            JButton createRepo = new JButton("Create");
            createRepo.setBackground(MaterialColors.GRAY_600);
            createRepo.setOpaque(true);
            createRepo.setForeground(Color.WHITE);
            createRepo.setBounds(390, 80, 165, 30);
            createRepo.addActionListener(e -> Repo.createLocal());

            //panel.setBackground(new Color(70, 70, 70));
            /*Card card = new Card("hi");
            card.setBounds(new Rectangle(5, 60, 50, 30));
            card.setBackground(new Color(70, 70, 70));*/

            panel.add(title);
            panel.add(welcomeLabel);
            panel.add(cloneRepo);
            panel.add(openRepo);
            panel.add(createRepo);

            title.setForeground(new Color(228, 228, 228));
            welcomeLabel.setForeground(new Color(228, 228, 228));
            panel.setBackground(new Color(41, 41, 41));

            firstWindow.setVisible(true);

            firstWindow.add(panel);
            firstWindow.pack();
        });
    }

    public static void createPushWindow(Object[] branches) {
        try {
            UIManager.setLookAndFeel(new MaterialLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        pushWindowFrame.setTitle("Push to Remote");
        pushWindowFrame.setVisible(true);
        pushWindowFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new CardLayout());

        JPanel card1 = new JPanel();
        JComboBox box = new JComboBox(branches);
        card1.add(box);

        JPanel card2 = new JPanel();
        PlaceholderTextField field = new PlaceholderTextField("URL");
        card2.add(field);

        JPanel card3 = new JPanel();
        JButton ok = new JButton("Ok");
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (field.getText().isEmpty()) {
                    // ignore
                }
                String selectedItem = box.getSelectedItem().toString();

            }
        });

        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closePushWindow();
            }
        });
        card3.add(ok);
        card3.add(cancel);

        panel.add(card1);
        panel.add(card2);
        panel.add(card3);

        pushWindowFrame.add(panel);
        pushWindowFrame.pack();
    }

    public static void setFirstStartWindowClose(boolean b) {
        if (b) {
            firstWindow.setVisible(false);
            firstWindow.dispose();
        } else {
            //nothing
        }
    }

    public static void closePushWindow() {
        pushWindowFrame.dispose();
    }

    /*

    This is not in the pseudocode. It originally was but then I completely scrapped the idea

    public static void createConfigWindow() {
        EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(new MaterialLookAndFeel());
            } catch (UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }

            JFrame configFrame = new JFrame();
            configFrame.setTitle("Config");
            configFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            configFrame.setMinimumSize(new Dimension(600, 400));
            configFrame.setResizable(false);
            configFrame.setVisible(true);

            JPanel panel = new JPanel();
            GroupLayout layout = new GroupLayout(configFrame.getContentPane());
            panel.setLayout(layout);
            layout.setAutoCreateGaps(true);

            JLabel remoteLabel = new JLabel("Default Remote");
            JComboBox comboBox = new JComboBox();
            for (int i = 0; i < RemoteList.size(); i++) {
                comboBox.addItem(RemoteList.get(i));
            }
            JTextField remoteURL = new JTextField(RemoteList.get(comboBox.getSelectedIndex()));
            JButton updateButton = new JButton("Update");
            updateButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    RemoteList.addRemotes((String) comboBox.getSelectedItem(), remoteURL.getText());
                }
            });


            configFrame.add(panel);
            configFrame.pack();
        });
    }*/
}
