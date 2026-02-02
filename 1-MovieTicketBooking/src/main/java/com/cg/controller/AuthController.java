package com.cg.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.cg.entity.Role;
import com.cg.entity.User;



  public class AuthController {
	@Autowired
	  private PasswordEncoder encoder;
	 
	    // User login page
	    @GetMapping("/login")
	    public String loginPage() {
	        return "login";  // loads login.html
	    }
	 
	    // User signup page
	    @GetMapping("/signup")
	    public String signupPage(Model model) {
	        model.addAttribute("user", new User());
	        return "signup";  // loads signup.html
	    }
	 
	    {
	 
	        // Assign default role USER
	        user.setRole(Role.USER);
	 
	        // Encrypt password before saving
	        user.setPassword(encoder.encode(user.getPassword()));
	 
	        // Enable user
	        user.setEnabled(true);
	 
	        // Save user in DB
	        userService.saveUser(user);
	 
	        // Redirect to login page after successful registration
	        return "redirect:/login";
	    }
	}

