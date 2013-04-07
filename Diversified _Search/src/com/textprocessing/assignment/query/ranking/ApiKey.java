package com.textprocessing.assignment.query.ranking;
import java.io.Serializable;

public class ApiKey implements Serializable
{
	private String apiKey;
	private String cx;
	private int remainingQuota;
	
	public ApiKey(String a, String c)
	{
		apiKey = a;
		cx = c;
		remainingQuota = 100;
	}
	
	public String getApi()
	{
		return apiKey;
	}
	
	public String getCx()
	{
		return cx;
	}
	
	public int getRemainingQuota()
	{
		return remainingQuota;
	}
	
	public void decrementQuota()
	{
		remainingQuota = Math.max(0, remainingQuota - 1);
	}
	
	public void setQuota(int q)
	{
		remainingQuota = q;
	}
}