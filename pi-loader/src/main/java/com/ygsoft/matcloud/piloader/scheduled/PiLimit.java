package com.ygsoft.matcloud.piloader.scheduled;

import java.io.IOException;
import java.io.InputStream;

import net.sf.json.JSONObject;

public class PiLimit
{
  private Integer groupNum = Integer.valueOf(2);
  private Integer taskNum = Integer.valueOf(2);
  private Integer piNum = Integer.valueOf(10);

  public PiLimit()
  {
  }

  public PiLimit(Integer groupNum, Integer taskNum, Integer piNum)
  {
    this.groupNum = groupNum;
    this.taskNum = taskNum;
    this.piNum = piNum;
  }

  public static PiLimit getPiLimit() {
    PiLimit pl = new PiLimit();
    String pStr = null;
    InputStream ins = Thread.currentThread().getContextClassLoader().getResourceAsStream(("cfgs/limit.json"));
    if (ins != null)
      pStr = LoadPIData.inputStreamToString(ins);

    if (pStr != null) {
    	JSONObject jsonObject = JSONObject.fromObject(pStr);
    	pl = (PiLimit) JSONObject.toBean(jsonObject, pl.getClass());
    }

    if (ins != null) {
      try {
        ins.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
      ins = null;
    }
    return pl; 
}

  public Integer getGroupNum() {
    return this.groupNum; }

  public void setGroupNum(Integer groupNum) {
    this.groupNum = groupNum; }

  public Integer getTaskNum() {
    return this.taskNum; }

  public void setTaskNum(Integer taskNum) {
    this.taskNum = taskNum; }

  public Integer getPiNum() {
    return this.piNum; }

  public void setPiNum(Integer piNum) {
    this.piNum = piNum;
  }
  
}