package com.lowest.experience;

import java.awt.image.BufferedImage;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.google.inject.Provides;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Skill;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.StatChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;

@Slf4j
@PluginDescriptor(
	name = "Lowest Experience",
	description = "Easily see where you're slacking",
	tags = {"panel"}
)
public class LowestExperiencePlugin extends Plugin
{

	@Inject
	private ClientToolbar clientToolbar;

	@Inject
	private Client client;

	@Inject
	private LowestExperienceConfig config;

	private NavigationButton navButton;

	private boolean initialized = false;
	private boolean initialize = false;
	private LowestExperiencePanel panel;

	private final EnumMap<Skill, Integer> currentXpMap = new EnumMap<>(Skill.class);

	@Override
	protected void startUp() {

		panel = new LowestExperiencePanel(config);

		final BufferedImage icon = ImageUtil.loadImageResource(getClass(), "lowest_level_icon.png");

		navButton = NavigationButton.builder()
				.tooltip("Lowest Experience")
				.icon(icon)
				.panel(panel)
				.priority(11)
				.build();

		clientToolbar.addNavigation(navButton);
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event) {
		GameState state = event.getGameState();
		if (state == GameState.LOGGING_IN) {
			initialize = true;
		}
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		if (initialize && !initialized) {
			initialized = true;

			// Initialize the tracker with the initial xp if not already initialized
			for (Skill skill : Skill.values())
			{
				if (skill == Skill.OVERALL)
				{
					continue;
				}

				int currentXp = client.getSkillExperience(skill);
				currentXpMap.put(skill, currentXp);
			}
			rebuildList();
		}
	}

	@Subscribe
	public void onStatChanged(StatChanged statChanged) {
		if(initialized) {
			final Skill skill = statChanged.getSkill();
			final int currentXp = statChanged.getXp();

			if(currentXp >= 0) {
				currentXpMap.put(skill, currentXp);
			}

			rebuildList();
		}
	}

	private void rebuildList() {
		panel.setCurrentXpMap(currentXpMap);
	}

	@Override
	protected void shutDown() {
		clientToolbar.removeNavigation(navButton);
	}

	@Provides
	LowestExperienceConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(LowestExperienceConfig.class);
	}
}
