package com.ygsoft.matcloud.piloader.scheduled;

import java.util.List;
import java.util.Map;

public class PointGroup
{
  private String ccode;
  private String org;
  private List<Map<String, Object>> attrs;
  private String[] sendNames;

  public String getCcode()
  {
    return this.ccode; }

  public void setCcode(String ccode) {
    this.ccode = ccode; }

  public String getOrg() {
    return this.org; }

  public void setOrg(String org) {
    this.org = org; }

  public List<Map<String, Object>> getAttrs() {
    return this.attrs; }

  public void setAttrs(List<Map<String, Object>> attrs) {
    this.attrs = attrs; }

  public String[] getSendNames() {
    return this.sendNames; }

  public void setSendNames(String[] sendNames) {
    this.sendNames = sendNames;
  }
}