package Tetris.gui;

import Tetris.settings.ControlScheme;
import Tetris.settings.HandlingPreset;
import Tetris.settings.ReceiveSettings;
import Tetris.settings.SettingKey;
import Tetris.settings.Settings;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class QuickSettings implements ReceiveSettings {
    private Component parent;

    private JFrame f = new JFrame("Quick Settings");
    private BlockSkinManager bsm;

    private Box.Filler leftFiller;
    private Box.Filler rightFiller;

    private JPanel p1Panel = new JPanel();
    private JLabel p1Label = new JLabel();
    private JLabel p1csLabel = new JLabel();
    private JLabel p1haLabel = new JLabel();
    private JRadioButton p1cs1RadioButton = new JRadioButton();
    private JRadioButton p1cs2RadioButton = new JRadioButton();
    private JRadioButton p1cs3RadioButton = new JRadioButton();
    private JRadioButton p1ha1RadioButton = new JRadioButton();
    private JRadioButton p1ha2RadioButton = new JRadioButton();
    private JCheckBox p1sdCheckBox = new JCheckBox();
    private PlayerNameField p1NameField = new PlayerNameField();

    private JPanel p2Panel = new JPanel();
    private JLabel p2Label = new JLabel();
    private JLabel p2csLabel = new JLabel();
    private JLabel p2haLabel = new JLabel();
    private JRadioButton p2cs1RadioButton = new JRadioButton();
    private JRadioButton p2cs2RadioButton = new JRadioButton();
    private JRadioButton p2cs3RadioButton = new JRadioButton();
    private JRadioButton p2ha1RadioButton = new JRadioButton();
    private JRadioButton p2ha2RadioButton = new JRadioButton();
    private JCheckBox p2sdCheckBox = new JCheckBox();
    private PlayerNameField p2NameField = new PlayerNameField();

    private JPanel skinPanel = new JPanel();
    private SkinTestPanel skinTestPanel;
    private JButton skinPrevButton = new JButton();
    private JButton skinNextButton = new JButton();
    private JComboBox<String> skinComboBox = new JComboBox<>();

    private JButton newGameButton = new JButton();

    private long lastFrameTime = System.nanoTime();
    private long frameTimeAccumulator = 0;

    private double windowDeltaX;
    private double windowDeltaY;
    private double windowLastDeltaX;
    private double windowLastDeltaY;

    public QuickSettings(Component parent, BlockSkinManager bsm) {
        this.parent = parent;
        this.bsm = bsm;

        f.setSize(800, 180);
        f.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        detailComponents();

        moveFrame(true);

        f.setAutoRequestFocus(false);
    }

    public void moveFrame(boolean covering) {
        var parentLocation = parent.getLocationOnScreen();
        var parentSize = parent.getSize();
        int yOffset = (int) (parentSize.height * (covering ? 0.5 : 1)) + (covering ? 0 : -20);

        if (f.isVisible()) {
            var selfLocation = f.getLocationOnScreen();
            windowDeltaX -= parentLocation.x - selfLocation.x;
            windowLastDeltaX -= parentLocation.x - selfLocation.x;
            windowDeltaY -= parentLocation.y + yOffset - selfLocation.y;
            windowLastDeltaY -= parentLocation.y + yOffset - selfLocation.y;
        } else {
            f.setLocation(parentLocation.x, parentLocation.y + yOffset);
        }
    }

    private void detailComponents() {
        detailP1Panel();
        detailP2Panel();
        detailSkinChangePanel();

        leftFiller = new Box.Filler(
                new Dimension(0, 0),
                new Dimension(0, 0),
                new Dimension(32767, 0));
        rightFiller = new Box.Filler(
                new Dimension(0, 0),
                new Dimension(0, 0),
                new Dimension(32767, 0));

        Box.Filler fillerA = new Box.Filler(
                new Dimension(0, 0),
                new Dimension(10, 0),
                new Dimension(10, 0));
        Box.Filler fillerB = new Box.Filler(
                new Dimension(0, 0),
                new Dimension(10, 0),
                new Dimension(10, 0));

        var contentPane = f.getContentPane();

        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
        contentPane.add(leftFiller);
        contentPane.add(p1Panel);
        contentPane.add(fillerA);
        contentPane.add(skinPanel);
        contentPane.add(fillerB);
        contentPane.add(p2Panel);
        contentPane.add(rightFiller);

    }

    private void detailP1Panel() {
        p1Label.setText("Player 1");
        p1csLabel.setText("Control Scheme");
        p1haLabel.setText("Handling");
        p1cs1RadioButton.setText("WASD");
        p1cs2RadioButton.setText("Classic");
        p1cs3RadioButton.setText("SlashBracket");
        p1ha1RadioButton.setText("Default");
        p1ha2RadioButton.setText("Fast");
        p1sdCheckBox.setText("Sonic Drop");

        p1NameField.setPlaceholder("P1...");

        p1cs1RadioButton.setSelected(true);
        p1ha1RadioButton.setSelected(true);

        ButtonGroup csButtonGroup = new ButtonGroup();
        csButtonGroup.add(p1cs1RadioButton);
        csButtonGroup.add(p1cs2RadioButton);
        csButtonGroup.add(p1cs3RadioButton);
        ButtonGroup hsButtonGroup = new ButtonGroup();
        hsButtonGroup.add(p1ha1RadioButton);
        hsButtonGroup.add(p1ha2RadioButton);

        GroupLayout layout = new GroupLayout(p1Panel);
        p1Panel.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING, false)
                .addComponent(p1Label)
                .addComponent(p1NameField)
                .addGroup(layout.createSequentialGroup()
                        .addGroup(
                                layout.createParallelGroup()
                                        .addComponent(p1csLabel)
                                        .addComponent(p1cs1RadioButton)
                                        .addComponent(p1cs2RadioButton)
                                        .addComponent(p1cs3RadioButton))
                        .addGroup(layout.createParallelGroup()
                                .addComponent(p1haLabel)
                                .addComponent(p1ha1RadioButton)
                                .addComponent(p1ha2RadioButton)
                                .addComponent(p1sdCheckBox))));
        layout.setVerticalGroup(
                layout.createParallelGroup(Alignment.LEADING, false).addGroup(
                        layout.createSequentialGroup()
                                .addComponent(p1Label)
                                .addComponent(p1NameField)
                                .addGroup(layout.createParallelGroup()
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(p1csLabel)
                                                .addComponent(p1cs1RadioButton)
                                                .addComponent(p1cs2RadioButton)
                                                .addComponent(p1cs3RadioButton))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(p1haLabel)
                                                .addComponent(p1ha1RadioButton)
                                                .addComponent(p1ha2RadioButton)
                                                .addComponent(p1sdCheckBox)))));
    }

    private void detailP2Panel() {
        p2Label.setText("Player 2");
        p2csLabel.setText("Control Scheme");
        p2haLabel.setText("Handling");
        p2cs1RadioButton.setText("WASD");
        p2cs2RadioButton.setText("Classic");
        p2cs3RadioButton.setText("SlashBracket");
        p2ha1RadioButton.setText("Default");
        p2ha2RadioButton.setText("Fast");
        p2sdCheckBox.setText("Sonic Drop");
        p2NameField.setPlaceholder("P2...");

        p2cs1RadioButton.setSelected(true);
        p2ha1RadioButton.setSelected(true);

        ButtonGroup csButtonGroup = new ButtonGroup();
        csButtonGroup.add(p2cs1RadioButton);
        csButtonGroup.add(p2cs2RadioButton);
        csButtonGroup.add(p2cs3RadioButton);
        ButtonGroup hsButtonGroup = new ButtonGroup();
        hsButtonGroup.add(p2ha1RadioButton);
        hsButtonGroup.add(p2ha2RadioButton);

        GroupLayout layout = new GroupLayout(p2Panel);
        p2Panel.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING, false)
                .addComponent(p2Label)
                .addComponent(p2NameField)
                .addGroup(layout.createSequentialGroup()
                        .addGroup(
                                layout.createParallelGroup()
                                        .addComponent(p2csLabel)
                                        .addComponent(p2cs1RadioButton)
                                        .addComponent(p2cs2RadioButton)
                                        .addComponent(p2cs3RadioButton))
                        .addGroup(layout.createParallelGroup()
                                .addComponent(p2haLabel)
                                .addComponent(p2ha1RadioButton)
                                .addComponent(p2ha2RadioButton)
                                .addComponent(p2sdCheckBox))));
        layout.setVerticalGroup(
                layout.createParallelGroup(Alignment.LEADING, false).addGroup(
                        layout.createSequentialGroup()
                                .addComponent(p2Label)
                                .addComponent(p2NameField)
                                .addGroup(layout.createParallelGroup()
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(p2csLabel)
                                                .addComponent(p2cs1RadioButton)
                                                .addComponent(p2cs2RadioButton)
                                                .addComponent(p2cs3RadioButton))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(p2haLabel)
                                                .addComponent(p2ha1RadioButton)
                                                .addComponent(p2ha2RadioButton)
                                                .addComponent(p2sdCheckBox)))));
    }

    private void detailSkinChangePanel() {
        skinTestPanel = new SkinTestPanel(bsm);
        skinPrevButton.setText("<");
        skinNextButton.setText(">");

        newGameButton.setText("New Game");

        GroupLayout layout = new GroupLayout(skinPanel);
        skinPanel.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(Alignment.CENTER, false)
                .addComponent(skinTestPanel.getPanel())
                .addGroup(layout.createSequentialGroup()
                        .addComponent(skinPrevButton)
                        .addComponent(skinComboBox)
                        .addComponent(skinNextButton))
                .addComponent(newGameButton));

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(skinTestPanel.getPanel())
                .addGroup(layout.createParallelGroup(Alignment.CENTER, false)
                        .addComponent(skinPrevButton)
                        .addComponent(skinComboBox)
                        .addComponent(skinNextButton))
                .addPreferredGap(ComponentPlacement.UNRELATED)
                .addComponent(newGameButton));

        skinNextButton.addActionListener(evt -> {
            int index = skinComboBox.getSelectedIndex();
            int length = skinComboBox.getItemCount();
            skinComboBox.setEnabled(false);
            skinComboBox.setSelectedIndex((index + 1) % length);
            skinComboBox.setEnabled(true);
        });

        skinPrevButton.addActionListener(evt -> {
            int index = skinComboBox.getSelectedIndex();
            int length = skinComboBox.getItemCount();
            skinComboBox.setEnabled(false);
            skinComboBox.setSelectedIndex((index + length - 1) % length);
            skinComboBox.setEnabled(true);
        });

        detailSkinChoices();
    }

    private void detailSkinChoices() {
        String[] blockSkinFolders = BlockSkinManager.getBlockSkinFolders();
        for (String v : blockSkinFolders) {
            skinComboBox.addItem(v);
        }
    }

    public void setVisible(boolean b) {
        f.setVisible(b);
    }

    public void bindToSettings(Settings s) {
        // updateToSettings(s);

        p1cs1RadioButton.addItemListener(evt -> {
            if (evt.getStateChange() == ItemEvent.SELECTED) {
                s.setControlScheme(ControlScheme.WASD);
            }
        });
        p1cs2RadioButton.addItemListener(evt -> {
            if (evt.getStateChange() == ItemEvent.SELECTED) {
                s.setControlScheme(ControlScheme.Classic);
            }
        });
        p1cs3RadioButton.addItemListener(evt -> {
            if (evt.getStateChange() == ItemEvent.SELECTED) {
                s.setControlScheme(ControlScheme.SlashBracket);
            }
        });
        p2cs1RadioButton.addItemListener(evt -> {
            if (evt.getStateChange() == ItemEvent.SELECTED) {
                s.setControlSchemeP2(ControlScheme.WASD);
            }
        });
        p2cs2RadioButton.addItemListener(evt -> {
            if (evt.getStateChange() == ItemEvent.SELECTED) {
                s.setControlSchemeP2(ControlScheme.Classic);
            }
        });
        p2cs3RadioButton.addItemListener(evt -> {
            if (evt.getStateChange() == ItemEvent.SELECTED) {
                s.setControlSchemeP2(ControlScheme.SlashBracket);
            }
        });

        p1ha1RadioButton.addItemListener(evt -> {
            if (evt.getStateChange() == ItemEvent.SELECTED) {
                s.setHandlingPreset(HandlingPreset.Default);
            }
        });
        p1ha2RadioButton.addItemListener(evt -> {
            if (evt.getStateChange() == ItemEvent.SELECTED) {
                s.setHandlingPreset(HandlingPreset.Fast);
            }
        });
        p2ha1RadioButton.addItemListener(evt -> {
            if (evt.getStateChange() == ItemEvent.SELECTED) {
                s.setHandlingPresetP2(HandlingPreset.Default);
            }
        });
        p2ha2RadioButton.addItemListener(evt -> {
            if (evt.getStateChange() == ItemEvent.SELECTED) {
                s.setHandlingPresetP2(HandlingPreset.Fast);
            }
        });

        p1sdCheckBox.addItemListener(evt -> s.setSonicDrop(evt.getStateChange() == ItemEvent.SELECTED));
        p2sdCheckBox.addItemListener(evt -> s.setSonicDropP2(evt.getStateChange() == ItemEvent.SELECTED));

        skinComboBox.addItemListener(evt -> {
            if (evt.getStateChange() == ItemEvent.SELECTED) {
                s.setBlockSkin((String) evt.getItem());
            }
        });
    }

    private void updateToSettings(Settings s) {
        switch (s.getControlScheme()) {
            case WASD:
                p1cs1RadioButton.setSelected(true);
                break;
            case Classic:
                p1cs2RadioButton.setSelected(true);
                break;
            case SlashBracket:
                p1cs3RadioButton.setSelected(true);
                break;
        }
        switch (s.getHandlingPreset()) {
            case Default:
                p1ha1RadioButton.setSelected(true);
                break;
            case Fast:
                p1ha2RadioButton.setSelected(true);
                break;
            default:
                break;
        }
        p1sdCheckBox.setSelected(s.getSonicDrop());
        switch (s.getControlSchemeP2()) {
            case WASD:
                p2cs1RadioButton.setSelected(true);
                break;
            case Classic:
                p2cs2RadioButton.setSelected(true);
                break;
            case SlashBracket:
                p2cs3RadioButton.setSelected(true);
                break;
        }
        switch (s.getHandlingPresetP2()) {
            case Default:
                p2ha1RadioButton.setSelected(true);
                break;
            case Fast:
                p2ha2RadioButton.setSelected(true);
                break;
            default:
                break;
        }
        p2sdCheckBox.setSelected(s.getSonicDropP2());

        skinComboBox.setSelectedItem(s.getBlockSkin());
    }

    @Override
    public Map<SettingKey, Consumer<Object>> getReceivers() {
        Map<SettingKey, Consumer<Object>> receiversMap = new EnumMap<>(SettingKey.class);
        receiversMap.put(SettingKey.BlockSkin, x -> {
            skinComboBox.setEnabled(false);
            skinComboBox.setSelectedItem((String) x);
            skinComboBox.setEnabled(true);
            skinTestPanel.repaint();
        });
        receiversMap.put(SettingKey.BlockConnectionMode, x -> {
            skinTestPanel.repaint();
        });
        receiversMap.put(SettingKey.ControlScheme, x -> {
            switch ((ControlScheme) x) {
                case WASD:
                    p1cs1RadioButton.setSelected(true);
                    break;
                case Classic:
                    p1cs2RadioButton.setSelected(true);
                    break;
                case SlashBracket:
                    p1cs3RadioButton.setSelected(true);
                    break;
            }
        });
        receiversMap.put(SettingKey.ControlSchemeP2, x -> {
            switch ((ControlScheme) x) {
                case WASD:
                    p2cs1RadioButton.setSelected(true);
                    break;
                case Classic:
                    p2cs2RadioButton.setSelected(true);
                    break;
                case SlashBracket:
                    p2cs3RadioButton.setSelected(true);
                    break;
            }
        });
        receiversMap.put(SettingKey.HandlingPreset, x -> {
            switch ((HandlingPreset) x) {
                case Default:
                    p1ha1RadioButton.setSelected(true);
                    break;
                case Fast:
                    p1ha2RadioButton.setSelected(true);
                    break;
                default:
                    p1ha1RadioButton.setSelected(false);
                    p1ha2RadioButton.setSelected(false);
                    break;
            }
        });
        receiversMap.put(SettingKey.HandlingPresetP2, x -> {
            switch ((HandlingPreset) x) {
                case Default:
                    p2ha1RadioButton.setSelected(true);
                    break;
                case Fast:
                    p2ha2RadioButton.setSelected(true);
                    break;
                default:
                    p2ha1RadioButton.setSelected(false);
                    p2ha2RadioButton.setSelected(false);
                    break;
            }
        });
        receiversMap.put(SettingKey.SonicDrop, x -> {
            p1sdCheckBox.setSelected((boolean) x);
        });
        receiversMap.put(SettingKey.SonicDropP2, x -> {
            p2sdCheckBox.setSelected((boolean) x);
        });
        return receiversMap;
    }

    public void setNewGameAction(ActionListener a) {
        newGameButton.addActionListener(a);
    }

    public void focusAndBringToFront() {
        f.setAlwaysOnTop(true);
        f.setAlwaysOnTop(false);
        f.getRootPane().requestFocusInWindow();
    }

    public void resetNames() {
        p1NameField.setText("");
        p2NameField.setText("");
    }

    public void update() {
        long currFrameTime = System.nanoTime();
        long deltaFrameTime = currFrameTime - lastFrameTime;
        frameTimeAccumulator += deltaFrameTime;

        if (TimeUnit.MILLISECONDS.toNanos(16) < frameTimeAccumulator) {
            frameTimeAccumulator = Math.max(0, frameTimeAccumulator - TimeUnit.MILLISECONDS.toNanos(16));

            windowDeltaX *= Math.pow(11.0 / 12.0,
                    TimeUnit.MILLISECONDS.toNanos(16) / TimeUnit.MILLISECONDS.toNanos(10));
            windowDeltaY *= Math.pow(11.0 / 12.0,
                    TimeUnit.MILLISECONDS.toNanos(16) / TimeUnit.MILLISECONDS.toNanos(10));

            int windowVelocityX = (int) (windowDeltaX - windowLastDeltaX);
            int windowVelocityY = (int) (windowDeltaY - windowLastDeltaY);

            windowLastDeltaX += windowVelocityX;
            windowLastDeltaY += windowVelocityY;

            if (windowVelocityX != 0 || windowVelocityY != 0) {
                var frameLoc = f.getLocationOnScreen();
                f.setLocation(frameLoc.x + windowVelocityX, frameLoc.y + windowVelocityY);
            }
        }

        lastFrameTime = currFrameTime;
    }

    public static class PartialLeaderboardEntry {
        public final String nameP1;
        public final String nameP2;

        public PartialLeaderboardEntry(String nameP1, String nameP2) {
            this.nameP1 = nameP1;
            this.nameP2 = nameP2;
        }
    }

    public PartialLeaderboardEntry getLeaderboardData() {
        String p1 = p1NameField.getText();
        String p2 = p2NameField.getText();
        if (p1.length() == 0) {
            p1 = "P1";
        }
        if (p2.length() == 0) {
            p2 = "P2";
        }
        return new PartialLeaderboardEntry(p1, p2);
    }

}
