package com.sportmgmt.controller.action;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sportmgmt.utility.constrant.SportConstrant;

@Controller
public class HomeAction {
	private Logger logger = Logger.getLogger(HomeAction.class);
	@RequestMapping("/LeagueHome")
	public String hello(ModelMap map)
	{
		//UserManager.getCountryStateCityMap();
		return SportConstrant.LEAGE_HOME_PAGE;
	}
	@RequestMapping("/LeagueLogin")
	public String login(ModelMap map)
	{
		//UserManager.getCountryStateCityMap();
		return SportConstrant.LEAGE_LOGIN_PAGE;
	}
	
}
