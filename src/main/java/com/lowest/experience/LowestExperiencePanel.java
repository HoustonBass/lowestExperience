package com.lowest.experience;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Skill;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;

@Slf4j
public class LowestExperiencePanel extends PluginPanel {

    private final LowestExperienceConfig config;
    private Skill selectedSkill;
    private final JComboBox<Skill[]> skillDropdown;
    private final int DROPDOWN_HEIGHT = 20;
    private final JPanel listContainer = new JPanel();
    private List<SkillRow> rows = new ArrayList<>();
    private final Map<Skill, SkillRow> rowMap = new EnumMap<>(Skill.class);
    private EnumMap<Skill, Integer> xpMap;

    private static final Skill[] LOWEST_ORDER = new Skill[]{
            Skill.RUNECRAFT,
            Skill.AGILITY,
            Skill.SMITHING,
            Skill.CONSTRUCTION,
            Skill.MINING,
            Skill.HERBLORE,
            Skill.HUNTER,
            Skill.CRAFTING,
            Skill.FARMING,
            Skill.SLAYER,
            Skill.PRAYER,
            Skill.FISHING,
            Skill.THIEVING,
            Skill.WOODCUTTING,
            Skill.FIREMAKING,
            Skill.FLETCHING,
            Skill.MAGIC,
            Skill.DEFENCE,
            Skill.ATTACK,
            Skill.COOKING,
            Skill.RANGED,
            Skill.HITPOINTS,
            Skill.STRENGTH
    };

    public LowestExperiencePanel(LowestExperienceConfig config) {
        this.config = config;
        selectedSkill = config.defaultSkill();
        skillDropdown = makeNewDropdown();

        getParent().setLayout(new BorderLayout());
        getParent().add(this, BorderLayout.CENTER);

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(ColorScheme.DARK_GRAY_COLOR);

        add(levelSelectContainer(), BorderLayout.NORTH);
        add(skillOrderContainer(), BorderLayout.CENTER);

        selectSkill(selectedSkill);
    }

    private JPanel skillOrderContainer() {
        listContainer.setLayout(new GridLayout(0, 1));
        listContainer.setBackground(ColorScheme.LIGHT_GRAY_COLOR.darker());
        Arrays.stream(Skill.values())
                .forEach(skill -> {
                    SkillRow row = new SkillRow(skill, 0, config.displayCurrentXp(), config.displayXpDifference());
                    rows.add(row);
                    rowMap.put(skill, row);
                    listContainer.add(row);
        });
        return listContainer;
    }

    public void updateSkillXp() {
        xpMap.forEach((key, value) -> {
            if(rowMap.containsKey(key)) {
                rowMap.get(key).setXpForSkill(value);
            }
        });
        listContainer.removeAll();
        rows = rows.stream()
                .sorted(Comparator.comparing(SkillRow::getXpForSkill, Integer::compare))
                .collect(Collectors.toList());
        Integer currentXp = xpMap.get(selectedSkill);
        rows.forEach(row -> {
            row.computeDifference(currentXp);
            listContainer.add(row);
        });
        listContainer.revalidate();
        listContainer.repaint();
    }

    private JPanel levelSelectContainer() {
        JPanel levelsContainer = new JPanel();
        levelsContainer.setLayout(new BorderLayout());

        levelsContainer.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel filtersPanel = makeDropdownPanel(skillDropdown);
        filtersPanel.setPreferredSize(new Dimension(PANEL_WIDTH, DROPDOWN_HEIGHT));

        levelsContainer.add(filtersPanel, BorderLayout.NORTH);
        return levelsContainer;
    }

    private JComboBox<Skill[]> makeNewDropdown() {
        JComboBox<Skill[]> dropdown = new JComboBox(LOWEST_ORDER);
        dropdown.setFocusable(false);
        for(int i=0; i < LOWEST_ORDER.length; i++) {
            if((LOWEST_ORDER[i]).equals(selectedSkill)) {
                dropdown.setSelectedIndex(i);
                break;
            }
        }
        dropdown.setForeground(Color.WHITE);
        dropdown.setRenderer(new DropDownRenderer());
        dropdown.addItemListener(e ->
        {
            if (e.getStateChange() == ItemEvent.SELECTED)
            {
                Skill skill = (Skill) e.getItem();
                unselectSkill(selectedSkill);
                selectedSkill = skill;
                selectSkill(selectedSkill);
                updateSkillXp();
            }
        });

        return dropdown;
    }

    private void unselectSkill(Skill skill) {
        rowMap.get(skill).unselect();
    }

    private void selectSkill(Skill skill) {
        rowMap.get(skill).select();
    }

    private JPanel makeDropdownPanel(JComboBox<Skill[]> dropdown) {
        JLabel filterName = new JLabel("Skills");
        filterName.setForeground(Color.WHITE);

        JPanel filtersPanel = new JPanel();
        filtersPanel.setLayout(new BorderLayout());
        filtersPanel.setMinimumSize(new Dimension(PANEL_WIDTH, 0));
        filtersPanel.add(filterName, BorderLayout.CENTER);
        filtersPanel.add(dropdown, BorderLayout.EAST);

        return filtersPanel;
    }

    public void setCurrentXpMap(Map<Skill, Integer> currentXpMap) {
        xpMap = (EnumMap<Skill, Integer>) currentXpMap;
        updateSkillXp();
    }
}
