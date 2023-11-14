package com.lowest.experience;

import net.runelite.api.Skill;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("LowestExperience")
public interface LowestExperienceConfig extends Config
{

	@ConfigItem(
			keyName = "defaultSkill",
			name = "Default Skill",
			description = "default target skill to be the lowest",
			position = 1
	)
	default Skill defaultSkill()
	{
		return Skill.RUNECRAFT;
	}

	@ConfigItem(
			keyName = "displayCurrentXp",
			name = "Display current xp",
			description = "Should the current xp column be displayed",
			position = 1
	)
	default Boolean displayCurrentXp()
	{
		return false;
	}

	@ConfigItem(
			keyName = "displayXpDifference",
			name = "Display difference",
			description = "Should the display difference between lowest xp and each xp column be displayed",
			position = 1
	)
	default Boolean displayXpDifference()
	{
		return true;
	}


}
