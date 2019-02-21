package com.ygsoft.matcloud.piloader.scheduled;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.springframework.stereotype.Component;
import org.xvolks.jnative.JNative;
import org.xvolks.jnative.Type;
import org.xvolks.jnative.exceptions.NativeException;
import org.xvolks.jnative.pointers.Pointer;
import org.xvolks.jnative.pointers.memory.HeapMemoryBlock;
import org.xvolks.jnative.pointers.memory.MemoryBlockFactory;

@Component
public class PIClientUtil implements IPiUtil {
	private static PIClientUtil piClientUtil = new PIClientUtil();

	public static void main(String[] args) throws Exception {
		PIClientUtil pu = getPIClientUtil("10.135.14.10");
		System.out.println("7#DCS:GEMW 最新值：" + pu.getTagValue("7#DCS:GEMW"));
		System.out.println(
				"7#DCS:GEMW 2017-05-30 12:00:00值：" + pu.getTagValueByTime("7#DCS:MSFLOW", "2017-05-30 12:00:00"));
		System.out.println("7#DCS:GEMW 2017-05-20 08:00:00 ~ 2017-05-20 12:00:00最大值："
				+ pu.getTagMaxValue("7#DCS:MSFLOW", "2017-05-20 08:00:00", "2017-05-20 12:00:00"));
		System.out.println(
				"TL:S32CL102 2017-12-10 08:00:00值：" + pu.getTagValueByTime("TL:S32CL102", "2017-12-10 08:00:00"));
	}

	public static PIClientUtil getPIClientUtil() {
		return piClientUtil;
	}

	public static PIClientUtil getPIClientUtil(String url) {
		piClientUtil.connection(url, null, null);
		return piClientUtil;
	}

	public static PIClientUtil getPIClientUtil(String url, String uid, String pwd) {
		piClientUtil.connection(url, uid, pwd);
		return piClientUtil;
	}

	public void connection(String url, String uid, String pwd) {
		JNative messageBox;
		try {
			messageBox = new JNative("piapi32.dll", "piut_setservernode");
			messageBox.setRetVal(Type.INT);
			messageBox.setParameter(0, Type.STRING, url);
			messageBox.invoke();

			if (uid == null)
				return;
			messageBox = new JNative("piapi32.dll", "piut_login");
			messageBox.setRetVal(Type.INT);
			messageBox.setParameter(0, Type.STRING, uid);
			messageBox.setParameter(1, Type.STRING, pwd);
			messageBox.setParameter(2, Type.STRING, "valid");
			messageBox.invoke();
		} catch (NativeException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	public float getTagValue(String tagName)
	  {
	    JNative messageBox;
	    try
	    {
	      messageBox = new JNative("piapi32.dll", "pipt_findpoint");
	      messageBox.setRetVal(Type.INT);
	      messageBox.setParameter(0, Type.STRING, tagName);
	      Pointer p = new Pointer(new HeapMemoryBlock(1024));
	      messageBox.setParameter(1, p);
	      messageBox.invoke();

	      int ptId = p.getAsInt(0);
	      if (messageBox.getRetValAsInt() == 0) {
	        System.out.println("测点id :" + ptId);
	        messageBox = new JNative("piapi32.dll", "pisn_getsnapshot");
	        messageBox.setRetVal(Type.INT);
	        messageBox.setParameter(0, ptId);
	        Pointer pp = new Pointer(new HeapMemoryBlock(1024));
	        messageBox.setParameter(1, pp);
	        messageBox.setParameter(2, new Pointer(new HeapMemoryBlock(1024)));

	        messageBox.invoke();
	        if (messageBox.getRetValAsInt() == 0)
	          return pp.getAsFloat(0);
	      }

	      System.out.println(tagName + " 测点查询失败");
	    }
	    catch (NativeException e)
	    {
	      e.printStackTrace();
	    }
	    catch (IllegalAccessException e) {
	      e.printStackTrace();
	    }
	    return 0.0F;
	  }
	

	public float getTagValueByTime(String tagName, String time) {
		JNative messageBox;
		try {
			messageBox = new JNative("piapi32.dll", "pipt_findpoint");
			messageBox.setRetVal(Type.INT);
			messageBox.setParameter(0, Type.STRING, tagName);
			Pointer p = new Pointer(new HeapMemoryBlock(8));
			messageBox.setParameter(1, p);
			messageBox.invoke();
			int ptId = p.getAsInt(0);
			if (messageBox.getRetValAsInt() == 0) {
				System.out.println("测点id :" + ptId);
				messageBox = new JNative("piapi32.dll", "piar_value");
				messageBox.setRetVal(Type.INT);
				messageBox.setParameter(0, ptId);
				Pointer pp = new Pointer(new HeapMemoryBlock(1024));
				Pointer pp_status = new Pointer(new HeapMemoryBlock(1024));
				messageBox.setParameter(1, getTimeIntsec(time));
				messageBox.setParameter(2, Type.INT, "3");
				messageBox.setParameter(3, pp);
				messageBox.setParameter(4, pp_status);
				messageBox.invoke();
				if (messageBox.getRetValAsInt() == 0) {
					return pp.getAsFloat(0);
				}
				System.out.println(tagName + " 查询状态:" + messageBox.getRetVal());

			}
			System.out.println(tagName + " 测点查询失败");
		} catch (NativeException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return 0.0F;
	}

	public float getTagMinValue(String tagName, String startTime, String endTime) {
		return getTagValuesBetween(tagName, startTime, endTime, 1);
	}

	public float getTagMaxValue(String tagName, String startTime, String endTime) {
		return getTagValuesBetween(tagName, startTime, endTime, 2);
	}

	public float getTagAvgValue(String tagName, String startTime, String endTime) {
		return getTagValuesBetween(tagName, startTime, endTime, 5);
	}

	public float getTagValuesBetween(String tagName, String startTime, String endTime, int code) {
		JNative messageBox;
		try {
			messageBox = new JNative("piapi32.dll", "pipt_findpoint");
			messageBox.setRetVal(Type.INT);
			messageBox.setParameter(0, Type.STRING, tagName);
			Pointer p = new Pointer(new HeapMemoryBlock(1024));
			messageBox.setParameter(1, p);
			messageBox.invoke();
			int ptId = p.getAsInt(0);
			if (messageBox.getRetValAsInt() == 0) {
				System.out.println("测点id :" + ptId);
				messageBox = new JNative("piapi32.dll", "piar_summary");
				messageBox.setRetVal(Type.INT);
				messageBox.setParameter(0, ptId);

				Pointer p_startTime = getTimeIntsec(startTime);
				Pointer p_endTime = getTimeIntsec(endTime);
				Pointer p_retVal = new Pointer(new HeapMemoryBlock(8));
				Pointer p_pctGood = new Pointer(new HeapMemoryBlock(8));

				messageBox.setParameter(1, p_startTime);
				messageBox.setParameter(2, p_endTime);
				messageBox.setParameter(3, p_retVal);
				messageBox.setParameter(4, p_pctGood);
				messageBox.setParameter(5, code);
				messageBox.invoke();
				if (messageBox.getRetValAsInt() == 0) {
					return p_retVal.getAsFloat(0);
				}
				System.out.println(tagName + " 查询状态:" + messageBox.getRetVal());

			}
			System.out.println(tagName + " 测点查询失败");
		} catch (NativeException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return 0.0F;
	}

	public int getPITime(String time) {
		Pointer pointer;
		try {
			pointer = new Pointer(MemoryBlockFactory.createMemoryBlock(8));
			JNative messageBox = new JNative("piapi32.dll", "pitm_parsetime");
			messageBox.setRetVal(Type.INT);
			messageBox.setParameter(0, Type.STRING, time);
			messageBox.setParameter(1, Type.INT, "0");
			messageBox.setParameter(2, pointer);
			messageBox.invoke();
			if (messageBox.getRetValAsInt() == 0) {
				return pointer.getAsInt(0);
			}
			System.out.println("失败");
			return 0;
		} catch (NativeException e) {
			e.printStackTrace();
			return 0;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public String getTimeFromInt(int time) {
		Pointer pointer;
		try {
			pointer = new Pointer(MemoryBlockFactory.createMemoryBlock(8));
			JNative messageBox = new JNative("piapi32.dll", "pitm_formtime");
			messageBox.setRetVal(Type.INT);
			messageBox.setParameter(0, getPITime(""));
			messageBox.setParameter(1, pointer);
			messageBox.setParameter(2, time);
			messageBox.invoke();

			return pointer.getAsString();
		} catch (NativeException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return "";
	}

	public int[] getTimeSecint(int time) {
		int[] t_array = new int[6];
		try {
			Pointer pointer = new Pointer(MemoryBlockFactory.createMemoryBlock(4 * t_array.length));
			JNative messageBox = new JNative("piapi32.dll", "pitm_secint");

			messageBox.setParameter(0, time);
			messageBox.setParameter(1, pointer);
			messageBox.invoke();
			for (int i = 0; i < t_array.length; ++i)
				t_array[i] = pointer.getAsInt(i * 4);

			return t_array;
		} catch (NativeException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Pointer getTimeIntsec(String time) {
		int[] t_array;
		try {
			t_array = timeArray(time);
			Pointer pointer = new Pointer(MemoryBlockFactory.createMemoryBlock(8));
			Pointer pointer_array = new Pointer(MemoryBlockFactory.createMemoryBlock(4 * t_array.length));
			JNative messageBox = new JNative("piapi32.dll", "pitm_intsec");
			for (int i = 0; i < t_array.length; ++i)
				pointer_array.setIntAt(4 * i, t_array[i]);

			messageBox.setParameter(0, pointer);
			messageBox.setParameter(1, pointer_array);
			messageBox.invoke();
			return pointer;
		} catch (NativeException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	public int[] timeArray(String time) {
		SimpleDateFormat FORMATTER_FULL = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		int[] t_array = null;
		try {
			cal.setTime(FORMATTER_FULL.parse(time));
			t_array = new int[] { cal.get(2), cal.get(5), cal.get(1), cal.get(11), cal.get(12), cal.get(13) };
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return t_array;
	}

	public String getPITimeStr(String timeStr) {
		SimpleDateFormat FORMATTER_FULL = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(FORMATTER_FULL.parse(timeStr));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		SimpleDateFormat dateFmt = new SimpleDateFormat("dd-MMM-yy HH:mm:ss", Locale.US);
		return dateFmt.format(cal.getTime());
	}
}