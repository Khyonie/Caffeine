package coffee.khyonieheart.caffeine;

public enum TimeSetting
{
	MORNING(1200),
	MIDDAY(6000),
	EVENING(12000),
	MIDNIGHT(18000)
	;

	private final int tickTime;

	private TimeSetting(int tickTime)
	{
		this.tickTime = tickTime;
	}

	public int getTimeInTicks()
	{
		return this.tickTime;
	}
}
