package Tetris;

import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import java.awt.Font;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.awt.Container;

public class Gui {
    private JFrame f;
    private JPanel centerPanel;
    private JLabel controlsText;
    private OneMinoPanel holdMino;
    private JPanel holdPanel;
    private JLabel holdText;
    private JPanel statsPanel;
    private Box.Filler leftFiller;
    private JLabel levelCountText;
    private JLabel levelText;
    private JLabel linesCountText;
    private JLabel linesText;
    private JButton newGameButton;
    private OneMinoPanel[] nextMinos;
    private JPanel nextPanel;
    private JLabel nextText;
    private PlayfieldPanel playfield;
    private Box.Filler rightFiller;
    private JLabel timeCountText;
    private JLabel timeText;
    private JPanel miscPanel;
    private JPanel calloutsPanel;
    private CalloutLabel spinLabel;
    private CalloutLabel lineCalloutLabel;
    private CalloutLabel comboLabel;
    private CalloutLabel b2bLabel;

    private KeyboardHandler kbh;

    private double windowDeltaX;
    private double windowDeltaY;
    private double windowLastDeltaX;
    private double windowLastDeltaY;

    private int lastB2B = 0;

    private static double roundToZero(double in) {
        if (in < 0) {
            return Math.ceil(in);
        } else {
            return Math.floor(in);
        }
    }

    public void update(GuiData gds) {
        if (gds.spinName != null) {
            spinLabel.startFadeOutAnimation((gds.spinMini ? "mini " : "") + gds.spinName + " spin");
        }
        if (gds.calloutLines != 0) {
            String calloutLinesStr = "";
            if (gds.calloutLines == 1) {
                calloutLinesStr = "SINGLE";
            } else if (gds.calloutLines == 2) {
                calloutLinesStr = "DOUBLE";
            } else if (gds.calloutLines == 3) {
                calloutLinesStr = "TRIPLE";
            } else if (gds.calloutLines == 4) {
                calloutLinesStr = "QUAD";
            } else {
                calloutLinesStr = "";
            }
            lineCalloutLabel.startFadeOutAnimation(calloutLinesStr);
        }
        // lineCalloutLabel.setText(String.format("%s", col.startAnimation()));
        if (gds.b2bCount != lastB2B) {
            if (gds.b2bCount != 0) {
                b2bLabel.startAnimation(String.format("B2B x%s", gds.b2bCount));
                lastB2B = gds.b2bCount;
            } else {
                b2bLabel.startFadeOutAnimation(String.format("B2B x%s", gds.b2bCount));
                lastB2B = gds.b2bCount;
            }
        }
        // comboLabel.setText(String.format("%d", gds.calloutLines));

        timeCountText.setText(
                String.format("%.0f:%05.2f", Math.floor(gds.timeMillis / (1000d * 60)),
                        (gds.timeMillis / 1000d) % 60));
        linesCountText.setText(String.format("%d", gds.linesCleared));
        levelCountText.setText(String.format("%d", gds.level));
        if (gds.lockHold) {
            holdMino.setMino(gds.hold, MinoColor.Gray);
        } else {
            holdMino.setMino(gds.hold);
        }

        if (gds.renderBlocks != null) {
            playfield.setRenderBlocks(gds.renderBlocks);
        }
        if (gds.nextQueue != null) {
            for (int i = 0; i < gds.nextQueue.length && i < nextMinos.length; i++) {
                nextMinos[i].setMino(gds.nextQueue[i]);
            }
        }
        playfield.setPlayerRenderData(gds.playerRenderData, gds.playerLockProgress);
        f.repaint();

        windowDeltaX += gds.windowNudgeX;
        windowDeltaY += gds.windowNudgeY;

        windowDeltaX -= windowDeltaX / 12;
        windowDeltaY -= windowDeltaY / 12;

        int windowVelocityX = (int) roundToZero(windowDeltaX - windowLastDeltaX);
        int windowVelocityY = (int) roundToZero(windowDeltaY - windowLastDeltaY);

        windowLastDeltaX += windowVelocityX;
        windowLastDeltaY += windowVelocityY;

        if (windowVelocityX != 0 || windowVelocityY != 0) {
            var frameLoc = f.getLocationOnScreen();
            f.setLocation(frameLoc.x + windowVelocityX, frameLoc.y + windowVelocityY);
        }
    }

    public Gui() {
        setLookAndFeel();
        f = new JFrame("Tetris");
        f.setSize(500, 500);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        detailComponents();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    private void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void detailComponents() {
        leftFiller = new Box.Filler(
                new Dimension(0, 0),
                new Dimension(0, 0),
                new Dimension(32767, 0));
        createCenterPanel();
        rightFiller = new Box.Filler(
                new Dimension(0, 0),
                new Dimension(0, 0),
                new Dimension(32767, 0));

        Container contentPane = f.getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
        contentPane.add(leftFiller);
        contentPane.add(centerPanel);
        contentPane.add(rightFiller);

    }

    private void createCenterPanel() {
        centerPanel = new JPanel();
        createPlayfield();
        createStatsPanel();
        createNextPanel();
        createHoldPanel();
        createCallOutsPanel();
        createMiscPanel();

        GroupLayout centerPanelLayout = new GroupLayout(centerPanel);
        centerPanel.setLayout(centerPanelLayout);
        centerPanelLayout.setHorizontalGroup(centerPanelLayout.createSequentialGroup()
                .addGroup(centerPanelLayout.createParallelGroup(Alignment.TRAILING)
                        .addComponent(holdPanel)
                        .addComponent(calloutsPanel)
                        .addComponent(statsPanel))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(playfield)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(centerPanelLayout.createParallelGroup()
                        .addComponent(nextPanel)
                        .addComponent(miscPanel)

                ));
        centerPanelLayout.setVerticalGroup(centerPanelLayout.createParallelGroup(Alignment.LEADING, false)
                .addGroup(centerPanelLayout.createSequentialGroup()
                        .addComponent(holdPanel)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(calloutsPanel)
                        .addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE,
                                Integer.MAX_VALUE)
                        .addComponent(statsPanel))
                .addComponent(playfield)
                .addGroup(centerPanelLayout.createSequentialGroup()
                        .addComponent(nextPanel)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(miscPanel)

                ));
    }

    private void createHoldPanel() {
        holdPanel = new JPanel();
        holdMino = new OneMinoPanel();
        holdText = new JLabel();

        holdText.setText("Hold");

        GroupLayout holdPanelLayout = new GroupLayout(holdPanel);
        holdPanel.setLayout(holdPanelLayout);
        holdPanelLayout.setHorizontalGroup(holdPanelLayout.createParallelGroup(Alignment.TRAILING)
                .addComponent(holdText)
                .addComponent(holdMino));
        holdPanelLayout.setVerticalGroup(holdPanelLayout.createSequentialGroup()
                .addComponent(holdText)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(holdMino));
    }

    private void createCallOutsPanel() {
        calloutsPanel = new JPanel();
        createCallOutsLabel();

        GroupLayout calloutsPanelLayout = new GroupLayout(calloutsPanel);
        calloutsPanel.setLayout(calloutsPanelLayout);
        calloutsPanelLayout.setHorizontalGroup(
                calloutsPanelLayout.createParallelGroup(Alignment.TRAILING)
                        .addComponent(spinLabel)
                        .addComponent(lineCalloutLabel)
                        .addComponent(b2bLabel)
                        .addComponent(comboLabel));
        calloutsPanelLayout.setVerticalGroup(calloutsPanelLayout.createSequentialGroup()
                .addComponent(spinLabel)
                .addComponent(lineCalloutLabel)
                .addComponent(b2bLabel)
                .addComponent(comboLabel));

    }

    private void createCallOutsLabel() {
        spinLabel = new CalloutLabel();
        lineCalloutLabel = new CalloutLabel();
        b2bLabel = new CalloutLabel();
        comboLabel = new CalloutLabel();

        lineCalloutLabel.setFont(lineCalloutLabel.getFont().deriveFont(
                lineCalloutLabel.getFont().getStyle() | Font.BOLD, lineCalloutLabel.getFont().getSize() + 6));
        lineCalloutLabel.setHorizontalAlignment(SwingConstants.TRAILING);

        b2bLabel.setFont(b2bLabel.getFont().deriveFont(b2bLabel.getFont().getStyle() | Font.BOLD,
                b2bLabel.getFont().getSize() + 2));
        b2bLabel.setHorizontalAlignment(SwingConstants.TRAILING);

        comboLabel.setFont(comboLabel.getFont().deriveFont(comboLabel.getFont().getStyle() | Font.BOLD,
                comboLabel.getFont().getSize() + 2));
        comboLabel.setHorizontalAlignment(SwingConstants.TRAILING);

        spinLabel.setFont(spinLabel.getFont().deriveFont(spinLabel.getFont().getStyle() | Font.BOLD,
                spinLabel.getFont().getSize() + 2));
        spinLabel.setHorizontalAlignment(SwingConstants.TRAILING);

    }

    private void createMiscPanel() {
        miscPanel = new JPanel();
        newGameButton = new JButton();
        controlsText = new JLabel();

        newGameButton.setFocusable(false);

        newGameButton.setText("New Game");
        controlsText.setText("<html>\nA/D - Move<br>\nS - Soft Drop<br>\nW - Hard Drop<br>\nR - Rotate<br>\nF - Hold");
        controlsText.setVerticalAlignment(SwingConstants.TOP);
        controlsText.setPreferredSize(new Dimension(80, 1));

        GroupLayout miscPanelLayout = new GroupLayout(miscPanel);
        miscPanel.setLayout(miscPanelLayout);
        miscPanelLayout.setHorizontalGroup(miscPanelLayout.createParallelGroup()
                .addComponent(newGameButton)
                .addComponent(controlsText, 0, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE));
        miscPanelLayout.setVerticalGroup(miscPanelLayout.createSequentialGroup()
                .addComponent(newGameButton)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(controlsText));

    }

    private void createNextPanel() {
        nextPanel = new JPanel();
        nextText = new JLabel();
        nextMinos = new OneMinoPanel[5];

        nextText.setText("Next");

        GroupLayout nextPanelLayout = new GroupLayout(nextPanel);
        nextPanel.setLayout(nextPanelLayout);
        var horizGroup = nextPanelLayout.createParallelGroup(Alignment.LEADING)
                .addComponent(nextText);
        var vertGroup = nextPanelLayout.createSequentialGroup()
                .addComponent(nextText);

        for (int i = 0; i < nextMinos.length; i++) {
            nextMinos[i] = new OneMinoPanel();
            horizGroup.addComponent(nextMinos[i]);

            vertGroup.addPreferredGap(ComponentPlacement.RELATED);
            vertGroup.addComponent(nextMinos[i]);
        }
        nextPanelLayout.setHorizontalGroup(horizGroup);
        nextPanelLayout.setVerticalGroup(vertGroup);

    }

    private void createPlayfield() {
        playfield = new PlayfieldPanel();
    }

    private void createStatsPanel() {
        statsPanel = new JPanel();
        createStatsLabels();

        GroupLayout statsPanelLayout = new GroupLayout(statsPanel);
        statsPanel.setLayout(statsPanelLayout);
        statsPanelLayout.setHorizontalGroup(
                statsPanelLayout.createParallelGroup(Alignment.TRAILING)
                        .addComponent(levelText)
                        .addComponent(levelCountText)
                        .addComponent(linesText)
                        .addComponent(linesCountText)
                        .addComponent(timeText)
                        .addComponent(timeCountText));
        statsPanelLayout.setVerticalGroup(statsPanelLayout.createSequentialGroup()
                .addComponent(levelText)
                .addComponent(levelCountText)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(linesText)
                .addComponent(linesCountText)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(timeText)
                .addComponent(timeCountText));
    }

    private void createStatsLabels() {
        levelText = new JLabel();
        levelCountText = new JLabel();
        linesText = new JLabel();
        linesCountText = new JLabel();
        timeText = new JLabel();
        timeCountText = new JLabel();

        levelText.setText("Level");

        levelCountText.setFont(levelCountText.getFont().deriveFont(
                levelCountText.getFont().getStyle() | Font.BOLD, levelCountText.getFont().getSize() + 7));
        levelCountText.setText("0");

        linesText.setText("Lines");

        linesCountText.setFont(linesCountText.getFont().deriveFont(
                linesCountText.getFont().getStyle() | Font.BOLD, linesCountText.getFont().getSize() + 7));
        linesCountText.setText("0");

        timeText.setText("Time");

        timeCountText.setFont(timeCountText.getFont().deriveFont(
                timeCountText.getFont().getStyle() | Font.BOLD, timeCountText.getFont().getSize() + 7));
        timeCountText.setText("00.00");
    }

    public KeyboardHandler getKeyboardHandler() {
        if (kbh == null) {
            kbh = new KeyboardHandler();
        }
        return kbh;
    }

    class KeyboardHandler implements RawInputSource {
        static final String PRESSED = "pressed";
        static final String RELEASED = "released";
        private Map<GameplayButton, Boolean> freshInput = new EnumMap<>(GameplayButton.class);
        private Map<GameplayButton, Boolean> lockInput = new EnumMap<>(GameplayButton.class);

        private KeyboardHandler() {
            InputMap inputMap = Gui.this.f.getRootPane().getInputMap();
            ActionMap actionMap = Gui.this.f.getRootPane().getActionMap();

            for (var v : GameplayButton.values()) {
                freshInput.put(v, false);
            }
            update();

            for (GameplayButton gb : GameplayButton.values()) {
                actionMap.put(gb.name() + PRESSED, new ButtonAction(gb, false));
                actionMap.put(gb.name() + RELEASED, new ButtonAction(gb, true));
            }

            BiConsumer<GameplayButton, Integer> setupKeyAction = (gb, keyCode) -> {
                inputMap.put(KeyStroke.getKeyStroke(keyCode, 0, false), gb.name() + PRESSED);
                inputMap.put(KeyStroke.getKeyStroke(keyCode, 0, true), gb.name() + RELEASED);
            };

            setupKeyAction.accept(GameplayButton.Left, KeyEvent.VK_A);
            setupKeyAction.accept(GameplayButton.Right, KeyEvent.VK_D);
            setupKeyAction.accept(GameplayButton.HardDrop, KeyEvent.VK_W);
            setupKeyAction.accept(GameplayButton.SoftDrop, KeyEvent.VK_S);
            setupKeyAction.accept(GameplayButton.Hold, KeyEvent.VK_F);
            setupKeyAction.accept(GameplayButton.RotateCCW, KeyEvent.VK_R);
            setupKeyAction.accept(GameplayButton.RotateFlip, KeyEvent.VK_T);
            setupKeyAction.accept(GameplayButton.RotateCW, KeyEvent.VK_Y);

            setupKeyAction.accept(GameplayButton.Hold, KeyEvent.VK_CAPS_LOCK);
            setupKeyAction.accept(GameplayButton.RotateCCW, KeyEvent.VK_SLASH);
            setupKeyAction.accept(GameplayButton.RotateFlip, KeyEvent.VK_OPEN_BRACKET);
            setupKeyAction.accept(GameplayButton.RotateCW, KeyEvent.VK_CLOSE_BRACKET);

        }

        class ButtonAction extends AbstractAction {
            private GameplayButton gameplayButton;
            private boolean onRelease;

            public ButtonAction(GameplayButton gameplayButton, boolean onRelease) {
                this.gameplayButton = gameplayButton;
                this.onRelease = onRelease;
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                freshInput.put(gameplayButton, !onRelease);
            }

        }

        @Override
        public boolean getHardDrop() {
            return lockInput.get(GameplayButton.HardDrop);
        }

        @Override
        public boolean getHold() {
            return lockInput.get(GameplayButton.Hold);
        }

        @Override
        public boolean getLeft() {
            return lockInput.get(GameplayButton.Left);
        }

        @Override
        public boolean getRight() {
            return lockInput.get(GameplayButton.Right);
        }

        @Override
        public boolean getRotateCCW() {
            return lockInput.get(GameplayButton.RotateCCW);
        }

        @Override
        public boolean getRotateCW() {
            return lockInput.get(GameplayButton.RotateCW);
        }

        @Override
        public boolean getRotateFlip() {
            return lockInput.get(GameplayButton.RotateFlip);
        }

        @Override
        public boolean getSoftDrop() {
            return lockInput.get(GameplayButton.SoftDrop);
        }

        @Override
        public void update() {
            lockInput.putAll(freshInput);
        }

    }

    public void setNewGameAction(ActionListener a) {
        newGameButton.addActionListener(a);
    }
}
