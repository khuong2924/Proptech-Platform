package khuong.com.lasttermjava;

import khuong.com.lasttermjava.dto.TransactionDTO;
import khuong.com.lasttermjava.entity.JobPost;
import khuong.com.lasttermjava.entity.Transaction;
import khuong.com.lasttermjava.repository.JobPostRepository;
import khuong.com.lasttermjava.repository.TransactionRepository;
import khuong.com.lasttermjava.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private JobPostRepository jobPostRepository;

    @InjectMocks
    private TransactionService transactionService;

    private Transaction transaction;
    private TransactionDTO transactionDTO;
    private JobPost jobPost;

    @BeforeEach
    void setUp() {
        jobPost = new JobPost();
        jobPost.setId(1L);

        transaction = new Transaction();
        transaction.setId(1L);
        transaction.setJobPost(jobPost);
        transaction.setNoiDung("Test Content");
        transaction.setSdtKhachHang("1234567890");
        transaction.setLoaiHoSo("Test Type");
        transaction.setTrangThaiGiaoDich(true);
        transaction.setGiayToPhapLy(Set.of("Legal Paper 1"));
        transaction.setHopDongMuaBan(Set.of("Contract 1"));
        transaction.setTrangThaiThanhToan(true);
        transaction.setHopDongThue(Set.of("Lease Contract"));
        transaction.setTienThue(BigDecimal.valueOf(1000.00));
        transaction.setNgayTraDinhKy("2024-12-01");

        transactionDTO = new TransactionDTO(
                1L,
                1L,
                "Test Content",
                "1234567890",
                "Test Type",
                true,
                Set.of("Legal Paper 1"),
                Set.of("Contract 1"),
                true,
                Set.of("Lease Contract"),
                BigDecimal.valueOf(1000.00),
                "2024-12-01"
        );
    }

    @Test
    void testGetAll() {
        when(transactionRepository.findAll()).thenReturn(List.of(transaction));

        var result = transactionService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(transactionDTO.getId(), result.get(0).getId());
    }

    @Test
    void testCreateTransaction() {
        when(jobPostRepository.findById(transactionDTO.getJobPostId())).thenReturn(Optional.of(jobPost));

        transactionService.create(transactionDTO);

        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void testUpdateTransaction() {
        when(transactionRepository.findById(transactionDTO.getId())).thenReturn(Optional.of(transaction));
        when(jobPostRepository.findById(transactionDTO.getJobPostId())).thenReturn(Optional.of(jobPost));

        transactionService.update(transactionDTO.getId(), transactionDTO);

        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void testGetById() {
        when(transactionRepository.findById(transactionDTO.getId())).thenReturn(Optional.of(transaction));

        var result = transactionService.getById(transactionDTO.getId());

        assertNotNull(result);
        assertEquals(transactionDTO.getId(), result.getId());
    }

    @Test
    void testDeleteTransaction() {
        when(transactionRepository.findById(transactionDTO.getId())).thenReturn(Optional.of(transaction));

        transactionService.delete(transactionDTO.getId());

        verify(transactionRepository, times(1)).deleteById(transactionDTO.getId());
    }

    @Test
    void testDeleteTransaction_NotFound() {
        when(transactionRepository.findById(transactionDTO.getId())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            transactionService.delete(transactionDTO.getId());
        });

        assertEquals("Transaction not found", exception.getMessage());
    }

    @Test
    void testUpdateTransaction_NotFound() {
        when(transactionRepository.findById(transactionDTO.getId())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            transactionService.update(transactionDTO.getId(), transactionDTO);
        });

        assertEquals("Transaction not found", exception.getMessage());
    }

    @Test
    void testGetById_NotFound() {
        when(transactionRepository.findById(transactionDTO.getId())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            transactionService.getById(transactionDTO.getId());
        });

        assertEquals("Transaction not found", exception.getMessage());
    }
}
