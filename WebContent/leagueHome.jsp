<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta charset="utf-8">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>The12thman | Football</title>
    <link rel="stylesheet" href="/SportMgmt/css/foundation.css">
    <link rel="stylesheet" href="/SportMgmt/css/app.css">
    <link rel="stylesheet" href="/SportMgmt/css/superslides.css">
    <style>
    /*body{background-color:#1b75bc;}*/
	#mainMenu a{ color:#fff; font-size:24px; text-transform:uppercase; position:relative; }
	#mainMenu a:hover{ color:#D4CCCC; text-align:center; border-bottom:2px solid #f0e809; padding-bottom:16px; position:relative; }
	#mainMenu li:last-child a{ border-bottom:none !important;}
	ul#mainMenu{ list-style:none; position:relative; top:25%; margin:auto; text-align:right;}
	
	ul#mainMenu li{ list-style:none; text-decoration:none; display:inline-block; text-align:center; text-transform:uppercase; padding:8px 10px; margin:5px auto; }
	#mainMenu a.logo{ font-size:34px; color:#fff; border-bottom:none;}
	.menuOverlay{ width:100%; height:80px; z-index:9; background:#1b75bc; position:absolute; opacity:.8; }
	#logo{top:20%; position:relative;}
	#logo a{ color:#fff; font-size:30px; }
    </style>
</head>
<body>
<header class="menuOverlay">
        <div id="logo" class="large-4 columns">
    	<a href="index.php">The12thman</a>
        </div>
        <nav class="large-8 columns">
               <ul id="mainMenu">
                    
                    <li><a href="#"> How to Play </a></li>
                    <li><a href="#"> Features </a></li>
                    <li><a href="#"> Our Partners </a></li>
                    <li><a href="#"> Sign In </a></li>
              </ul>
        </nav>
    </header>
    <div id="slides">
        <div class="slides-container">
            <img src="/SportMgmt/images/6922364-football-stadium-wallpaper.jpg">
            <img src="/SportMgmt/images/mzhnah28se4o5ykw.jpg">
            <img src="/SportMgmt/images/FreeGreatPicture.com-137-nike-nike-football-boots.jpg">
            <img src="/SportMgmt/images/nike-nike-write-the-future-full-length-version-600-43549.jpg">
        </div>
        <div id="main-content">
            <div class="row clearfix main-banner">
                <div class="large-12 column logo">
                    <div class="logo-container wow zoomIn" data-wow-duration=".8s" data-wow-delay="0.5s"
                        style="visibility: visible; animation-duration: 0.8s; animation-delay: 0.5s;
                        animation-name: zoomIn;">
                    </div>
                </div>
                <div class="large-12 column euro-headings">
                    <p>
                        Fantasy Euro'16</p>
                    <div class="euro-divider">
                        <hr style="border-top: 5px solid #fff">
                    </div>
                    <p>
                        Win Real <span class="highlight">Cash!<span></span></span></p>
                </div>
            </div>
            <div class="row clearfix big-register">
                <div class="large-12 column text-center">
                    <button class="btn btn-default bigRegister" data-open="exampleModal1">
                        Play Now</button>
                </div>
            </div>
        </div>
    </div>
    
    
    <nav class="slides-navigation">
          <a href="#" class="next">Next</a>
          <a href="#" class="prev">Previous</a>
        </nav>
    </div>
    
    <!-- pop-up container -->
   

<div class="reveal medium" id="exampleModal1" data-reveal>
  
  
  <form name="RegisterForm" action="user/register" method="post">
        <div class="large-12 columns">
          <label>Name*
            <input type="text" name="displayName" placeholder="Name" required />
          </label>
        </div>
            <!-- <div class="large-12 columns">
          <label>last name*
            <input type="text" name="lastname" placeholder="last name" required />
          </label>
        </div>
 -->            <div class="large-12 columns">
          <label>Email Address/Login ID*
            <input type="email" name="emailId" placeholder="email" required />
          </label>
        </div>
           <!--  <div class="large-12 columns">
          <label>confirm email*
            <input type="email" name="emailconfirm" placeholder="email" required />
          </label>
        </div>
 -->        <div class="large-12 columns">
            <label>Password*</label>
            <input type="password" name="logonPassword" placeholder="password" required />       
            
             </div>
        <div class="large-12 columns">
            <label>confirm password*</label>
              <input type="password" name="logonPasswordConfirm" placeholder="password" />
            
            </div>
            <div class="large-12 columns">
            <br>
                <div align="right">
                    <a href="javascript:void(0);" class="button-lrg" onclick="submitForm();">REGISTER</a>
                </div>
            </div>
    </form>
  <button class="close-button" data-close aria-label="Close modal" type="button">
    <span aria-hidden="true">&times;</span>
  </button>
</div>
    
    <script src="/SportMgmt/js/vendor/jquery.js"></script>
    <script src="/SportMgmt/js/vendor/what-input.js"></script>
    <script src="/SportMgmt/js/vendor/foundation.js"></script>
    <script src="/SportMgmt/js/app.js"></script>
    <script src="/SportMgmt/js/jquery.easing.1.3.js"></script>
    <script src="/SportMgmt/js/jquery.animate-enhanced.min.js"></script>
    <script src="/SportMgmt/js/jquery.superslides.js" type="text/javascript" charset="utf-8"></script>
    <script>
        $(function () {
            $('#slides').superslides({
                hashchange: false
            });
        });
        function submitForm()
        {
        	var regForm = document.forms['RegisterForm'];
        	if(typeof regForm['displayName'] == 'undefined' || regForm['displayName'].value=='')
        	{
        		alert("Please input  Name");
        		return false;
        	}
        	if(typeof regForm['emailId'] == 'undefined' || regForm['emailId'].value=='')
        	{
        		alert("Please input email");
        		return false;
        	}
        	var isEmailValid = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(regForm['emailId'].value);
        	if(!isEmailValid)
       		{
        		alert("Please input valid email");
        		return false;
       		}
        	if(typeof regForm['logonPassword'] == 'undefined' || regForm['logonPassword'].value =='')
        	{
        		alert("Please input password");
        		return false;
        	}
        	if(typeof regForm['logonPasswordConfirm'] == 'undefined' || regForm['logonPasswordConfirm'].value !=regForm['logonPassword'].value)
        	{
        		alert("Password should match");
        		return false;
        	}
        	regForm.submit();
        }
    </script>
  </body>
</body>
</html>