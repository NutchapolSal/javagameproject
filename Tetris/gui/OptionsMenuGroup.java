package Tetris.gui;

import Tetris.data.mino.MinoColor;
import Tetris.settings.BlockConnectionMode;
import Tetris.settings.ControlScheme;
import Tetris.settings.HandlingPreset;
import Tetris.settings.ReceiveSettings;
import Tetris.settings.SettingKey;
import Tetris.settings.Settings;
import java.awt.event.ItemEvent;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Consumer;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

public class OptionsMenuGroup implements ReceiveSettings {
    private JMenu optionsMenu = new JMenu();

    private JMenu p1Menu = new JMenu();
    private JMenu p2Menu = new JMenu();

    private JMenu controlSchemeMenu = new JMenu();
    private JRadioButtonMenuItem wasdSchemeMenuItem = new JRadioButtonMenuItem();
    private JRadioButtonMenuItem classicSchemeMenuItem = new JRadioButtonMenuItem();
    private JRadioButtonMenuItem slashBracketSchemeMenuItem = new JRadioButtonMenuItem();
    private JMenu handlingMenu = new JMenu();
    private JRadioButtonMenuItem defaultHandlingMenuItem = new JRadioButtonMenuItem();
    private JRadioButtonMenuItem fastHandlingMenuItem = new JRadioButtonMenuItem();
    private JCheckBoxMenuItem sonicDropMenuItem = new JCheckBoxMenuItem();

    private JMenu controlSchemeP2Menu = new JMenu();
    private JRadioButtonMenuItem wasdSchemeP2MenuItem = new JRadioButtonMenuItem();
    private JRadioButtonMenuItem classicSchemeP2MenuItem = new JRadioButtonMenuItem();
    private JRadioButtonMenuItem slashBracketSchemeP2MenuItem = new JRadioButtonMenuItem();
    private JMenu handlingP2Menu = new JMenu();
    private JRadioButtonMenuItem defaultHandlingP2MenuItem = new JRadioButtonMenuItem();
    private JRadioButtonMenuItem fastHandlingP2MenuItem = new JRadioButtonMenuItem();
    private JCheckBoxMenuItem sonicDropP2MenuItem = new JCheckBoxMenuItem();

    private JMenu blockSkinMenu = new JMenu();
    private JRadioButtonMenuItem[] blockSkinMenuItems = new JRadioButtonMenuItem[BlockSkinManager
            .getBlockSkinFolders().length];
    private JMenu blockConnectionMenu = new JMenu();
    private JRadioButtonMenuItem noneConnectionMenuItem = new JRadioButtonMenuItem();
    private JRadioButtonMenuItem minoConnectionMenuItem = new JRadioButtonMenuItem();
    private JRadioButtonMenuItem colorConnectionMenuItem = new JRadioButtonMenuItem();
    private JRadioButtonMenuItem allConnectionMenuItem = new JRadioButtonMenuItem();

    private JMenuItem quickSettingsMenuItem = new JMenuItem();

    public OptionsMenuGroup() {
        detailP1Menu();
        detailP2Menu();

        detailBlockSkinMenu();
        detailBlockConnectionMenu();

        detailQuickSettingsItem();

        optionsMenu.setText("Options");
        optionsMenu.add(p1Menu);
        optionsMenu.add(p2Menu);
        optionsMenu.addSeparator();
        optionsMenu.add(blockSkinMenu);
        optionsMenu.add(blockConnectionMenu);
        optionsMenu.addSeparator();
        optionsMenu.add(quickSettingsMenuItem);
    }

    private void detailQuickSettingsItem() {
        quickSettingsMenuItem.setText("Quick Settings");
    }

    private void detailP1Menu() {
        detailControlSchemeMenu();
        detailHandlingMenu();
        sonicDropMenuItem.setText("Sonic Drop");

        p1Menu.setText("P1");
        p1Menu.add(controlSchemeMenu);
        p1Menu.add(handlingMenu);
        p1Menu.add(sonicDropMenuItem);
    }

    private void detailP2Menu() {
        detailControlSchemeP2Menu();
        detailHandlingP2Menu();
        sonicDropP2MenuItem.setText("Sonic Drop");

        p2Menu.setText("P2");
        p2Menu.add(controlSchemeP2Menu);
        p2Menu.add(handlingP2Menu);
        p2Menu.add(sonicDropP2MenuItem);
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

    private void detailControlSchemeP2Menu() {
        wasdSchemeP2MenuItem.setSelected(true);
        wasdSchemeP2MenuItem.setText("WASD");

        classicSchemeP2MenuItem.setText("Classic");

        slashBracketSchemeP2MenuItem.setText("SlashBracket");

        controlSchemeP2Menu.setText("Control Scheme");
        controlSchemeP2Menu.add(wasdSchemeP2MenuItem);
        controlSchemeP2Menu.add(classicSchemeP2MenuItem);
        controlSchemeP2Menu.add(slashBracketSchemeP2MenuItem);
        ButtonGroup controlSchemeGroup = new ButtonGroup();
        controlSchemeGroup.add(wasdSchemeP2MenuItem);
        controlSchemeGroup.add(classicSchemeP2MenuItem);
        controlSchemeGroup.add(slashBracketSchemeP2MenuItem);
    }

    private void detailHandlingP2Menu() {
        defaultHandlingP2MenuItem.setSelected(true);
        defaultHandlingP2MenuItem.setText("Default");

        fastHandlingP2MenuItem.setText("Fast");

        handlingP2Menu.setText("Handling");
        handlingP2Menu.add(defaultHandlingP2MenuItem);
        handlingP2Menu.add(fastHandlingP2MenuItem);
        ButtonGroup handlingGroup = new ButtonGroup();
        handlingGroup.add(defaultHandlingP2MenuItem);
        handlingGroup.add(fastHandlingP2MenuItem);
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
        bindControlSchemeP2MenuItems(s);
        bindHandlingP2MenuItems(s);
        sonicDropP2MenuItem.addItemListener(evt -> s.setSonicDropP2(evt.getStateChange() == ItemEvent.SELECTED));
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
        switch (s.getControlSchemeP2()) {
            case WASD:
                wasdSchemeP2MenuItem.setSelected(true);
                break;
            case Classic:
                classicSchemeP2MenuItem.setSelected(true);
                break;
            case SlashBracket:
                slashBracketSchemeP2MenuItem.setSelected(true);
                break;
        }
        switch (s.getHandlingPresetP2()) {
            case Default:
                defaultHandlingP2MenuItem.setSelected(true);
                break;
            case Fast:
                fastHandlingP2MenuItem.setSelected(true);
                break;
            default:
                break;
        }
        sonicDropP2MenuItem.setSelected(s.getSonicDropP2());

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

    private void bindControlSchemeP2MenuItems(Settings s) {
        wasdSchemeP2MenuItem.addItemListener(evt -> {
            if (evt.getStateChange() == ItemEvent.SELECTED) {
                s.setControlSchemeP2(ControlScheme.WASD);
            }
        });
        classicSchemeP2MenuItem.addItemListener(evt -> {
            if (evt.getStateChange() == ItemEvent.SELECTED) {
                s.setControlSchemeP2(ControlScheme.Classic);
            }
        });
        slashBracketSchemeP2MenuItem.addItemListener(evt -> {
            if (evt.getStateChange() == ItemEvent.SELECTED) {
                s.setControlSchemeP2(ControlScheme.SlashBracket);
            }
        });
    }

    private void bindHandlingP2MenuItems(Settings s) {
        defaultHandlingP2MenuItem.addItemListener(evt -> {
            if (evt.getStateChange() == ItemEvent.SELECTED) {
                s.setHandlingPresetP2(HandlingPreset.Default);
            }
        });
        fastHandlingP2MenuItem.addItemListener(evt -> {
            if (evt.getStateChange() == ItemEvent.SELECTED) {
                s.setHandlingPresetP2(HandlingPreset.Fast);
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

    @Override
    public Map<SettingKey, Consumer<Object>> getReceivers() {
        Map<SettingKey, Consumer<Object>> receiversMap = new EnumMap<>(SettingKey.class);
        receiversMap.put(SettingKey.ControlScheme, x -> {
            switch ((ControlScheme) x) {
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
        });
        receiversMap.put(SettingKey.ControlSchemeP2, x -> {
            switch ((ControlScheme) x) {
                case WASD:
                    wasdSchemeP2MenuItem.setSelected(true);
                    break;
                case Classic:
                    classicSchemeP2MenuItem.setSelected(true);
                    break;
                case SlashBracket:
                    slashBracketSchemeP2MenuItem.setSelected(true);
                    break;
            }
        });
        receiversMap.put(SettingKey.HandlingPreset, x -> {
            switch ((HandlingPreset) x) {
                case Default:
                    defaultHandlingMenuItem.setSelected(true);
                    break;
                case Fast:
                    fastHandlingMenuItem.setSelected(true);
                    break;
                default:
                    defaultHandlingMenuItem.setSelected(false);
                    fastHandlingMenuItem.setSelected(false);
                    break;
            }
        });
        receiversMap.put(SettingKey.HandlingPresetP2, x -> {
            switch ((HandlingPreset) x) {
                case Default:
                    defaultHandlingP2MenuItem.setSelected(true);
                    break;
                case Fast:
                    fastHandlingP2MenuItem.setSelected(true);
                    break;
                default:
                    defaultHandlingP2MenuItem.setSelected(false);
                    fastHandlingP2MenuItem.setSelected(false);
                    break;
            }
        });
        receiversMap.put(SettingKey.SonicDrop, x -> {
            sonicDropMenuItem.setSelected((boolean) x);
        });
        receiversMap.put(SettingKey.SonicDropP2, x -> {
            sonicDropP2MenuItem.setSelected((boolean) x);
        });
        receiversMap.put(SettingKey.BlockSkin, x -> {
            String selectedSkin = (String) x;
            for (JRadioButtonMenuItem v : blockSkinMenuItems) {
                if (v.getText().equals(selectedSkin)) {
                    v.setSelected(true);
                    break;
                }
            }
        });
        receiversMap.put(SettingKey.BlockConnectionMode, x -> {
            switch ((BlockConnectionMode) x) {
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
        });
        return receiversMap;
    }

    public JMenuItem getQuickSettingsMenuItem() {
        return quickSettingsMenuItem;
    }

}
