package ua.in.usv.controller;

import org.springframework.beans.factory.annotation.Value;
import ua.in.usv.entity.firm.Company;
import ua.in.usv.entity.root.CustomUser;
import ua.in.usv.service.CompanyService;
import ua.in.usv.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @Value("${ispro.firm.site}")
    private String companySite;

    private final UserService userService;
    private final CompanyService companyService;

    @Autowired
    public HomeController(UserService userService, CompanyService companyService) {
        this.userService = userService;
        this.companyService = companyService;
    }

    @RequestMapping("/")
    public String index(Model model){

        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        CustomUser customUser = userService.findByLogin(login);
        model.addAttribute("user_name", customUser.getName());

        Company company = companyService.findFirst();
        model.addAttribute("company_name", company.getName());
        model.addAttribute("company_site", companySite);

        return "home/index";
    }

    @RequestMapping("/unauthorized")
    public String unauthorized(Model model){
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("login", user.getUsername());
        return "system/unauthorized";
    }
}

