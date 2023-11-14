package com.lowestExperience;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import net.runelite.client.ui.ColorScheme;
import net.runelite.client.util.Text;

public final class DropDownRenderer extends DefaultListCellRenderer
{
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object o, int i, boolean isSelected, boolean b1) {
        setBackground(ColorScheme.DARK_GRAY_COLOR);
        setForeground(Color.WHITE);
        setBorder(new EmptyBorder(0, 0, 0, 0));

        if (!isSelected) {
            setBackground(ColorScheme.DARK_GRAY_COLOR);
            setForeground(Color.WHITE);
        } else {
            setBackground(list.getBackground());
            setForeground(ColorScheme.LIGHT_GRAY_COLOR);
        }

        setText(Text.titleCase((Enum) o));

        return this;
    }
}