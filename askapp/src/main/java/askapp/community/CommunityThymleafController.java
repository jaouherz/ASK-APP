package askapp.community;

import askapp.community.services.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/com")
@RequiredArgsConstructor
public class CommunityThymleafController {
    @Autowired
    CommunityService communityService;
    @GetMapping("/all")
    public String getAllCommunities(Model model) {
        model.addAttribute("communitylist", communityService.getAll());
        return "communitylist";
    }
    @GetMapping("/ban/{id}")
    public String updateCommunity(@PathVariable("id") long id) throws Exception {
        communityService.banCommunity(id);
        return "redirect:/api/v1/com/all";
    }
}
