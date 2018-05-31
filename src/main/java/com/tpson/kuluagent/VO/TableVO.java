package com.tpson.kuluagent.VO;

public class TableVO {
	private Integer total;
	private Object rows;
	
	public TableVO(Integer total, Object rows) {
		this.total = total;
		this.rows = rows;
	}
	
	public static TableVO successResult(Integer total, Object rows) {
		return new TableVO(total, rows);
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Object getRows() {
		return rows;
	}

	public void setRows(Object rows) {
		this.rows = rows;
	}
	
	/*@Override
	public String toString() {
		JSONObject json = new JSONObject();
		json.put("total", total);
		json.put("rows", rows);
		
		return json.toJSONString();
	}*/
}
