package org.TRX.integration;

import javax.inject.Named;

@Named
public class SimpleService {

	public String hello(){
		return "hallo aus dem SimpleService";
	}
	
}
