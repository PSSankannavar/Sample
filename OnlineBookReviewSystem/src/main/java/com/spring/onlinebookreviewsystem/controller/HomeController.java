package com.spring.onlinebookreviewsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

	 
	/*	@RequestMapping("home.view")
		public String getHomePage(){
			System.out.println("I am in");
			return "Home";
		}*/
		@RequestMapping(value = "/home**", method = RequestMethod.GET)
		public ModelAndView adminPage() {

			ModelAndView model = new ModelAndView();
			model.addObject("title", "Spring Security Hello World");
			model.addObject("message", "This is protected page!");
			model.setViewName("Home");

			return model;

		}
	
	
}
