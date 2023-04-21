package Tetris.data;

import Tetris.data.mino.Mino;

public class PlayerGuiData {
    public final Mino hold;
    public final boolean lockHold;
    public final PlayerRenderData pdr;
    public final double playerLockProgress;
    /**
     * may be null
     */
    public final Mino[] nextQueue;

    /**
     * @param nextQueue if null, will keep using old data
     */
    public PlayerGuiData(Mino hold,
            boolean lockHold,
            PlayerRenderData pdr,
            double playerLockProgress,
            Mino[] nextQueue) {
        this.hold = hold;
        this.lockHold = lockHold;
        this.pdr = pdr;
        this.playerLockProgress = playerLockProgress;
        this.nextQueue = nextQueue;
    }

}
