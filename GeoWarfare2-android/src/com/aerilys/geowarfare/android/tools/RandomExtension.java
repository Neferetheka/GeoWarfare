package com.aerilys.geowarfare.android.tools;

import java.util.Random;

public class RandomExtension extends Random
{
	private static final long serialVersionUID = 8914228484090011478L;

	public int nextInt(int min, int max)
	{
		return min + (int)(Math.random()*max);
	}
}
