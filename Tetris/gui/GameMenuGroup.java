package Tetris.gui;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;

public class GameMenuGroup {
    private JMenu gameMenu = new JMenu();
    private JMenuItem newGameMenuItem = new JMenuItem();
    private JMenu selectModeMenuItem = new JMenu();
    private JRadioButtonMenuItem marathonModeMenuItem = new JRadioButtonMenuItem();
    private JRadioButtonMenuItem sprintModeMenuItem = new JRadioButtonMenuItem();
    private JRadioButtonMenuItem ultraModeMenuItem = new JRadioButtonMenuItem();
    private JRadioButtonMenuItem zenModeMenuItem = new JRadioButtonMenuItem();

    public GameMenuGroup() {
        newGameMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
                InputEvent.CTRL_DOWN_MASK));
        newGameMenuItem.setText("New Game");

        marathonModeMenuItem.setSelected(true);
        marathonModeMenuItem.setText("150 Lines Marathon");

        sprintModeMenuItem.setText("40 Lines Sprint");

        ultraModeMenuItem.setText("3 Minutes Ultra");

        zenModeMenuItem.setText("Zen");

        selectModeMenuItem.setText("Select Mode");
        selectModeMenuItem.add(marathonModeMenuItem);
        selectModeMenuItem.add(sprintModeMenuItem);
        selectModeMenuItem.add(ultraModeMenuItem);
        selectModeMenuItem.add(zenModeMenuItem);
        ButtonGroup selectModeGroup = new ButtonGroup();
        selectModeGroup.add(marathonModeMenuItem);
        selectModeGroup.add(sprintModeMenuItem);
        selectModeGroup.add(ultraModeMenuItem);
        selectModeGroup.add(zenModeMenuItem);

        gameMenu.setText("Game");
        gameMenu.add(newGameMenuItem);
        gameMenu.add(selectModeMenuItem);
    }

    public JMenu getMenu() {
        return gameMenu;
    }

    public JMenuItem getNewGameMenuItem() {
        return newGameMenuItem;
    }

    public JMenu getSelectModeMenuItem() {
        return selectModeMenuItem;
    }

    public JRadioButtonMenuItem getMarathonModeMenuItem() {
        return marathonModeMenuItem;
    }

    public JRadioButtonMenuItem getSprintModeMenuItem() {
        return sprintModeMenuItem;
    }

    public JRadioButtonMenuItem getUltraModeMenuItem() {
        return ultraModeMenuItem;
    }

    public JRadioButtonMenuItem getZenModeMenuItem() {
        return zenModeMenuItem;
    }

}
