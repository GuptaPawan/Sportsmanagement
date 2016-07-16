package com.sportmgmt.controller.action;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeAction {
	private Logger logger = Logger.getLogger(HomeAction.class);
	@RequestMapping("/LeagueHome")
	public String hello(ModelMap map)
	{
		//UserManager.getCountryStateCityMap();
		System.out.println("<----------------- Entered By User: ");
		return "leagueHome";
	}
	
}
