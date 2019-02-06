package net.machinemuse.numina.capabilities.player;

public class PlayerValueStorage implements IPlayerValues {
    private boolean downKeyState = false;
    private boolean jumpKeyState = false;

    @Override
    public void setDownKeyState(boolean state) {
        downKeyState = state;
    }

    @Override
    public boolean getDownKeyState() {
        return downKeyState;
    }

    @Override
    public void setJumpKeyState(boolean state) {
        jumpKeyState = state;
    }

    @Override
    public boolean getJumpKeyState() {
        return jumpKeyState;
    }
}