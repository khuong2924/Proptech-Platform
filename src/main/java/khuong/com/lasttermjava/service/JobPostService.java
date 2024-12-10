package khuong.com.lasttermjava.service;

import khuong.com.lasttermjava.dto.JobPostDTO;
import khuong.com.lasttermjava.entity.JobPost;
import khuong.com.lasttermjava.repository.JobPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class JobPostService {

    @Autowired
    private JobPostRepository jobPostRepository;


    private List<JobPostDTO> mapToDto(List<JobPost> jobPosts) {
        List<JobPostDTO> jobPostDTOList = new ArrayList<>();
        for (JobPost jobPost : jobPosts) {
            jobPostDTOList.add(new JobPostDTO(
                    jobPost.getId(),
                    jobPost.getUser().getId(),
                    jobPost.getLoaiBaiDang(),
                    jobPost.getDiaChi(),
                    jobPost.getLoaiBDS(),
                    jobPost.getDienTich(),
                    jobPost.getMucGia(),
                    jobPost.getDonVi(),
                    jobPost.getGiayToPhapLy(),
                    jobPost.getNoiThat(),
                    jobPost.getSoPhongNgu(),
                    jobPost.getSoPhongTamVeSinh(),
                    jobPost.getHuongNha(),
                    jobPost.getHuongBanCong(),
                    jobPost.getDuongVao(),
                    jobPost.getMatTien(),
                    jobPost.getTenChuBaiDang(),
                    jobPost.getSoDienThoaiChu(),
                    jobPost.getTieuDe(),
                    jobPost.getMoTa(),
                    jobPost.getThoiGianDang(),
                    jobPost.getCongKhai(),
                    jobPost.getImageUrls()
            ));
        }
        return jobPostDTOList;
    }

    public List<JobPostDTO> getAll() {
        List<JobPost> jobPosts = jobPostRepository.findAll();
        return mapToDto(jobPosts);
    }

    public void create(JobPostDTO jobPostDTO) {
        JobPost jobPost = new JobPost();
        jobPost.setLoaiBaiDang(jobPostDTO.getLoaiBaiDang());
        jobPost.setDiaChi(jobPostDTO.getDiaChi());
        jobPost.setLoaiBDS(jobPostDTO.getLoaiBDS());
        jobPost.setDienTich(jobPostDTO.getDienTich());
        jobPost.setMucGia(jobPostDTO.getMucGia());
        jobPost.setDonVi(jobPostDTO.getDonVi());
        jobPost.setGiayToPhapLy(jobPostDTO.getGiayToPhapLy());
        jobPost.setNoiThat(jobPostDTO.getNoiThat());
        jobPost.setSoPhongNgu(jobPostDTO.getSoPhongNgu());
        jobPost.setSoPhongTamVeSinh(jobPostDTO.getSoPhongTamVeSinh());
        jobPost.setHuongNha(jobPostDTO.getHuongNha());
        jobPost.setHuongBanCong(jobPostDTO.getHuongBanCong());
        jobPost.setDonVi(jobPostDTO.getDonVi());
        jobPost.setMatTien(jobPostDTO.getMatTien());
        jobPost.setTenChuBaiDang(jobPostDTO.getTenChuBaiDang());
        jobPost.setTieuDe(jobPostDTO.getTieuDe());
        jobPost.setMoTa(jobPostDTO.getMoTa());
        jobPost.setThoiGianDang(jobPostDTO.getThoiGianDang());
        jobPost.setCongKhai(jobPostDTO.getCongKhai());
        jobPost.setImageUrls(jobPostDTO.getImageUrls());
        jobPostRepository.save(jobPost);
    }

    public void update(Long id, JobPostDTO jobPostDTO) {
        JobPost jobPost = jobPostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job post not found"));
        jobPost.setLoaiBaiDang(jobPostDTO.getLoaiBaiDang());
        jobPost.setDiaChi(jobPostDTO.getDiaChi());
        jobPost.setLoaiBDS(jobPostDTO.getLoaiBDS());
        jobPost.setDienTich(jobPostDTO.getDienTich());
        jobPost.setMucGia(jobPostDTO.getMucGia());
        jobPost.setDonVi(jobPostDTO.getDonVi());
        jobPost.setGiayToPhapLy(jobPostDTO.getGiayToPhapLy());
        jobPost.setNoiThat(jobPostDTO.getNoiThat());
        jobPost.setSoPhongNgu(jobPostDTO.getSoPhongNgu());
        jobPost.setSoPhongTamVeSinh(jobPostDTO.getSoPhongTamVeSinh());
        jobPost.setHuongNha(jobPostDTO.getHuongNha());
        jobPost.setHuongBanCong(jobPostDTO.getHuongBanCong());
        jobPost.setDonVi(jobPostDTO.getDonVi());
        jobPost.setMatTien(jobPostDTO.getMatTien());
        jobPost.setTenChuBaiDang(jobPostDTO.getTenChuBaiDang());
        jobPost.setTieuDe(jobPostDTO.getTieuDe());
        jobPost.setMoTa(jobPostDTO.getMoTa());
        jobPost.setThoiGianDang(jobPostDTO.getThoiGianDang());
        jobPost.setCongKhai(jobPostDTO.getCongKhai());
        jobPost.setImageUrls(jobPostDTO.getImageUrls());
        jobPostRepository.save(jobPost);
    }

    public JobPostDTO getById(Long id) {
        JobPost jobPost = jobPostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job post not found"));
        return new JobPostDTO(
                jobPost.getId(),
                jobPost.getUser().getId(),
                jobPost.getLoaiBaiDang(),
                jobPost.getDiaChi(),
                jobPost.getLoaiBDS(),
                jobPost.getDienTich(),
                jobPost.getMucGia(),
                jobPost.getDonVi(),
                jobPost.getGiayToPhapLy(),
                jobPost.getNoiThat(),
                jobPost.getSoPhongNgu(),
                jobPost.getSoPhongTamVeSinh(),
                jobPost.getHuongNha(),
                jobPost.getHuongBanCong(),
                jobPost.getDuongVao(),
                jobPost.getMatTien(),
                jobPost.getTenChuBaiDang(),
                jobPost.getSoDienThoaiChu(),
                jobPost.getTieuDe(),
                jobPost.getMoTa(),
                jobPost.getThoiGianDang(),
                jobPost.getCongKhai(),
                jobPost.getImageUrls()
        );
    }

    public void delete(Long id) {
        jobPostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job post not found"));
        jobPostRepository.deleteById(id);
    }
    public List<JobPost> getByUserId(Long userId) {
        return jobPostRepository.findByUserId(userId);
    }
}
