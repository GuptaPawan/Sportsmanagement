package com.sportmgmt.controller.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ravij.crypto.EncodeDecoder;
import com.sportmgmt.controller.bean.User;
import com.sportmgmt.model.manager.UserManager;
import com.sportmgmt.utility.common.MailUtility;
import com.sportmgmt.utility.common.PropertyFileUtility;
import com.sportmgmt.utility.constrant.ErrorConstrant;
import com.sportmgmt.utility.constrant.SportConstrant;

@Controller
@RequestMapping("/user")
public class UserAction {
	private Logger logger = Logger.getLogger(UserAction.class);
	@Autowired
	MailUtility mailUtility;
	public MailUtility getMailUtility() {
		return mailUtility;
	}
	public void setMailUtility(MailUtility mailUtility) {
		this.mailUtility = mailUtility;
	}
	@Autowired
	PropertyFileUtility propFileUtility;
	public PropertyFileUtility getPropFileUtility() {
		return propFileUtility;
	}
	public void setPropFileUtility(PropertyFileUtility propFileUtility) {
		this.propFileUtility = propFileUtility;
	}
	@RequestMapping(value = "register", method = RequestMethod.POST)
	public String userLogin(ModelMap modeMap,@RequestParam Map<String,String> userMap)
	{
		logger.debug("Entry in register method model Map: "+modeMap);
		if(userMap.get("emailId") == null || userMap.get("emailId").equals(SportConstrant.NULL))
		{
			modeMap.put("emailIdError", "emailId is required");
		}
		if(userMap.get("displayName") == null || userMap.get("displayName").equals(SportConstrant.NULL))
		{
			modeMap.put("displayName", "name is required");
		}
		/*if(userMap.get("dobDay") == null || userMap.get("dobDay").equals(SportConstrant.NULL))
		{
			modeMap.put("dobError", "Date of Birth is required");
		}
		if(userMap.get("dobMonth") == null || userMap.get("dobMonth").equals(SportConstrant.NULL))
		{
			modeMap.put("dobError", "Date of Birth is required");
		}
		if(userMap.get("dobYear") == null || userMap.get("dobYear").equals(SportConstrant.NULL))
		{
			modeMap.put("dobError", "Date of Birth is required");
		}*/
		if(userMap.get("logonId") == null || userMap.get("logonId").equals(SportConstrant.NULL))
		{
			userMap.put("logonId", userMap.get("emailId"));
		}
		if(userMap.get("logonPassword") == null || userMap.get("logonPassword").equals(SportConstrant.NULL))
		{
			modeMap.put("logonPasswordError", "User Password is required");
		}
		else
		{
			logger.debug("--------- password: "+userMap.get("logonPassword"));
			try
			{
				userMap.put("logonPassword", EncodeDecoder.encrypt(userMap.get("logonPassword")));
				logger.debug("--------- password: After Encoding:  "+userMap.get("logonPassword"));
			}
			catch(Exception ex)
			{
				logger.error("--------- Error in encoding password: "+ex);
			}
		}
		logger.debug("------------ Error Map: "+modeMap);
		if(modeMap.isEmpty())
		{
			boolean isRegistered = UserManager.saveUser(userMap);
			modeMap.put("isRegistered",isRegistered);
			if(isRegistered)
			{
				modeMap.put("message","You are registered");
				logger.debug("----------- start to send mail------");
				try
				{
					Map<String,Object> mailMap = new java.util.HashMap<String,Object>();
					
					mailMap.put(SportConstrant.FROM, propFileUtility.getEnvProperty().getString(SportConstrant.FROM));
					mailMap.put(SportConstrant.TO,userMap.get("emailId"));
					mailMap.put("displayName", userMap.get("displayName"));
					mailMap.put(SportConstrant.VELOCIYY_FILE_LOC,propFileUtility.getEnvProperty().getString(SportConstrant.USER_VER_EMAIL_LOC) );
					mailMap.put(SportConstrant.SUBJECT, propFileUtility.getEnvProperty().getString(SportConstrant.USER_VER_EMAIL_SUB));
					String userVerifyURL = propFileUtility.getEnvProperty().getString(SportConstrant.BASE_URL)+propFileUtility.getEnvProperty().getString(SportConstrant.USER_VER_URL)+"/"+UserManager.getUserId();
					mailMap.put(SportConstrant.USER_VER_URL,userVerifyURL);
					mailUtility.sendHtmlMail(mailMap);
					logger.debug("----------- end to send mail------");
				}
				catch (Exception ex)
				{
					logger.error("----------- Excepton in Sending Mail ----------"+ex);
				}
			}
			else
			{
				modeMap.put("message","Your Registration is failed due to "+UserManager.getErrorMessage());
			}
		}
		else
		{
			modeMap.put("isRegistered",false);
			modeMap.put("message","Your Registration is failed due to incomplete info");
		}
		//return "redirect:/login/loginSuccess.jsp";
		return SportConstrant.USER_REG_RESULT_PAGE;

	}
	
	@RequestMapping(value = "validate", method = RequestMethod.GET)
	public @ResponseBody Map validateUser(@RequestParam("logonId") String logonId, @RequestParam("logonPassword") String logonPassword)
	{
	 java.util.Map resultMap = new java.util.HashMap();
	 boolean isValidUser = false;
	 if(logonId == null || logonId.equals(SportConstrant.NULL))
	 {
		 resultMap.put("errorCode", ErrorConstrant.EMPTY_LOGON);
		 resultMap.put("errorMessage", "Email Id cannot be blank");
	 }
	 if(logonPassword == null || logonPassword.equals(SportConstrant.NULL))
	 {
		 resultMap.put("errorCode", ErrorConstrant.EMPTY_PASS);
		 resultMap.put("errorMessage", "Password Can't be black");
	 }
	 else
	 {
		 logger.debug("--------- password: "+logonPassword);
			try
			{
				logonPassword = EncodeDecoder.encrypt(logonPassword);
				logger.debug("--------- password: After Encoding:  "+logonPassword);
			}
			catch(Exception ex)
			{
				logger.error("--------- Error in encoding password: "+ex);
			}
	 }
	 if(resultMap.size() == 0)
	 {
		 isValidUser = UserManager.validateUser(logonId, logonPassword);
		 if(isValidUser)
		 {
			 resultMap.put("userId", UserManager.getUserId()); 
		 }
		 else
		 {
			 resultMap.put("errorCode", UserManager.getErrorCode());
			 resultMap.put("errorMessage", UserManager.getErrorMessage());
		 }
		
	}
	 resultMap.put("isValidUser", isValidUser);
	 return resultMap;
	}
	@RequestMapping(value = "verify/{userId}", method = RequestMethod.GET)
	public  String verifyUser(ModelMap modeMap,@PathVariable String userId)
	{
	 logger.debug("---------- Entry in verifyUser  -----Path Variable User Id:  "+userId);
	 modeMap.clear();
	 if(userId == null || userId.equals(SportConstrant.NULL))
	 {
		 modeMap.put("errorCode", ErrorConstrant.EMPTY_LOGON);
		 modeMap.put("errorMessage", "Invalid URL");
		 modeMap.put("isVerified", false);
	 }
	
	 if(modeMap.size() == 0)
	 {
		 if(UserManager.activateUser(userId))
		 {
			 modeMap.put("isVerified", true);
		 }
		 else
		 {
			 modeMap.put("isVerified", false);
			 modeMap.put("errorCode", UserManager.getErrorCode());
			 modeMap.put("errorMessage", UserManager.getErrorMessage());
			 if(UserManager.getErrorCode().equals(ErrorConstrant.TRANSACTION_ERROR))
			 modeMap.put("errorMessage", "Invalid URL");
		 }
		
	}
	 return SportConstrant.USER_VERIFY_PAGE;
	}
	@RequestMapping(value = "login/{userId}", method = RequestMethod.GET)
	public  String doLogin(ModelMap modeMap,@PathVariable String userId,HttpServletRequest request)
	{
	 logger.debug("---------- Entry in login  ---- Path Variable User Id:  "+userId);
	 modeMap.clear();
	 if(userId == null || userId.equals(SportConstrant.NULL))
	 {
		 modeMap.put("errorCode", ErrorConstrant.EMPTY_LOGON);
		 modeMap.put("errorMessage", "Invalid URL");
		 modeMap.put("isLogined", false);
	 }
	
	 if(modeMap.size() == 0)
	 {
		 logger.debug("---------- Calling Hibernate get User Method: ");
		 User user = UserManager.getUser(userId);
		 logger.debug("---------- Fetche User Object: "+user);
		 if(user != null)
		 {
			 modeMap.put("isLogined", true);
			 logger.debug("---------- Getting HTTP Session: "+user);
			 HttpSession session = request.getSession();
			 logger.debug("---------- Setting User to Sesison: "+user);
			 session.setAttribute("userId", String.valueOf(user.getUserId()));
			 session.setAttribute("user", user);
			 
		 }
		 else
		 {
			 modeMap.put("isLogined", false);
			 modeMap.put("errorCode", UserManager.getErrorCode());
			 modeMap.put("errorMessage", UserManager.getErrorMessage());
			 if(UserManager.getErrorCode().equals(ErrorConstrant.TRANSACTION_ERROR))
			 modeMap.put("errorMessage", "Invalid URL");
		 }
		
	}
	 logger.debug("---------- Forwardng to : "+SportConstrant.USER_LANDING_PAGE);
	 return SportConstrant.USER_LANDING_PAGE;
	}
	@RequestMapping(value = "forgotPassword", method = RequestMethod.GET)
	public  String forgorPassword(ModelMap modeMap,@RequestParam String emailId)
	{
	 logger.debug("---------- Entry in forgotPassword  ---- Path Variable EmailId:  "+emailId);
	 modeMap.clear();
	 if(emailId == null || emailId.equals(SportConstrant.NULL))
	 {
		 modeMap.put("errorCode", ErrorConstrant.EMPTY_LOGON);
		 modeMap.put("errorMessage", "Invalid URL");
	 }
	
	 if(modeMap.size() == 0)
	 {
		 logger.debug("---------- Calling Hibernate getPasswordByEmail Method: ");
		 String password = UserManager.getPasswordByEmail(emailId);
		 logger.debug("---------- Fetche User Passoword: "+password);
		 if(password != null && !password.equals(SportConstrant.NULL))
		 {
				try
				{
					password = EncodeDecoder.decrypt(password);
					logger.debug("--------- password: After Decoding:  "+password);
				}
				catch(Exception ex)
				{
					logger.error("--------- Error in encoding password: "+ex);
				}
			 logger.debug("---------- Starting to send mail");
			 try
				{
					Map<String,Object> mailMap = new java.util.HashMap<String,Object>();
					
					mailMap.put(SportConstrant.FROM, propFileUtility.getEnvProperty().getString(SportConstrant.FROM));
					mailMap.put(SportConstrant.TO,emailId);
					mailMap.put("password", password);
					mailMap.put(SportConstrant.VELOCIYY_FILE_LOC,propFileUtility.getEnvProperty().getString(SportConstrant.FORGOT_PASS_EMAIL_LOC) );
					mailMap.put(SportConstrant.SUBJECT, propFileUtility.getEnvProperty().getString(SportConstrant.FORGOT_PASSWORD_EMAIL_SUB));
					mailUtility.sendHtmlMail(mailMap);
					logger.debug("----------- end to send mail------");
					modeMap.put("message", "Password is sent to provided email");
				}
				catch (Exception ex)
				{
					logger.error("----------- Excepton in Sending Mail ----------"+ex);
					modeMap.put("message", "Could not send mail on provided email Id");
				}
			 		 
		 }
		 else
		 {
			 modeMap.put("errorCode", UserManager.getErrorCode());
			 modeMap.put("errorMessage", UserManager.getErrorMessage());
			 if(UserManager.getErrorCode().equals(ErrorConstrant.TRANSACTION_ERROR))
			 modeMap.put("errorMessage", "Invalid URL");
		 }
		
	}
	 logger.debug("---------- Forwardng to : "+SportConstrant.FORGOT_PASS_RESULT_PAGE);
	 return SportConstrant.FORGOT_PASS_RESULT_PAGE;
	}
	@RequestMapping(value = "UpdateAction", method = RequestMethod.POST)
	public String updateUser(ModelMap modeMap,@RequestParam Map<String,String> userMap,HttpServletRequest request)
	{
		logger.debug("Entry in UpdateAction method model Map: "+modeMap);
		if(userMap.get("userId") == null || userMap.get("userId").equals(SportConstrant.NULL))
		{
			modeMap.put("userIdError", "UserId is required");
		}
		if(userMap.get(SportConstrant.ACTION) != null && userMap.get(SportConstrant.ACTION).equals(SportConstrant.CHANGE_PASS))
		{
			if(userMap.get("logonPassword") == null || userMap.get("logonPassword").equals(SportConstrant.NULL))
			modeMap.put("newPasswordError", "New Password is required");
			else
			{
				logger.debug("--------- password: "+userMap.get("logonPassword"));
				try
				{
					userMap.put("logonPassword", EncodeDecoder.encrypt(userMap.get("logonPassword")));
					logger.debug("--------- password: After Encoding:  "+userMap.get("logonPassword"));
				}
				catch(Exception ex)
				{
					logger.error("--------- Error in encoding password: "+ex);
				}
			}
		}
		logger.debug("------------ Error Map: "+modeMap);
		if(modeMap.isEmpty())
		{
			boolean isUpdated = UserManager.saveUser(userMap);
			modeMap.put("isUpdated",isUpdated);
			if(isUpdated)
			{
				modeMap.put("message","Your profile is updated");
				logger.debug("----------- start to send mail------");
				if(userMap.get(SportConstrant.ACTION) != null && userMap.get(SportConstrant.ACTION).equals(SportConstrant.CHANGE_PASS))
				{
						String emailId = SportConstrant.NULL;
						String displayName = SportConstrant.NULL;
						if(userMap.get("emailId") != null && !userMap.get("emailId").equals(SportConstrant.NULL))
						{
							emailId = userMap.get("emailId");
						}
						else
						{
							HttpSession session = request.getSession();
							if(session.getAttribute("user") !=null && ((User)session.getAttribute("user")).getEmailId() != null && !((User)session.getAttribute("user")).getEmailId().equals(SportConstrant.NULL))
							{
								emailId = ((User)session.getAttribute("user")).getEmailId();
							}
							if(session.getAttribute("user") !=null && ((User)session.getAttribute("user")).getDisplayName() != null && !((User)session.getAttribute("user")).getDisplayName().equals(SportConstrant.NULL))
							{
								displayName = ((User)session.getAttribute("user")).getDisplayName();
							}
						}
						if(!emailId.equals(SportConstrant.NULL))
						{
							try
							{
								Map<String,Object> mailMap = new java.util.HashMap<String,Object>();
								//mailMap.put(SportConstrant.FROM, propFileUtility.getEnvProperty().getString(SportConstrant.FROM));
								mailMap.put(SportConstrant.TO,emailId);
								mailMap.put("displayName", displayName);
								mailMap.put(SportConstrant.VELOCIYY_FILE_LOC,propFileUtility.getEnvProperty().getString(SportConstrant.USER_PASS_CHANGE_EMAIL_LOC));
								mailMap.put(SportConstrant.SUBJECT, propFileUtility.getEnvProperty().getString(SportConstrant.USER_PASS_CHANGE_EMAIL_SUB));
								mailUtility.sendHtmlMail(mailMap);
								logger.debug("----------- Mail sent successfylly to mail Id: "+emailId);
							}
							catch (Exception ex)
							{
								logger.error("----------- Excepton in Sending Mail ----------"+ex);
							}
						}
						else
						{
							logger.error("-------Do not found email to send the mail ----------: "+emailId);
						}
						
				}
				
			}
			else
			{
				if(userMap.get(SportConstrant.ACTION) != null && userMap.get(SportConstrant.ACTION).equals(SportConstrant.CHANGE_PASS))
				{
					modeMap.put("message","Password Chagne Request failed due to "+UserManager.getErrorMessage());
				}
				else
				{
					modeMap.put("message","Your Registration is failed due to "+UserManager.getErrorMessage());
				}
			}
		}
		else
		{
			modeMap.put("message","Your Registration is failed due to incomplete info");
		}
		modeMap.put(SportConstrant.ACTION,userMap.get(SportConstrant.ACTION));
		//return "redirect:/login/loginSuccess.jsp";
		return SportConstrant.USER_UPDATE_RESULT_PAGE;

	}
	@RequestMapping(value = "UserUpdateView", method = RequestMethod.GET)
	public  String updateView(ModelMap modeMap,HttpServletRequest request)
	{
		 logger.debug("---------- Entry in --- UpdateView");
		 HashMap<String,HashMap<String,ArrayList<String>>> countryStateCityMap = UserManager.getCountryStateCityMap();
		 logger.debug("-------- UpdateView : countryStateCityMap: "+countryStateCityMap);
		 modeMap.put("countryStateCityMap", countryStateCityMap);
		 String countryStateCityJson = "{}";
		 if(countryStateCityMap != null)
		 {
			 ObjectMapper mapperObj = new ObjectMapper();
			 try
			 {
				 countryStateCityJson = mapperObj.writeValueAsString(countryStateCityMap);
				 logger.debug("-------- UpdateView : countryStateCityJson: "+countryStateCityJson);
				 modeMap.put("countryStateCityJson", countryStateCityJson);
			 }
			 catch(Exception ex)
			 {
				 logger.error("---------- Entry in parsing map to json: "+ex);
			 }
		 }
		 return SportConstrant.USER_UPDATE_PAGE;
	}
	@RequestMapping(value = "ChangePasswordView", method = RequestMethod.GET)
	public  String changePasswordView(ModelMap modeMap,HttpServletRequest request)
	{
		 logger.debug("---------- Entry in --- ChangePasswordView");
		 return SportConstrant.CHANGE_PASSWORD_PAGE;
	}
	@RequestMapping(value = "ForgotPasswordView", method = RequestMethod.GET)
	public  String forgotPasswordView(ModelMap modeMap,HttpServletRequest request)
	{
		 logger.debug("---------- Entry in --- ChangePasswordView");
		 return SportConstrant.FORGOT_PASSWORD_PAGE;
	}
}
