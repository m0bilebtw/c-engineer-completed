package com.github.dappermickie.odablock.sounds;

public abstract class TimedSoundBase
{
	TimedSoundBase(int tickDelay)
	{
		this.tickDelay = tickDelay;
	}

	private final int tickDelay;
	private int lastPlayedTick;

	void setLastPlayedTickTick(int tick)
	{
		lastPlayedTick = tick;
	}

	boolean canPlaySound(int currentTick)
	{
		return currentTick - lastPlayedTick > tickDelay || lastPlayedTick == -1;
	}

	int getLastPlayedTick()
	{
		return lastPlayedTick;
	}

	int getTickDelay()
	{
		return tickDelay;
	}

	public void cleanupTicks(int currentTick)
	{
		if (currentTick - lastPlayedTick > tickDelay)
		{
			setLastPlayedTickTick(-1);
		}
	}
}
