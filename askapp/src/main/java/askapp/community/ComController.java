package askapp.community;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/com")
@RequiredArgsConstructor

public class ComController {
    private final ComService servicecom;
    @PostMapping("/addcom")
    public ResponseEntity<Community> add(
            @RequestBody Comrequest request
    ) throws Exception {
        return ResponseEntity.ok(servicecom.addCommunity(request));
    }


}
