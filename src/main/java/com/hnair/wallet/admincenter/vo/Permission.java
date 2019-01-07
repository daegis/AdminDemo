package com.hnair.wallet.admincenter.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Permission {

	private Integer id;
	private Integer pid;
	private String name;
	private String icon;
	private boolean open = true;
	private boolean checked = false;
	private List<Permission> children = new ArrayList<Permission>();
	
}
