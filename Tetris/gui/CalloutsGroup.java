package Tetris.gui;

import java.awt.Font;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class CalloutsGroup {
    private JPanel calloutsPanel = new JPanel();
    private CalloutLabel spinLabel = new CalloutLabel();
    private CalloutLabel lineCalloutLabel = new CalloutLabel();
    private CalloutLabel comboLabel = new CalloutLabel();
    private CalloutLabel b2bLabel = new CalloutLabel();

    public CalloutsGroup() {
        detailCallOutsLabel();

        GroupLayout calloutsPanelLayout = new GroupLayout(calloutsPanel);
        calloutsPanel.setLayout(calloutsPanelLayout);
        calloutsPanelLayout.setHorizontalGroup(
                calloutsPanelLayout.createSequentialGroup()
                        .addComponent(spinLabel)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(lineCalloutLabel)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(b2bLabel)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(comboLabel));
        calloutsPanelLayout.setVerticalGroup(calloutsPanelLayout.createParallelGroup(Alignment.CENTER)
                .addComponent(spinLabel)
                .addComponent(lineCalloutLabel)
                .addComponent(b2bLabel)
                .addComponent(comboLabel));
    }

    private void detailCallOutsLabel() {
        lineCalloutLabel.setFont(lineCalloutLabel.getFont().deriveFont(
                lineCalloutLabel.getFont().getStyle() | Font.BOLD, lineCalloutLabel.getFont().getSize() + 6));
        lineCalloutLabel.setHorizontalAlignment(SwingConstants.CENTER);

        b2bLabel.setFont(b2bLabel.getFont().deriveFont(b2bLabel.getFont().getStyle() | Font.BOLD,
                b2bLabel.getFont().getSize() + 2));
        b2bLabel.setHorizontalAlignment(SwingConstants.CENTER);

        comboLabel.setFont(comboLabel.getFont().deriveFont(comboLabel.getFont().getStyle() | Font.BOLD,
                comboLabel.getFont().getSize() + 2));
        comboLabel.setHorizontalAlignment(SwingConstants.CENTER);

        spinLabel.setFont(spinLabel.getFont().deriveFont(spinLabel.getFont().getStyle() | Font.BOLD,
                spinLabel.getFont().getSize() + 2));
        spinLabel.setHorizontalAlignment(SwingConstants.CENTER);
    }

    public JPanel getPanel() {
        return calloutsPanel;
    }

    public CalloutLabel getSpinLabel() {
        return spinLabel;
    }

    public CalloutLabel getLineCalloutLabel() {
        return lineCalloutLabel;
    }

    public CalloutLabel getComboLabel() {
        return comboLabel;
    }

    public CalloutLabel getB2bLabel() {
        return b2bLabel;
    }

}
