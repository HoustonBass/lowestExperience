package com.lowestExperience;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.lowestExperience.LowestExperienceConfig.FilterOrder;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Skill;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;

@Slf4j
public class LowestExperiencePanel extends PluginPanel {

    private final LowestExperienceConfig config;
    private Skill selectedSkill;
    private final JComboBox<Enum<FilterOrder>> skillDropdown;
    private final int DROPDOWN_HEIGHT = 20;
    private final JPanel listContainer = new JPanel();
    private List<SkillRow> rows = new ArrayList<>();
    private final Map<Skill, SkillRow> rowMap = new EnumMap<>(Skill.class);


    public LowestExperiencePanel(LowestExperienceConfig config) {
        this.config = config;
        selectedSkill = config.defaultSkill().getSkill();
        skillDropdown = makeNewDropdown(FilterOrder.values());

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
        for(Skill skill : Skill.values()) {
            if(Skill.OVERALL.equals(skill)) {
                continue;
            }
            SkillRow row = new SkillRow(skill, 0, config.displayCurrentXp(), config.displayXpDifference());
            rows.add(row);
            rowMap.put(skill, row);
        }
        for(SkillRow row : rows) {
            listContainer.add(row);
        }
        return listContainer;
    }

    public void updateSkillXp(List<Map.Entry<Skill, Integer>> skillOrder) {
        for(Map.Entry<Skill, Integer> entry : skillOrder) {
            rowMap.get(entry.getKey()).setXpForSkill(entry.getValue());
        }
        listContainer.removeAll();
        rows = rows.stream()
                .sorted(Comparator.comparing(SkillRow::getXpForSkill, Integer::compare))
                .collect(Collectors.toList());
        SkillRow lowestSkill = rows.get(0);
        for(SkillRow row : rows) {
            if(lowestSkill.equals(row)) {
                continue;
            }
            listContainer.add(row);
            row.computeDifference(lowestSkill.getXpForSkill());
        }
    }

    private JPanel levelSelectContainer() {
        JPanel levelsContainer = new JPanel();
        levelsContainer.setLayout(new BorderLayout());

        levelsContainer.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel filtersPanel = makeDropdownPanel(skillDropdown, "Skill");
        filtersPanel.setPreferredSize(new Dimension(PANEL_WIDTH, DROPDOWN_HEIGHT));

        levelsContainer.add(filtersPanel, BorderLayout.NORTH);
        return levelsContainer;
    }

    private JComboBox<Enum<FilterOrder>> makeNewDropdown(Enum<FilterOrder>[] values) {
        JComboBox<Enum<FilterOrder>> dropdown = new JComboBox<>(values);
        dropdown.setFocusable(false);
        for(int i=0; i < values.length; i++) {
            if(((FilterOrder) values[i]).getSkill().equals(selectedSkill)) {
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
                FilterOrder skill = (FilterOrder) e.getItem();
                unselectSkill(selectedSkill);
                selectedSkill = skill.getSkill();
                selectSkill(selectedSkill);
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

    private JPanel makeDropdownPanel(JComboBox dropdown, String name) {
        JLabel filterName = new JLabel(name);
        filterName.setForeground(Color.WHITE);

        JPanel filtersPanel = new JPanel();
        filtersPanel.setLayout(new BorderLayout());
        filtersPanel.setMinimumSize(new Dimension(PANEL_WIDTH, 0));
        filtersPanel.add(filterName, BorderLayout.CENTER);
        filtersPanel.add(dropdown, BorderLayout.EAST);

        return filtersPanel;
    }
}
