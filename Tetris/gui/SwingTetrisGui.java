package Tetris.gui;

import Tetris.data.GoalData;
import Tetris.data.GuiData;
import Tetris.data.mino.MinoColor;
import Tetris.gameplay.goal.GoalState;
import Tetris.input.GameplayButton;
import Tetris.input.RawInputSource;
import Tetris.settings.ControlScheme;
import Tetris.settings.GameplayMode;
import Tetris.settings.ReceiveSettings;
import Tetris.settings.SendSettings;
import Tetris.settings.SettingKey;
import Tetris.settings.Settings;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;

public class SwingTetrisGui implements TetrisGui, SendSettings, ReceiveSettings {
    private JFrame f;
    private JPanel fakeContentPane;
    private JPanel centerPanel;
    private Box.Filler leftFiller;
    private PlayfieldPanel playfield;
    private Box.Filler rightFiller;
    private PlayerGroup[] playerGroups;
    private CalloutsGroup calloutsGroup;
    private StatsGroup statsGroup;
    // private JPanel miscPanel;
    private JButton newGameButton;
    // private JLabel controlsText;
    private DangerBorder dangerBorder;

    private KeyboardHandler[] kbh = new KeyboardHandler[2];
    private JMenuBar menuBar;
    private GameMenuGroup gameMenuGroup;
    private OptionsMenuGroup optionsMenuGroup;
    private ActionListener newGameAction;

    private double windowDeltaX;
    private double windowDeltaY;
    private double windowLastDeltaX;
    private double windowLastDeltaY;

    private long lastFrameTime = System.nanoTime();
    private long frameTimeAccumulator = 0;
    private int lastB2B = 0;
    private int lastCombo = 0;
    private GoalData goalData = new GoalData();
    private GoalState lastGoalState = GoalState.NONE;
    private String lastGamemodeName = "";
    // private ControlScheme controlScheme;
    // private boolean controlSchemeSonicDrop;
    private BlockSkinManager blockSkinManager = new BlockSkinManager();
    private boolean lastDanger = false;

    private static double roundToZero(double in) {
        if (in < 0) {
            return Math.ceil(in);
        } else {
            return Math.floor(in);
        }
    }

    public void update(GuiData gds) {
        long currFrameTime = System.nanoTime();
        long deltaFrameTime = currFrameTime - lastFrameTime;
        frameTimeAccumulator += deltaFrameTime;
        if (gds != null) {
            frameTimeAccumulator = Math.max(0, frameTimeAccumulator - TimeUnit.NANOSECONDS.toMillis(33));
            if (gds.spinName != null) {
                calloutsGroup.getSpinLabel()
                        .startAnimation((gds.spinMini ? "MINI " : "") + gds.spinName.toUpperCase() + "-SPIN");
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
                    calloutLinesStr = String.format("%d LINES", gds.calloutLines);
                }
                calloutsGroup.getLineCalloutLabel().startAnimation(calloutLinesStr);
            }
            if (gds.b2bCount != lastB2B) {
                if (gds.b2bCount != 0) {
                    calloutsGroup.getB2bLabel().startAnimation(String.format("B2B x%s", gds.b2bCount), false);
                    lastB2B = gds.b2bCount;
                } else {
                    calloutsGroup.getB2bLabel().startAnimation(String.format("B2B x%s", gds.b2bCount));
                    lastB2B = gds.b2bCount;
                }
            }
            if (gds.comboCount != lastCombo && 0 < gds.comboCount) {
                calloutsGroup.getComboLabel().startAnimation(String.format("%d COMBO", gds.comboCount));
            }
            lastCombo = gds.comboCount;

            if (gds.goalData != null) {
                goalData = gds.goalData;
            }

            long showMillis;
            if (goalData.isTimesGoal()) {
                showMillis = Math.max(0, goalData.getTimeMillisLength() - gds.timeMillis);
            } else {
                showMillis = gds.timeMillis;
            }

            statsGroup.getTimeCountText().setText(
                    String.format("%.0f:%05.2f", Math.floor(showMillis / (1000d * 60)),
                            (showMillis / 1000d) % 60));

            statsGroup.getScoreCountText().setText(String.format("%d", gds.score));
            String linesGoalPart = "";
            if (goalData.isLinesGoal()) {
                linesGoalPart = String.format(" / %d", goalData.getLinesCount());
            }
            statsGroup.getLinesCountText().setText(String.format("%d%s", gds.linesCleared, linesGoalPart));
            statsGroup.getLevelCountText().setText(String.format("%d", gds.level));
            if (gds.renderBlocks != null) {
                playfield.setRenderBlocks(gds.renderBlocks);
            }

            for (int i = 0; i < gds.playerGuiDatas.length; i++) {
                if (gds.playerGuiDatas[i].lockHold) {
                    playerGroups[i].getHoldGroup().getHoldMino().setMino(gds.playerGuiDatas[i].hold, MinoColor.Gray);
                } else {
                    playerGroups[i].getHoldGroup().getHoldMino().setMino(gds.playerGuiDatas[i].hold);
                }

                if (gds.playerGuiDatas[i].nextQueue == null) {
                    continue;
                }

                for (int j = 0; j < gds.playerGuiDatas[i].nextQueue.length
                        && j < playerGroups[i].getNextGroup().getNextMinos().length; j++) {
                    playerGroups[i].getNextGroup().getNextMinos()[j].setMino(gds.playerGuiDatas[i].nextQueue[j]);
                }

            }

            playfield.setPlayerGuiDatas(gds.playerGuiDatas);

            if (gds.countdown != -1) {
                if (gds.countdown == 0) {
                    calloutsGroup.getLineCalloutLabel().startAnimation("GO!");
                } else {
                    calloutsGroup.getLineCalloutLabel().startAnimation(String.format("%s", gds.countdown));
                }
            }

            if (lastGamemodeName != gds.gamemodeName) {
                playfield.startAnimation(gds.gamemodeName.toUpperCase());
            }
            lastGamemodeName = gds.gamemodeName;

            if (gds.danger != lastDanger) {
                // playfield.setBorder(BorderFactory.createLineBorder(gds.danger ? Color.RED :
                // Color.BLACK));
                // f.getContentPane().setBackground(gds.danger ? new Color(255, 0, 0, 63) : new
                // Color(0, 0, 0, 0));
                // centerPanel.setBackground(new Color(0, 0, 0, 0));
                dangerBorder.transition(gds.danger);
            }
            lastDanger = gds.danger;

            if (gds.allClear) {
                playfield.startAnimation("ALL CLEAR");
            }
            if (gds.goalState != lastGoalState) {
                switch (gds.goalState) {
                    case WIN:
                        playfield.startAnimation("FINISH");
                        playfield.setPlayerOverrideColor(MinoColor.Gray);
                        calloutsGroup.getB2bLabel().doFadeOut();
                        lastB2B = 0;
                        break;
                    case LOSE:
                        playfield.startAnimation("GAME OVER");
                        playfield.setPlayerOverrideColor(MinoColor.Gray);
                        calloutsGroup.getB2bLabel().doFadeOut();
                        lastB2B = 0;
                        break;
                    default:
                        break;
                }
            }
            lastGoalState = gds.goalState;

            windowDeltaX += gds.windowNudgeX;
            windowDeltaY += gds.windowNudgeY;
        }

        if (gds != null || TimeUnit.NANOSECONDS.toMillis(33) < frameTimeAccumulator) {
            frameTimeAccumulator = 0;
            f.repaint();
        }

        windowDeltaX *= Math.pow(11.0 / 12.0, deltaFrameTime / TimeUnit.MILLISECONDS.toNanos(10));
        windowDeltaY *= Math.pow(11.0 / 12.0, deltaFrameTime / TimeUnit.MILLISECONDS.toNanos(10));

        int windowVelocityX = (int) roundToZero(windowDeltaX - windowLastDeltaX);
        int windowVelocityY = (int) roundToZero(windowDeltaY - windowLastDeltaY);

        windowLastDeltaX += windowVelocityX;
        windowLastDeltaY += windowVelocityY;

        if (windowVelocityX != 0 || windowVelocityY != 0) {
            var frameLoc = f.getLocationOnScreen();
            f.setLocation(frameLoc.x + windowVelocityX, frameLoc.y + windowVelocityY);
        }

        lastFrameTime = currFrameTime;
    }

    public SwingTetrisGui() {
        setLookAndFeel();
        f = new JFrame("Tetris");
        f.setSize(800, 600);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        detailComponents();

        getKeyboardHandler(0).setupWASDP1();
        getKeyboardHandler(1).setupWASDP2();

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
        createMenu();
        f.setJMenuBar(menuBar);

        fakeContentPane = new JPanel();
        leftFiller = new Box.Filler(
                new Dimension(0, 0),
                new Dimension(0, 0),
                new Dimension(32767, 0));
        createCenterPanel();
        rightFiller = new Box.Filler(
                new Dimension(0, 0),
                new Dimension(0, 0),
                new Dimension(32767, 0));
        dangerBorder = new DangerBorder(5);

        fakeContentPane.setLayout(new BoxLayout(fakeContentPane, BoxLayout.X_AXIS));
        fakeContentPane.add(leftFiller);
        fakeContentPane.add(centerPanel);
        fakeContentPane.add(rightFiller);
        fakeContentPane.setBorder(dangerBorder);

        Container contentPane = f.getContentPane();
        contentPane.add(fakeContentPane);

        // debugWithBorder();
    }

    private void createMenu() {
        menuBar = new JMenuBar();

        gameMenuGroup = new GameMenuGroup();
        optionsMenuGroup = new OptionsMenuGroup();

        menuBar.add(gameMenuGroup.getMenu());
        menuBar.add(optionsMenuGroup.getMenu());
    }

    private void createCenterPanel() {
        centerPanel = new JPanel();
        playfield = new PlayfieldPanel(blockSkinManager);
        statsGroup = new StatsGroup();
        playerGroups = new PlayerGroup[] {
                new PlayerGroup(0, blockSkinManager, Alignment.TRAILING),
                new PlayerGroup(1, blockSkinManager, Alignment.LEADING)
        };
        calloutsGroup = new CalloutsGroup();

        newGameButton = new JButton();

        newGameButton.setFocusable(false);
        newGameButton.setText("New Game");

        GroupLayout centerPanelLayout = new GroupLayout(centerPanel);
        centerPanel.setLayout(centerPanelLayout);
        centerPanelLayout.setHorizontalGroup(centerPanelLayout.createSequentialGroup()
                .addGroup(centerPanelLayout.createParallelGroup(Alignment.TRAILING)
                        .addComponent(playerGroups[0].getPanel())
                        .addComponent(statsGroup.getPanel()))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(centerPanelLayout.createParallelGroup(Alignment.CENTER)
                        .addComponent(calloutsGroup.getPanel())
                        .addComponent(playfield))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(centerPanelLayout.createParallelGroup()
                        .addComponent(playerGroups[1].getPanel())
                        .addComponent(newGameButton)

                ));
        centerPanelLayout.setVerticalGroup(centerPanelLayout.createSequentialGroup()
                .addComponent(calloutsGroup.getPanel())
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(centerPanelLayout.createParallelGroup(Alignment.LEADING, false)
                        .addGroup(centerPanelLayout.createSequentialGroup()
                                .addComponent(playerGroups[0].getPanel())
                                .addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE,
                                        Integer.MAX_VALUE)
                                .addComponent(statsGroup.getPanel()))

                        .addComponent(playfield)
                        .addGroup(centerPanelLayout.createSequentialGroup()
                                .addComponent(playerGroups[1].getPanel())
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(newGameButton)

                        )));
    }

    public KeyboardHandler getKeyboardHandler(int index) {
        if (kbh[index] == null) {
            kbh[index] = new KeyboardHandler(index);
        }
        return kbh[index];
    }

    class KeyboardHandler implements RawInputSource, ReceiveSettings {
        static final String PRESSED = "pressed";
        static final String RELEASED = "released";
        final int playerIndex;
        private Map<GameplayButton, Boolean> freshInput = new EnumMap<>(GameplayButton.class);
        private Map<GameplayButton, Boolean> lockInput = new EnumMap<>(GameplayButton.class);

        private final InputMap inputMap = SwingTetrisGui.this.f.getRootPane().getInputMap();

        private void setupKeyAction(GameplayButton gb, int keyCode) {
            inputMap.put(KeyStroke.getKeyStroke(keyCode, 0, false),
                    gb.name() + PRESSED + Integer.toString(playerIndex));
            inputMap.put(KeyStroke.getKeyStroke(keyCode, 0, true),
                    gb.name() + RELEASED + Integer.toString(playerIndex));
            inputMap.put(KeyStroke.getKeyStroke(keyCode, KeyEvent.SHIFT_DOWN_MASK, false),
                    gb.name() + PRESSED + Integer.toString(playerIndex));
            inputMap.put(KeyStroke.getKeyStroke(keyCode, KeyEvent.SHIFT_DOWN_MASK, true),
                    gb.name() + RELEASED + Integer.toString(playerIndex));
        }

        private void cleanupKeyActions() {
            if (inputMap.keys() == null) {
                return;
            }
            for (var k : inputMap.keys()) {
                var v = inputMap.get(k);
                if (((String) v).endsWith(Integer.toString(playerIndex))) {
                    inputMap.put(k, null);
                }
            }
        }

        private KeyboardHandler(int playerIndex) {
            this.playerIndex = playerIndex;
            ActionMap actionMap = SwingTetrisGui.this.f.getRootPane().getActionMap();

            for (var v : GameplayButton.values()) {
                freshInput.put(v, false);
            }
            update();

            for (GameplayButton gb : GameplayButton.values()) {
                actionMap.put(gb.name() + PRESSED + Integer.toString(playerIndex), new ButtonAction(gb, false));
                actionMap.put(gb.name() + RELEASED + Integer.toString(playerIndex), new ButtonAction(gb, true));
            }
        }

        private void setupWASDP1() {
            cleanupKeyActions();
            setupKeyAction(GameplayButton.Left, KeyEvent.VK_A);
            setupKeyAction(GameplayButton.Right, KeyEvent.VK_D);
            setupKeyAction(GameplayButton.HardDrop, KeyEvent.VK_W);
            setupKeyAction(GameplayButton.SoftDrop, KeyEvent.VK_S);
            setupKeyAction(GameplayButton.Hold, KeyEvent.VK_F);
            setupKeyAction(GameplayButton.RotateCCW, KeyEvent.VK_R);
        }

        private void setupWASDP2() {
            cleanupKeyActions();
            setupKeyAction(GameplayButton.Left, KeyEvent.VK_J);
            setupKeyAction(GameplayButton.Right, KeyEvent.VK_L);
            setupKeyAction(GameplayButton.HardDrop, KeyEvent.VK_I);
            setupKeyAction(GameplayButton.SoftDrop, KeyEvent.VK_K);
            setupKeyAction(GameplayButton.Hold, KeyEvent.VK_COLON);
            setupKeyAction(GameplayButton.RotateCCW, KeyEvent.VK_P);
        }

        private void setupClassicP1() {
            cleanupKeyActions();
            setupKeyAction(GameplayButton.Left, KeyEvent.VK_C);
            setupKeyAction(GameplayButton.Right, KeyEvent.VK_B);
            setupKeyAction(GameplayButton.HardDrop, KeyEvent.VK_F);
            setupKeyAction(GameplayButton.SoftDrop, KeyEvent.VK_V);
            setupKeyAction(GameplayButton.Hold, KeyEvent.VK_D);
            setupKeyAction(GameplayButton.RotateCCW, KeyEvent.VK_A);
            setupKeyAction(GameplayButton.RotateCW, KeyEvent.VK_S);
        }

        private void setupClassicP2() {
            cleanupKeyActions();
            setupKeyAction(GameplayButton.Left, KeyEvent.VK_LEFT);
            setupKeyAction(GameplayButton.Right, KeyEvent.VK_RIGHT);
            setupKeyAction(GameplayButton.HardDrop, KeyEvent.VK_UP);
            setupKeyAction(GameplayButton.SoftDrop, KeyEvent.VK_DOWN);
            setupKeyAction(GameplayButton.Hold, KeyEvent.VK_SLASH);
            setupKeyAction(GameplayButton.RotateCCW, KeyEvent.VK_COMMA);
            setupKeyAction(GameplayButton.RotateCW, KeyEvent.VK_PERIOD);
        }

        private void setupSlashBracketP1() {
            cleanupKeyActions();
            setupKeyAction(GameplayButton.Left, KeyEvent.VK_W);
            setupKeyAction(GameplayButton.Right, KeyEvent.VK_D);
            setupKeyAction(GameplayButton.HardDrop, KeyEvent.VK_E);
            setupKeyAction(GameplayButton.SoftDrop, KeyEvent.VK_S);
            setupKeyAction(GameplayButton.Hold, KeyEvent.VK_Q);
            setupKeyAction(GameplayButton.RotateCCW, KeyEvent.VK_C);
            setupKeyAction(GameplayButton.RotateFlip, KeyEvent.VK_F);
            setupKeyAction(GameplayButton.RotateCW, KeyEvent.VK_G);
        }

        private void setupSlashBracketP2() {
            cleanupKeyActions();
            setupKeyAction(GameplayButton.Left, KeyEvent.VK_K);
            setupKeyAction(GameplayButton.Right, KeyEvent.VK_SEMICOLON);
            setupKeyAction(GameplayButton.HardDrop, KeyEvent.VK_O);
            setupKeyAction(GameplayButton.SoftDrop, KeyEvent.VK_L);
            setupKeyAction(GameplayButton.Hold, KeyEvent.VK_M);
            setupKeyAction(GameplayButton.RotateCCW, KeyEvent.VK_QUOTE);
            setupKeyAction(GameplayButton.RotateFlip, KeyEvent.VK_MINUS);
            setupKeyAction(GameplayButton.RotateCW, KeyEvent.VK_EQUALS);
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

        public Map<SettingKey, Consumer<Object>> getReceivers() {
            Map<SettingKey, Consumer<Object>> receiversMap = new EnumMap<>(SettingKey.class);
            if (playerIndex == 0) {
                receiversMap.put(SettingKey.ControlScheme, x -> {
                    switch ((ControlScheme) x) {
                        case WASD:
                            setupWASDP1();
                            break;
                        case Classic:
                            setupClassicP1();
                            break;
                        case SlashBracket:
                            setupSlashBracketP1();
                            break;
                    }
                });
            } else {
                receiversMap.put(SettingKey.ControlSchemeP2, x -> {
                    switch ((ControlScheme) x) {
                        case WASD:
                            setupWASDP2();
                            break;
                        case Classic:
                            setupClassicP2();
                            break;
                        case SlashBracket:
                            setupSlashBracketP2();
                            break;
                    }
                });
            }
            return receiversMap;
        }

    }

    public void setNewGameAction(ActionListener a) {
        newGameButton.addActionListener(a);
        gameMenuGroup.getNewGameMenuItem().addActionListener(a);
        newGameAction = a;
    }

    public Map<SettingKey, Consumer<Object>> getReceivers() {
        Map<SettingKey, Consumer<Object>> receiversMap = new EnumMap<>(SettingKey.class);
        receiversMap.put(SettingKey.ControlScheme, x -> {
            playerGroups[0].setControlScheme((ControlScheme) x);
        });
        receiversMap.put(SettingKey.ControlSchemeP2, x -> {
            playerGroups[1].setControlScheme((ControlScheme) x);
        });
        receiversMap.put(SettingKey.SonicDrop, x -> {
            playerGroups[0].setSonicDrop((Boolean) x);
        });
        receiversMap.put(SettingKey.SonicDropP2, x -> {
            playerGroups[1].setSonicDrop((Boolean) x);
        });
        return receiversMap;
    }

    public void bindToSettings(Settings s) {
        gameMenuGroup.updateToSettings(s);
        bindSelectModeMenuItems(s);

        optionsMenuGroup.bindToSettings(s);
    }

    private void bindSelectModeMenuItems(Settings s) {
        gameMenuGroup.getMarathonModeMenuItem().addItemListener(evt -> {
            if (evt.getStateChange() == ItemEvent.SELECTED) {
                s.setGameplayMode(GameplayMode.Marathon);
                newGameAction.actionPerformed(null);
            }
        });
        gameMenuGroup.getSprintModeMenuItem().addItemListener(evt -> {
            if (evt.getStateChange() == ItemEvent.SELECTED) {
                s.setGameplayMode(GameplayMode.Sprint);
                newGameAction.actionPerformed(null);
            }
        });
        gameMenuGroup.getUltraModeMenuItem().addItemListener(evt -> {
            if (evt.getStateChange() == ItemEvent.SELECTED) {
                s.setGameplayMode(GameplayMode.Ultra);
                newGameAction.actionPerformed(null);
            }
        });
        gameMenuGroup.getZenModeMenuItem().addItemListener(evt -> {
            if (evt.getStateChange() == ItemEvent.SELECTED) {
                s.setGameplayMode(GameplayMode.Zen);
                newGameAction.actionPerformed(null);
            }
        });
    }

    private void debugWithBorder() {
        recurseAddBorders(f.getContentPane());
    }

    private void recurseAddBorders(Container c) {
        for (var v : c.getComponents()) {
            try {
                ((JComponent) v).setBorder(BorderFactory.createLineBorder(Color.RED));
            } catch (ClassCastException e) {
            }
            try {
                recurseAddBorders((Container) v);
            } catch (ClassCastException e) {
            }
        }
    }

    public BlockSkinManager getBlockSkinManager() {
        return blockSkinManager;
    }
}
