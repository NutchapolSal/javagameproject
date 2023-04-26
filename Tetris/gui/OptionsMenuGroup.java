package Tetris.gui;

import Tetris.data.mino.MinoColor;
import Tetris.settings.BlockConnectionMode;
import Tetris.settings.ControlScheme;
import Tetris.settings.HandlingPreset;
import Tetris.settings.Settings;
import java.awt.event.ItemEvent;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;

public class OptionsMenuGroup {
    private JMenu optionsMenu = new JMenu();
    private JMenu controlSchemeMenu = new JMenu();
    private JRadioButtonMenuItem wasdSchemeMenuItem = new JRadioButtonMenuItem();
    private JRadioButtonMenuItem classicSchemeMenuItem = new JRadioButtonMenuItem();
    private JRadioButtonMenuItem slashBracketSchemeMenuItem = new JRadioButtonMenuItem();
    private JMenu handlingMenu = new JMenu();
    private JRadioButtonMenuItem defaultHandlingMenuItem = new JRadioButtonMenuItem();
    private JRadioButtonMenuItem fastHandlingMenuItem = new JRadioButtonMenuItem();
    private JCheckBoxMenuItem sonicDropMenuItem = new JCheckBoxMenuItem();
    private JMenu blockSkinMenu = new JMenu();
    private JRadioButtonMenuItem[] blockSkinMenuItems = new JRadioButtonMenuItem[BlockSkinManager
            .getBlockSkinFolders().length];
    private JMenu blockConnectionMenu = new JMenu();
    private JRadioButtonMenuItem noneConnectionMenuItem = new JRadioButtonMenuItem();
    private JRadioButtonMenuItem minoConnectionMenuItem = new JRadioButtonMenuItem();
    private JRadioButtonMenuItem colorConnectionMenuItem = new JRadioButtonMenuItem();
    private JRadioButtonMenuItem allConnectionMenuItem = new JRadioButtonMenuItem();

    public OptionsMenuGroup() {
        detailControlSchemeMenu();
        detailHandlingMenu();
        sonicDropMenuItem.setText("Sonic Drop");
        detailBlockSkinMenu();
        detailBlockConnectionMenu();

        optionsMenu.setText("Options");
        optionsMenu.add(controlSchemeMenu);
        optionsMenu.add(handlingMenu);
        optionsMenu.add(sonicDropMenuItem);
        optionsMenu.addSeparator();
        optionsMenu.add(blockSkinMenu);
        optionsMenu.add(blockConnectionMenu);
    }

    private void detailControlSchemeMenu() {
        wasdSchemeMenuItem.setSelected(true);
        wasdSchemeMenuItem.setText("WASD");

        classicSchemeMenuItem.setText("Classic");

        slashBracketSchemeMenuItem.setText("SlashBracket");

        controlSchemeMenu.setText("Control Scheme");
        controlSchemeMenu.add(wasdSchemeMenuItem);
        controlSchemeMenu.add(classicSchemeMenuItem);
        controlSchemeMenu.add(slashBracketSchemeMenuItem);
        ButtonGroup controlSchemeGroup = new ButtonGroup();
        controlSchemeGroup.add(wasdSchemeMenuItem);
        controlSchemeGroup.add(classicSchemeMenuItem);
        controlSchemeGroup.add(slashBracketSchemeMenuItem);
    }

    private void detailHandlingMenu() {
        defaultHandlingMenuItem.setSelected(true);
        defaultHandlingMenuItem.setText("Default");

        fastHandlingMenuItem.setText("Fast");

        handlingMenu.setText("Handling");
        handlingMenu.add(defaultHandlingMenuItem);
        handlingMenu.add(fastHandlingMenuItem);
        ButtonGroup handlingGroup = new ButtonGroup();
        handlingGroup.add(defaultHandlingMenuItem);
        handlingGroup.add(fastHandlingMenuItem);
    }

    private void detailBlockSkinMenu() {
        blockSkinMenu.setText("Block Skin");
        ButtonGroup blockSkinGroup = new ButtonGroup();
        String[] blockSkinFolders = BlockSkinManager.getBlockSkinFolders();
        for (int i = 0; i < blockSkinMenuItems.length; i++) {
            blockSkinMenuItems[i] = new JRadioButtonMenuItem();
            blockSkinMenuItems[i].setText(blockSkinFolders[i]);
            blockSkinMenuItems[i].setIcon(
                    new ImageIcon(
                            BlockSkinManager.getImagesFromFolder(blockSkinFolders[i], MinoColor.Red, 1).images[0]));
            blockSkinMenu.add(blockSkinMenuItems[i]);
            blockSkinGroup.add(blockSkinMenuItems[i]);
            if (i == 0) {
                blockSkinMenuItems[i].setSelected(true);
            }
        }
    }

    private void detailBlockConnectionMenu() {
        noneConnectionMenuItem.setText("None");
        minoConnectionMenuItem.setText("Mino");
        colorConnectionMenuItem.setText("Color");
        allConnectionMenuItem.setText("All");

        blockConnectionMenu.setText("Block Connection");
        blockConnectionMenu.add(noneConnectionMenuItem);
        blockConnectionMenu.add(minoConnectionMenuItem);
        blockConnectionMenu.add(colorConnectionMenuItem);
        blockConnectionMenu.add(allConnectionMenuItem);
        ButtonGroup blockConnectionGroup = new ButtonGroup();
        blockConnectionGroup.add(noneConnectionMenuItem);
        blockConnectionGroup.add(minoConnectionMenuItem);
        blockConnectionGroup.add(colorConnectionMenuItem);
        blockConnectionGroup.add(allConnectionMenuItem);
    }

    public void bindToSettings(Settings s) {
        updateToSettings(s);

        bindControlSchemeMenuItems(s);
        bindHandlingMenuItems(s);
        sonicDropMenuItem.addItemListener(evt -> s.setSonicDrop(evt.getStateChange() == ItemEvent.SELECTED));
        bindBlockSkinMenuItems(s);
        bindBlockConnectionMenuItems(s);
    }

    private void updateToSettings(Settings s) {
        switch (s.getControlScheme()) {
            case WASD:
                wasdSchemeMenuItem.setSelected(true);
                break;
            case Classic:
                classicSchemeMenuItem.setSelected(true);
                break;
            case SlashBracket:
                slashBracketSchemeMenuItem.setSelected(true);
                break;
        }
        switch (s.getHandlingPreset()) {
            case Default:
                defaultHandlingMenuItem.setSelected(true);
                break;
            case Fast:
                fastHandlingMenuItem.setSelected(true);
                break;
            default:
                break;
        }
        sonicDropMenuItem.setSelected(s.getSonicDrop());
        String selectedSkin = s.getBlockSkin();
        for (JRadioButtonMenuItem v : blockSkinMenuItems) {
            if (v.getText().equals(selectedSkin)) {
                v.setSelected(true);
                break;
            }
        }
        switch (s.getBlockConnectionMode()) {
            case None:
                noneConnectionMenuItem.setSelected(true);
                break;
            case Mino:
                minoConnectionMenuItem.setSelected(true);
                break;
            case Color:
                colorConnectionMenuItem.setSelected(true);
                break;
            case All:
                allConnectionMenuItem.setSelected(true);
                break;
            default:
                break;
        }
    }

    private void bindControlSchemeMenuItems(Settings s) {
        wasdSchemeMenuItem.addItemListener(evt -> {
            if (evt.getStateChange() == ItemEvent.SELECTED) {
                s.setControlScheme(ControlScheme.WASD);
            }
        });
        classicSchemeMenuItem.addItemListener(evt -> {
            if (evt.getStateChange() == ItemEvent.SELECTED) {
                s.setControlScheme(ControlScheme.Classic);
            }
        });
        slashBracketSchemeMenuItem.addItemListener(evt -> {
            if (evt.getStateChange() == ItemEvent.SELECTED) {
                s.setControlScheme(ControlScheme.SlashBracket);
            }
        });
    }

    private void bindHandlingMenuItems(Settings s) {
        defaultHandlingMenuItem.addItemListener(evt -> {
            if (evt.getStateChange() == ItemEvent.SELECTED) {
                s.setHandlingPreset(HandlingPreset.Default);
            }
        });
        fastHandlingMenuItem.addItemListener(evt -> {
            if (evt.getStateChange() == ItemEvent.SELECTED) {
                s.setHandlingPreset(HandlingPreset.Fast);
            }
        });
    }

    private void bindBlockSkinMenuItems(Settings s) {
        for (int i = 0; i < blockSkinMenuItems.length; i++) {
            final String yourFolder = BlockSkinManager.getBlockSkinFolders()[i];
            blockSkinMenuItems[i].addItemListener(evt -> {
                if (evt.getStateChange() == ItemEvent.SELECTED) {
                    s.setBlockSkin(yourFolder);
                }
            });
        }
    }

    private void bindBlockConnectionMenuItems(Settings s) {
        noneConnectionMenuItem.addItemListener(evt -> {
            if (evt.getStateChange() == ItemEvent.SELECTED) {
                s.setBlockConnectionMode(BlockConnectionMode.None);
            }
        });
        minoConnectionMenuItem.addItemListener(evt -> {
            if (evt.getStateChange() == ItemEvent.SELECTED) {
                s.setBlockConnectionMode(BlockConnectionMode.Mino);
            }
        });
        colorConnectionMenuItem.addItemListener(evt -> {
            if (evt.getStateChange() == ItemEvent.SELECTED) {
                s.setBlockConnectionMode(BlockConnectionMode.Color);
            }
        });
        allConnectionMenuItem.addItemListener(evt -> {
            if (evt.getStateChange() == ItemEvent.SELECTED) {
                s.setBlockConnectionMode(BlockConnectionMode.All);
            }
        });
    }

    public JMenu getMenu() {
        return optionsMenu;
    }
}
