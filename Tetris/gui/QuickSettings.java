package Tetris.gui;

import Tetris.settings.ReceiveSettings;
import Tetris.settings.SettingKey;
import Tetris.settings.Settings;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Map;
import java.util.function.Consumer;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class QuickSettings implements ReceiveSettings {
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
    private JTextField p1NameField = new JTextField();

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
    private JTextField p2NameField = new JTextField();

    private JPanel skinPanel = new JPanel();
    private SkinTestPanel skinTestPanel;
    private JButton skinPrevButton = new JButton();
    private JButton skinNextButton = new JButton();
    private JComboBox<String> skinComboBox = new JComboBox<>();

    public QuickSettings(Component parent, BlockSkinManager bsm) {
        this.bsm = bsm;

        f.setSize(800, 180);
        f.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        detailComponents();

        var parentLocation = parent.getLocationOnScreen();
        var parentSize = parent.getSize();
        f.setLocation(parentLocation.x, parentLocation.y + parentSize.height - 20);

        f.setAutoRequestFocus(false);
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
        p1NameField.setText("P1!");

        p1cs1RadioButton.setSelected(true);
        p1ha1RadioButton.setSelected(true);

        ButtonGroup csButtonGroup = new ButtonGroup();
        csButtonGroup.add(p1cs1RadioButton);
        csButtonGroup.add(p1cs2RadioButton);
        csButtonGroup.add(p1cs3RadioButton);
        ButtonGroup hsButtonGroup = new ButtonGroup();
        hsButtonGroup.add(p1ha1RadioButton);
        hsButtonGroup.add(p1ha2RadioButton);

        p1NameField.setFont(p1NameField.getFont().deriveFont(p1NameField.getFont().getStyle() | Font.BOLD,
                p1NameField.getFont().getSize() + 8));

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
        p2NameField.setText("P2!");

        p2cs1RadioButton.setSelected(true);
        p2ha1RadioButton.setSelected(true);

        ButtonGroup csButtonGroup = new ButtonGroup();
        csButtonGroup.add(p2cs1RadioButton);
        csButtonGroup.add(p2cs2RadioButton);
        csButtonGroup.add(p2cs3RadioButton);
        ButtonGroup hsButtonGroup = new ButtonGroup();
        hsButtonGroup.add(p2ha1RadioButton);
        hsButtonGroup.add(p2ha2RadioButton);

        p2NameField.setFont(p2NameField.getFont().deriveFont(p2NameField.getFont().getStyle() | Font.BOLD,
                p2NameField.getFont().getSize() + 8));

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

        GroupLayout layout = new GroupLayout(skinPanel);
        skinPanel.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(Alignment.CENTER, false)
                .addComponent(skinTestPanel.getPanel())
                .addGroup(layout.createSequentialGroup()
                        .addComponent(skinPrevButton)
                        .addComponent(skinComboBox)
                        .addComponent(skinNextButton)));

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(skinTestPanel.getPanel())
                .addGroup(layout.createParallelGroup(Alignment.CENTER, false)
                        .addComponent(skinPrevButton)
                        .addComponent(skinComboBox)
                        .addComponent(skinNextButton)));

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
        updateToSettings(s);
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
        // TODO Auto-generated method stub
        return null;
    }

}
