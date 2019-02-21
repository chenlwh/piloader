package com.ygsoft.matcloud.piloader.scheduled;

import java.util.List;

public abstract interface ILoadPIData
{
  public abstract PiPointFactory getPiPointFactory();

  public abstract List<PointGroup> getPointGroups();

  public abstract List<PointGroup> getSendPiGroupData();

  public abstract void loadAndsendPIData();
}