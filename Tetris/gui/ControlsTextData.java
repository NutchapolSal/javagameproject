package Tetris.gui;

import Tetris.settings.ControlScheme;

class ControlsTextData {
    public final String button;
    public final String name;

    private ControlsTextData(String button, String name) {
        this.button = button;
        this.name = name;
    }

    public static ControlsTextData[] getControlsTexts(int playerIndex, ControlScheme controlScheme, boolean sonicDrop) {
        String sonicDropText = (sonicDrop ? "Sonic" : "Soft") + " Drop";

        ControlsTextData[] ctds;
        switch (controlScheme) {
            case WASD:
                switch (playerIndex) {
                    default:
                        ctds = new ControlsTextData[] {
                                new ControlsTextData("A D", "Move"),
                                new ControlsTextData("S", sonicDropText),
                                new ControlsTextData("W", "Hard Drop"),
                                new ControlsTextData("R", "Rotate"),
                                new ControlsTextData("F", "Hold"),
                        };
                        break;
                    case 1:
                        ctds = new ControlsTextData[] {
                                new ControlsTextData("J L", "Move"),
                                new ControlsTextData("K", sonicDropText),
                                new ControlsTextData("I", "Hard Drop"),
                                new ControlsTextData("P", "Rotate"),
                                new ControlsTextData(";", "Hold"),
                        };
                        break;
                }
                break;
            case Classic:
                switch (playerIndex) {
                    default:
                        ctds = new ControlsTextData[] {
                                new ControlsTextData("C B", "Move"),
                                new ControlsTextData("V", sonicDropText),
                                new ControlsTextData("F", "Hard Drop"),
                                new ControlsTextData("A", "Rotate CCW"),
                                new ControlsTextData("S", "Rotate CW"),
                                new ControlsTextData("D", "Hold"),
                        };
                        break;
                    case 1:
                        ctds = new ControlsTextData[] {
                                new ControlsTextData("⬅ ➡", "Move"),
                                new ControlsTextData("⬇", sonicDropText),
                                new ControlsTextData("⬆", "Hard Drop"),
                                new ControlsTextData(",", "Rotate CCW"),
                                new ControlsTextData(".", "Rotate CW"),
                                new ControlsTextData("/", "Hold"),
                        };
                        break;
                }

                break;
            case SlashBracket:
                switch (playerIndex) {
                    default:
                        ctds = new ControlsTextData[] {
                                new ControlsTextData("W D", "Move"),
                                new ControlsTextData("S", sonicDropText),
                                new ControlsTextData("E", "Hard Drop"),
                                new ControlsTextData("C", "Rotate CCW"),
                                new ControlsTextData("F", "Rotate Flip"),
                                new ControlsTextData("G", "Rotate CW"),
                                new ControlsTextData("Q", "Hold"),
                        };
                        break;
                    case 1:
                        ctds = new ControlsTextData[] {
                                new ControlsTextData("K ;", "Move"),
                                new ControlsTextData("L", sonicDropText),
                                new ControlsTextData("O", "Hard Drop"),
                                new ControlsTextData("'", "Rotate CCW"),
                                new ControlsTextData("-", "Rotate Flip"),
                                new ControlsTextData("=", "Rotate CW"),
                                new ControlsTextData("M", "Hold"),
                        };
                        break;
                }

                break;
            default:
                ctds = new ControlsTextData[0];
                break;
        }

        return ctds;
    }
}