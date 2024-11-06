package com.vincent.coretest.vo.ro;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ColumnDefVo {
	public String name;
	public String type;
	public String desc;

	/**
	 * due-date-for-sight Date - Due Date for Sigh
	 * 
	 * @param input
	 */
	public static ColumnDefVo convert(String input) {
		if (input == null) {
			return null;
		}
		ColumnDefVo vo = new ColumnDefVo();

		String tmp = new String(input);
		String[] result = tmp.split(" ");

		vo.name = result[0];
		int i = 1;
		while (i < result.length) {
			if (result[i].trim().length() == 0) {
				i++;
			} else {
				vo.type = result[i];
				break;
			}
		}

		int index = tmp.indexOf(vo.type);
		tmp = tmp.substring(index + vo.type.length());
		
		index = tmp.indexOf("-");
		if (index > 0) {
			vo.desc = tmp.substring(index + 1).trim();
		}

		return vo;
	}

	@Override
	public String toString() {
		return "ColumnDefVo [name=" + name + ", type=" + type + ", desc=" + desc + "]";
	}

}
