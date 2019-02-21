package com.ygsoft.matcloud.piloader.scheduled;

import java.util.List;

public class PiPointFactory {
	private String piUrl = "";
	private String piUid = "";
	private String piPwd = "";
	private String targetUrl;
	private String urlTop = "http://";
	private String resultName;
	private String showPointName = "false";
	private String orgName;

	private List<PointGroup> pointGroups;

	public String getPiUrl() {
		return this.piUrl;
	}

	public void setPiUrl(String piUrl) {
		this.piUrl = piUrl;
	}

	public String getPiUid() {
		return this.piUid;
	}

	public void setPiUid(String piUid) {
		this.piUid = piUid;
	}

	public String getPiPwd() {
		return this.piPwd;
	}

	public void setPiPwd(String piPwd) {
		this.piPwd = piPwd;
	}

	public String getTargetUrl() {
		return this.targetUrl;
	}

	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}

	public String getUrlTop() {
		return this.urlTop;
	}

	public void setUrlTop(String urlTop) {
		this.urlTop = urlTop;
	}

	public String getResultName() {
		return this.resultName;
	}

	public void setResultName(String resultName) {
		this.resultName = resultName;
	}

	public String getShowPointName() {
		return this.showPointName;
	}

	public void setShowPointName(String showPointName) {
		this.showPointName = showPointName;
	}

	public String getOrgName() {
		return this.orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public List<PointGroup> getPointGroups() {
		return this.pointGroups;
	}

	public void setPointGroups(List<PointGroup> pointGroups) {
		this.pointGroups = pointGroups;
	}
	
	
//	public static void main(String[] args) {
//		String pStr = null;
//	    InputStream ins = Thread.currentThread().getContextClassLoader().getResourceAsStream(("pi3.json"));
//	    if (ins != null)
//	      pStr = LoadPIData.inputStreamToString(ins);
//
//	    if (pStr != null) {
//	    	ObjectMapper mapper = new ObjectMapper();
//	    	try {
//	    		PiPointFactory pf = mapper.readValue(pStr, PiPointFactory.class);
//	    		PointGroup pg = pf.getPointGroups().get(0);
//		    	List <Map<String,Object>> lm = pg.getAttrs();
//		    	for(Map<String,Object> map : lm) {
//		    		System.out.println(map.get("piName"));
//		    	}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}	    	
//	    }
//	}
}