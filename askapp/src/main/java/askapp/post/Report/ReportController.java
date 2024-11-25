package askapp.post.Report;

import askapp.post.Models.Post;
import askapp.post.Models.PostRequest;
import askapp.post.repositories.Postrepo;
import askapp.post.services.PostService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/api/v1/report")
@RestController

@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;
    private final ReportRepository reportRepository;
    private final PostService postService;
    private final Postrepo postrepo;


    @PostMapping("/create")
    public ResponseEntity<RepINFO> createReport(@RequestBody Reprequest report) {
        return new ResponseEntity<RepINFO>(reportService.addReport(report), HttpStatus.CREATED);
    }

    @GetMapping("/reports")
    public ResponseEntity<List<RepINFO>> getrepports() {
        return new ResponseEntity<List<RepINFO>>(this.reportService.getAll(), HttpStatus.OK);
    }

    @PutMapping("/ReportNonValide/{reportId}")
    public ResponseEntity<String> ValiderReport(@PathVariable int reportId) {


        try {
            reportRepository.findById(reportId)
                    .orElseThrow(() -> new EntityNotFoundException("Report with ID " + reportId + " not found"));

            reportService.ValiderReport(reportId);

            return ResponseEntity.ok("Report with ID " + reportId + " has been successfully set to Traité and post visibility set to false.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Report with ID " + reportId + " not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred.");
        }
    }


  @PutMapping("/ReportNonValider/{reportId}")
    public ResponseEntity<String> NonValiderReport(@PathVariable int reportId) {
        try {
            Report report = reportRepository.findById(reportId)
                    .orElseThrow(() -> new EntityNotFoundException("Report with ID " + reportId + " not found"));
            reportService.NonValiderReport(reportId);
            return ResponseEntity.ok("Report with ID " + reportId + " has been successfully set to Traité and post visibility set to false.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Report with ID " + reportId + " not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred.");
        }
    }

    @DeleteMapping("/DeleteReport/{reportId}")
    public ResponseEntity<String> DeleteReport(@PathVariable int reportId) {
        try {
            Report report = reportRepository.findById(reportId)
                    .orElseThrow(() -> new EntityNotFoundException("Report with ID " + reportId + " not found"));
            reportRepository.delete(report);
            return ResponseEntity.ok("Report with ID " + reportId + " has been successfully deleted");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Report with ID " + reportId + " not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred.");
        }
    }

}
