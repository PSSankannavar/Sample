package com.spring.onlinebookreviewsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

	 
		@RequestMapping("home.view")
		public String getHomePage(){
			System.out.println("I am in");
			return "Home";
		}

	
	
}
