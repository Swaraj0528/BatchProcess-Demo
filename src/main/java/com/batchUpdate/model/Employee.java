package com.batchUpdate.model;


import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Employee {
	
	@Id
	private Integer id;
	private String name;
	private Integer seq;
	private String email;
	//private Double marks;

}
