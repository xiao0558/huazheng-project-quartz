package com.huazheng.project.hana.model;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

public class Test {

	public static void main(String[] args) {
		JSONObject object = JSONUtil.parseObj(new HanaTest1(), false);
		
		String str = "";
		str += "CREATE TRIGGER %s_TRIGGER_VAR_DELETE AFTER DELETE ON %s REFERENCING OLD ROW myoldrow BEGIN INSERT INTO "; 
		str += "%s(ID,\"TYPE\",\"BEFORE\") VALUES(%s_log_SEQ.NEXTVAL,'d','%s'); END;";
		String format = String.format(str, "TEST2","TEST2","TEST2_log","TEST1",object.toString());
		
		System.out.println(format);
	}
//	CREATE TRIGGER TEST2_TRIGGER_VAR_DELETE AFTER DELETE ON test2 REFERENCING OLD ROW myoldrow BEGIN INSERT INTO test2_log(ID,"TYPE","BEFORE") VALUES(test2_log_SEQ.NEXTVAL,'d','{"id":'||:myoldrow.id||',"user":"'||:myoldrow.user||'","pswd":"'||:myoldrow.pswd||'","times":"'||:myoldrow.times||'"}'); END;
//	CREATE TRIGGER TEST2_TRIGGER_VAR_DELETE AFTER DELETE ON TEST2 REFERENCING OLD ROW myoldrow BEGIN INSERT INTO TEST2(ID,"TYPE","BEFORE") VALUES(TEST1_log_SEQ.NEXTVAL,'d','{"rowids":null,"pswd":null,"times":null,"id":null,"user":null}'); END;

}
