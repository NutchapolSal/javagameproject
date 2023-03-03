package Tetris;

import javax.swing.JLabel;

import javax.swing.Timer;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;
import java.awt.font.TextAttribute;
import java.awt.font.TransformAttribute;
import java.awt.geom.AffineTransform;

public class CalloutLabel extends JLabel {
    public void startAnimation() {
        JLabel label = new JLabel();
        Timer timer = new Timer(1, new ActionListener() {
            private double spacing = -0.5;
            private double scale = 1.0;
            private float alpha = 1.0f;

            @Override
            public void actionPerformed(ActionEvent e) {
                Map<TextAttribute, Object> attribute = new HashMap<>();
                attribute.put(TextAttribute.TRACKING, spacing);
                // attribute.put(TextAttribute.TRANSFORM,
                // new TransformAttribute(AffineTransform.getScaleInstance(scale, scale)));
                // Scale-up
                spacing += (0.1 - spacing) / 30;
                if (spacing >= 0.1) {
                    ((Timer) e.getSource()).stop();
                }
                scale *= 1.25;
                label.setFont(label.getFont().deriveFont(attribute));

                // Fade-out
                alpha -= 0.02f;
                if (alpha < 0) {
                    alpha = 0;
                }
                label.setForeground(new Color(label.getForeground().getRed(),
                        label.getForeground().getGreen(),
                        label.getForeground().getBlue(),
                        (int) (255 * alpha)));

            }
        });
        timer.start();
    }
}
