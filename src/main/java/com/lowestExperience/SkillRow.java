package com.lowestExperience;

import java.awt.*;
import java.text.NumberFormat;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Skill;
import net.runelite.client.ui.ColorScheme;

@Slf4j
public class SkillRow extends JPanel {
    private final Skill skill;
    @Getter
    private int xpForSkill;
    private int xpDifference;
    private JLabel xpLabel;
    private JLabel xpDifferenceLabel;

    private static final Color HIGHLIGHT_COLOR = ColorScheme.MEDIUM_GRAY_COLOR;
    private static final Color DEFAULT_BACKGROUND = ColorScheme.DARK_GRAY_COLOR;

    public SkillRow(Skill skill, int xpForSkill, boolean displayCurrentXp, boolean displayXpDifference) {
        this.skill = skill;
        this.xpForSkill = xpForSkill;
        this.xpDifference = 0;

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBorder(new EmptyBorder(2, 0, 2, 0));

        JPanel skillNameField = buildSkillNameField();
        skillNameField.setOpaque(false);

        JPanel xpField = buildXpNameField();
        skillNameField.setOpaque(false);

        JPanel xpDifferenceField = buildXpDifferenceField();
        xpDifferenceField.setOpaque(false);

        add(skillNameField, BorderLayout.WEST);
        if(displayCurrentXp) {
            add(xpField, BorderLayout.CENTER);
        }
        if(displayXpDifference) {
            add(xpDifferenceField, BorderLayout.EAST);
        }
    }

    private JPanel buildXpNameField() {
        JPanel column = new JPanel();
        column.setBorder(new EmptyBorder(0, 5, 0, 5));

        xpLabel = new JLabel(formattedXp(xpForSkill));

        column.add(xpLabel, BorderLayout.LINE_START);

        return column;
    }

    private JPanel buildSkillNameField() {
        JPanel column = new JPanel();
        column.setBorder(new EmptyBorder(0, 5, 0, 5));

        JLabel skillNameLabel = new JLabel(skill.getName());

        column.add(skillNameLabel, BorderLayout.LINE_START);

        return column;
    }

    private JPanel buildXpDifferenceField() {
        JPanel column = new JPanel();
        column.setBorder(new EmptyBorder(0, 5, 0, 5));

        xpDifferenceLabel = new JLabel(formattedXpDifference());

        column.add(xpDifferenceLabel, BorderLayout.LINE_END);

        return column;
    }

    private String formattedXpDifference() {
        return formattedXp(xpDifference);
    }

    public void setXpForSkill(int xpForSkill) {
        this.xpForSkill = xpForSkill;
        xpLabel.setText(formattedXp(xpForSkill));
    }

    public void computeDifference(int xpForLowestSkill) {
        this.xpDifference = xpForSkill - xpForLowestSkill;
        xpDifferenceLabel.setText(formattedXpDifference());
    }

    private String formattedXp(int xp) {
        return NumberFormat.getIntegerInstance().format(xp);
    }

    public void select() {
        setBackground(HIGHLIGHT_COLOR);
        repaint();
    }

    public void unselect() {
        setBackground(DEFAULT_BACKGROUND);
        repaint();
    }

}
