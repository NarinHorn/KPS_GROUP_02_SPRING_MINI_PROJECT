package springkpsgroup02.kps.Service.ServiceImplement;

import org.springframework.stereotype.Service;
import springkpsgroup02.kps.Model.Entity.EmailVerification;
import springkpsgroup02.kps.Repository.EmailVerificationRepo;
import springkpsgroup02.kps.Service.EmailVerificationService;

import java.util.UUID;

@Service
public class EmailVerificationImp  implements EmailVerificationService {
    private EmailVerificationRepo emailVerificationRepo;
    @Override
    public EmailVerification getUserVerifyById(UUID uuid) {
        return emailVerificationRepo.getUserVerifyById(uuid);

    }

    @Override
    public void deleteOTPCodeByUserUUID(UUID userId) {
        emailVerificationRepo.deleteOTPCodeByUserUUID(userId);
    }

}
