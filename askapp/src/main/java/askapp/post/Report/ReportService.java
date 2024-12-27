package askapp.post.Report;

import askapp.post.Models.ModelsINFO.PostINFO;
import askapp.post.Models.Post;
import askapp.post.Models.PostRequest;
import askapp.post.repositories.Postrepo;
import askapp.user.models.User;
import askapp.user.usersrepo.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ReportService {
    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private  Postrepo postRepository;
    @Autowired
    private UserRepository userRepository;

    public RepINFO addReport(Reprequest request) {
        Post post = postRepository.findById(request.getPost())
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));
        User user= userRepository.findById(request.getWhoreported())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Report report = Report.builder()
                .date_ajout(LocalDateTime.now())
                .post(post)
                .user(user)
                .Cause(request.getCause())
                .etat("NonTraité")
                .description(request.getDescription())
                .build();

        reportRepository.save(report) ;
        RepINFO  returnedrep = RepINFO.builder().id(report.getId()).cause(report.getCause()).date_ajout(report.getDate_ajout()).post(report.getPost().getId()).whoposted(report.getPost().getWhoposted().getId().toString()).build();
        return returnedrep ;
    }




    public void ValiderReport(long reportId) {

        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new EntityNotFoundException("Report with ID " + reportId + " not found."));
        Post post= report.getPost();
        report.setEtat("Traité");
        post.setIsvisible(false);
        reportRepository.save(report);
        postRepository.save(post);

    }

    public void NonValiderReport(long reportId) {

        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new EntityNotFoundException("Report with ID " + reportId + " not found."));
        Post post= report.getPost();
        report.setEtat("Traité");
        post.setIsvisible(true);
        reportRepository.save(report);
        postRepository.save(post);

    }
    public RepINFO mapToReportinfo(Report report) {
        return RepINFO.builder()
                .id(report.getId())
                .date_ajout(report.getDate_ajout())
                .post(report.getPost().getId())
                .whoposted(report.getPost().getWhoposted().getUsername())
                .cause(report.getCause())
                .etat(report.getEtat())
                .description(report.getDescription())
                .whoreported(report.getUser().getEmail())
                .build();
    }
    public List<RepINFO> getAll(){
        return this.reportRepository.findAll().stream()
                .map(this::mapToReportinfo)
                .collect(Collectors.toList());
    }

}
