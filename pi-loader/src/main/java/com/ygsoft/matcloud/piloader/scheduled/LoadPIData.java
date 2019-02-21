package com.ygsoft.matcloud.piloader.scheduled;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ygsoft.matcloud.piloader.util.HttpUtil;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

@Component
public class LoadPIData implements ILoadPIData {
	private Logger log = LoggerFactory.getLogger("com.ygsoft.matcloud.piloader");
	private PiPointFactory piPointFactory;	
	private List<PointGroup> pointGroups;
	private List<PointGroup> sendPointGroups;
	
	public LoadPIData() {
		String pStr = null;
	    InputStream ins = Thread.currentThread().getContextClassLoader().getResourceAsStream(("pi.json"));
	    if (ins != null)
	      pStr = inputStreamToString(ins);

	    if (pStr != null) {
	    	try {
	    		ObjectMapper mapper = new ObjectMapper();
	    		piPointFactory = mapper.readValue(pStr, PiPointFactory.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
	    	
	    }
	}

	public PiPointFactory getPiPointFactory() {
		
		return this.piPointFactory;
	}

	public void setPiPointFactory(PiPointFactory piPointFactory) {
		this.piPointFactory = piPointFactory;
	}

	public List<PointGroup> getPointGroups() {
		if (this.pointGroups == null)
			this.pointGroups = this.piPointFactory.getPointGroups();
		return this.pointGroups;
	}

	public void setPointGroups(List<PointGroup> pointGroups) {
		this.pointGroups = pointGroups;
	}

	public List<PointGroup> getSendPointGroups() {
		return this.sendPointGroups;
	}

	public void setSendPointGroups(List<PointGroup> sendPointGroups) {
		this.sendPointGroups = sendPointGroups;
	}

	public List<PointGroup> getSendPiGroupData() {
		ArrayList<PointGroup> pgs = new ArrayList<PointGroup>();
		this.log.debug("load and send PIData start");
		String url = this.piPointFactory.getPiUrl();
		String uid = this.piPointFactory.getPiUid();
		String pwd = this.piPointFactory.getPiPwd();
		String org = this.piPointFactory.getOrgName();
		String showPointName = this.piPointFactory.getShowPointName();
		piPointFactory.getPointGroups();
		if (showPointName != null)
			showPointName = showPointName.trim().toLowerCase();
		if (org == null)
			org = "yzpgc.chd.com.cn";
		PiLimit pl = PiLimit.getPiLimit();

		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dd = sdf.format(d);
		this.log.info("Connection pi server :" + url);

		PIClientUtil pu = PIClientUtil.getPIClientUtil(url, uid, pwd);

		getPointGroups();

		for (int i = 0; i < this.pointGroups.size(); ++i) {
			if (i > pl.getGroupNum().intValue())
				break;
			PointGroup pg = (PointGroup) this.pointGroups.get(i);
			this.log.info("load for pointGroup: " + pg.getCcode());
			PointGroup pg_send = new PointGroup();
			pg_send.setCcode(pg.getCcode());
			pg_send.setOrg(org);
			List <Map<String,Object>> sendAttrs = new ArrayList<Map<String,Object>>();
			int n = 0;
			for (Map<String,Object> map : pg.getAttrs()) {
				if (n > pl.getPiNum().intValue())
					break;
				Map<String,Object> s_map = new HashMap<String,Object>();
				map.put("doccur", dd);
				DecimalFormat df = new DecimalFormat("#.00");
				String pName = (String) map.get("piName");
				String v = df.format(pu.getTagValue(pName));
				map.put("nvalue", v);

				for (String sname : pg.getSendNames())
					s_map.put(sname, map.get(sname));

				String lName = (showPointName.equals("true")) ? pName + " " : "";
				this.log.info("load  pi point " + lName + "value: " + map.get("nvalue"));
				sendAttrs.add(s_map);
				++n;
			}
			pg_send.setAttrs(sendAttrs);
			pgs.add(pg_send);
		}
		return pgs;
	}

	@Scheduled(cron="0 0/2 * * * ?")
	public void loadAndsendPIData() {
		this.sendPointGroups = getSendPiGroupData();
		String url_send = this.piPointFactory.getTargetUrl();
		String urlTop = this.piPointFactory.getUrlTop();
		this.log.info("send pi-data url " + url_send);
		String rName = this.piPointFactory.getResultName();

		Map<String,List<PointGroup>> map = new HashMap<String,List<PointGroup>>();
		map.put("data", this.sendPointGroups);

		if (url_send.indexOf("http") < 0) {
			url_send = urlTop + url_send;
		}

		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(new String[] { "sendNames" });
		JSONObject json = JSONObject.fromObject(map, jsonConfig);
		String result = HttpUtil.jsonPostString(url_send, json.toString());

		if ((result.equals("")) || (result.equals("erroe")) || (result.equals("jsonPost error"))) {
			this.log.info("send failure.");
		} else {
			this.log.info("send successful.");
			if ((rName != null) && (!(rName.trim().equals("")))) {
				JSONObject obj = JSONObject.fromObject(result);
				if (obj.get(rName) != null) {
					this.log.info("result " + rName + " : " + obj.get(rName).toString());
				} else if (result != null)
					this.log.info("result : " + result);
			} else {
				this.log.info(result);
			}
		}
	}
	
    public static String inputStreamToString(InputStream in){
        StringBuilder sb = new StringBuilder();
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line = null;
            for(int n = 0; (line = reader.readLine()) != null; n++){
                if(n > 0)
                    sb.append(System.lineSeparator());
                sb.append(line);
            }
            
            in.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return sb.toString();
    }
}