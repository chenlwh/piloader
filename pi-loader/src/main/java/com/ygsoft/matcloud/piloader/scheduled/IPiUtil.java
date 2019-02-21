package com.ygsoft.matcloud.piloader.scheduled;

import org.xvolks.jnative.pointers.Pointer;

public abstract interface IPiUtil
{
  public abstract void connection(String paramString1, String paramString2, String paramString3);

  public abstract float getTagValue(String paramString);

  public abstract float getTagValueByTime(String paramString1, String paramString2);

  public abstract float getTagMinValue(String paramString1, String paramString2, String paramString3);

  public abstract float getTagMaxValue(String paramString1, String paramString2, String paramString3);

  public abstract float getTagAvgValue(String paramString1, String paramString2, String paramString3);

  public abstract float getTagValuesBetween(String paramString1, String paramString2, String paramString3, int paramInt);

  public abstract int getPITime(String paramString);

  public abstract String getTimeFromInt(int paramInt);

  public abstract int[] getTimeSecint(int paramInt);

  public abstract Pointer getTimeIntsec(String paramString);

  public abstract int[] timeArray(String paramString);

  public abstract String getPITimeStr(String paramString);
}