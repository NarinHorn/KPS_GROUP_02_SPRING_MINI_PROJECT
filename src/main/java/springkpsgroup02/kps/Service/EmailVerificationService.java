package springkpsgroup02.kps.Service;

import springkpsgroup02.kps.Model.Entity.EmailVerification;

import java.util.UUID;

public interface EmailVerificationService {

    public EmailVerification getUserVerifyById(UUID uuid);
    public void deleteOTPCodeByUserUUID(UUID userId);
}
