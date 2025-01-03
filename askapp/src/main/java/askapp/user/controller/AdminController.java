package askapp.user.controller;

import askapp.auth.AuthenticationService;
import askapp.auth.RegisterRequest;
import askapp.post.blacklist.models.Blacklist;
import askapp.user.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/admin")

public class AdminController {
    @Autowired
    AuthenticationService authenticationService;
    @GetMapping("/addAdmin")
    public String addAdmin(Model model) {
        model.addAttribute("user", new User());
        return "new_admin";
    }

    @PostMapping("/save")
    public String saveAdmin(@ModelAttribute("user") RegisterRequest user) throws Exception {
        authenticationService.registerAdmin(user);
        return "redirect:http://localhost:4200/adminIndex/tablepanel";
    }
}
