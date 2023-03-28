package Tetris.input;

public class PlayerInput {
    private int DAS_CHARGE_FRAMES = 9;
    private int AUTO_REPEAT_FRAMES = 2;

    private int dasChargeLeft;
    private int dasChargeRight;
    private int autoRepeatFrame;
    private int xMove;
    private boolean hardDrop = false;
    private boolean softDrop = false;
    private Rotation rotation = Rotation.None;
    private boolean hold = false;
    private RawInputSource inputSource;
    private char prevInput = ' ';

    public PlayerInput(RawInputSource inputSource) {
        this.inputSource = inputSource;
        this.dasChargeLeft = DAS_CHARGE_FRAMES;
        this.dasChargeRight = DAS_CHARGE_FRAMES;
        this.autoRepeatFrame = AUTO_REPEAT_FRAMES;
    }

    public PlayerInput() {
        this.inputSource = null;
        this.dasChargeLeft = DAS_CHARGE_FRAMES;
        this.dasChargeRight = DAS_CHARGE_FRAMES;
        this.autoRepeatFrame = AUTO_REPEAT_FRAMES;
    }

    public void setRawInputSource(RawInputSource inputSource) {
        this.inputSource = inputSource;
    }

    public void tick() {
        boolean lastHardDrop = inputSource.getHardDrop();
        boolean lastHold = inputSource.getHold();
        boolean lastRotationCW = inputSource.getRotateCW();
        boolean lastRotationCCW = inputSource.getRotateCCW();
        boolean lastRotationFlip = inputSource.getRotateFlip();

        inputSource.update();

        xMove = 0;

        if (inputSource.getLeft() && !inputSource.getRight()) {
            prevInput = 'L';
            dasChargeRight = DAS_CHARGE_FRAMES;
            if (dasChargeLeft == DAS_CHARGE_FRAMES) {
                xMove = -1;
            }
            if (dasChargeLeft > 0) {
                dasChargeLeft--;
            } else {
                if (autoRepeatFrame == AUTO_REPEAT_FRAMES) {
                    xMove = -1;
                }
                autoRepeatFrame--;
                if (autoRepeatFrame == 0) {
                    autoRepeatFrame = AUTO_REPEAT_FRAMES;
                }
            }
        } else if (inputSource.getRight() && !inputSource.getLeft()) {
            prevInput = 'R';
            dasChargeLeft = DAS_CHARGE_FRAMES;
            if (dasChargeRight == DAS_CHARGE_FRAMES) {
                xMove = 1;
            }
            if (dasChargeRight > 0) {
                dasChargeRight--;
            } else {
                if (autoRepeatFrame == AUTO_REPEAT_FRAMES) {
                    xMove = 1;
                }
                autoRepeatFrame--;
                if (autoRepeatFrame == 0) {
                    autoRepeatFrame = AUTO_REPEAT_FRAMES;
                }
            }
        } else if (prevInput == 'L' && inputSource.getRight()) {
            if (dasChargeRight == DAS_CHARGE_FRAMES) {
                xMove = 1;
            }
            if (dasChargeRight > 0) {
                dasChargeRight--;
            } else {
                if (autoRepeatFrame == AUTO_REPEAT_FRAMES) {
                    xMove = 1;
                }
                autoRepeatFrame--;
                if (autoRepeatFrame == 0) {
                    autoRepeatFrame = AUTO_REPEAT_FRAMES;
                }
            }
        } else if (prevInput == 'R' && inputSource.getLeft()) {
            if (dasChargeLeft == DAS_CHARGE_FRAMES) {
                xMove = -1;
            }
            if (dasChargeLeft > 0) {
                dasChargeLeft--;
            } else {
                if (autoRepeatFrame == AUTO_REPEAT_FRAMES) {
                    xMove = -1;
                }
                autoRepeatFrame--;
                if (autoRepeatFrame == 0) {
                    autoRepeatFrame = AUTO_REPEAT_FRAMES;
                }
            }
        } else {
            prevInput = ' ';
            dasChargeLeft = DAS_CHARGE_FRAMES;
            dasChargeRight = DAS_CHARGE_FRAMES;
        }

        hold = inputSource.getHold() && !lastHold;
        hardDrop = inputSource.getHardDrop() && !lastHardDrop;
        softDrop = inputSource.getSoftDrop();

        rotation = Rotation.None;
        if (inputSource.getRotateCW() && !lastRotationCW) {
            rotation = Rotation.Clockwise;
        }

        if (inputSource.getRotateCCW() && !lastRotationCCW) {
            rotation = Rotation.CounterClockwise;
        }

        if (inputSource.getRotateFlip() && !lastRotationFlip) {
            rotation = Rotation.Flip;
        }

        // System.out.printf("%2d", getXMove());
        // System.out.print(getHardDrop() ? " V " : " ");
        // System.out.print(getSoftDrop() ? "v " : " ");
        // System.out.print(getHold() ? "H " : " ");
        // System.out.println(getRotation());
    }

    public int getXMove() {
        return xMove;
    }

    public boolean getHardDrop() {
        return hardDrop;
    }

    public boolean getSoftDrop() {
        return softDrop;
    }

    public Rotation getRotation() {
        return rotation;
    }

    public boolean getHold() {
        return hold;
    }

    public void setDAS(int das) {
        this.DAS_CHARGE_FRAMES = das;
        this.dasChargeLeft = DAS_CHARGE_FRAMES;
        this.dasChargeRight = DAS_CHARGE_FRAMES;
    }

    public void setARR(int arr) {
        this.AUTO_REPEAT_FRAMES = arr;
        this.autoRepeatFrame = AUTO_REPEAT_FRAMES;
    }
}
