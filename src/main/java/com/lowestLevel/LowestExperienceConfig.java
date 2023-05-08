package com.lowestLevel;

import net.runelite.api.Skill;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("example")
public interface LowestExperienceConfig extends Config
{

	@ConfigItem(
			keyName = "defaultSkill",
			name = "Default Skill",
			description = "default target skill to be the lowest",
			position = 1
	)
	default FilterOrder defaultSkill()
	{
		return FilterOrder.RUNECRAFT;
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




	enum FilterOrder {
		RUNECRAFT(Skill.RUNECRAFT),
		AGILITY(Skill.AGILITY),
		HUNTER(Skill.HUNTER),
		;

		private final Skill skill;
		FilterOrder(Skill skill) {
			this.skill = skill;
		}

		public Skill getSkill() {
			return skill;
		}
	}
}
