package springkpsgroup02.kps.Service.ServiceImplement;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import springkpsgroup02.kps.Exception.EmailAlreadyExistException;
import springkpsgroup02.kps.Exception.EmailNotRegisterException;
import springkpsgroup02.kps.Exception.ExpireOTPCodeException;
import springkpsgroup02.kps.Exception.NotFoundException;
import springkpsgroup02.kps.Model.DTO.Request.ProfileRequest;
import springkpsgroup02.kps.Model.DTO.Response.ProfileResponse;
import springkpsgroup02.kps.Model.Entity.EmailVerification;
import springkpsgroup02.kps.Model.Entity.Profile;
import springkpsgroup02.kps.Repository.EmailVerificationRepo;
import springkpsgroup02.kps.Repository.ProfileRepository;
import springkpsgroup02.kps.Service.AuthService;

import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthServiceImp implements AuthService {
    private final ProfileRepository profileRepository;
    private final EmailVerificationRepo emailVerificationRepo;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String adminEmail;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails userDetails = profileRepository.getUserByEmailOrUserName(username);
        if (userDetails == null) throw new UsernameNotFoundException("User Not Found with username: " + username);
        return userDetails;
    }

    @Override
    public ProfileResponse register(ProfileRequest profileRequest){
            try{
                Profile tempProfile = new Profile();
                tempProfile.setUsername(profileRequest.getUsername());
                tempProfile.setEmail(profileRequest.getEmail());
                tempProfile.setPassword(passwordEncoder.encode(profileRequest.getPassword()));
                tempProfile.setProfileImage(profileRequest.getProfileImage());
                tempProfile.setIsVerified(false);
                ProfileResponse profileResponse = profileRepository.registerUser(tempProfile);

                // Prepare Email
                var randomNumber =  new Random().nextInt(99999);
                EmailVerification emailVerification = new EmailVerification();
                emailVerification.setExpireTime(LocalTime.now().plusMinutes(5));
                emailVerification.setVerification(String.format("%06d",randomNumber));
                emailVerification.setUserId(profileResponse.getProfileId());

                // Insert Into EmailVerification
                emailVerificationRepo.insertUserVerify(emailVerification);

                    // Prepare Send Email To User
                    MimeMessage mimeMessage = javaMailSender.createMimeMessage();

                    MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,
                            MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                            StandardCharsets.UTF_8.name());


                    // Thymeleaf context setup
                    Context context = new Context();
                    context.setVariable("user", profileRequest.getUsername());
                    context.setVariable("otp", emailVerification.getVerification());

                    // Process the template
                    String htmlContent = templateEngine.process("otp-form", context);

                    mimeMessageHelper.setSubject("Email Verify Company name");
                    mimeMessageHelper.setTo(profileRequest.getEmail());
                    mimeMessageHelper.setFrom(adminEmail);

                    mimeMessageHelper.setText(htmlContent,true);
                    javaMailSender.send(mimeMessage);

                return profileResponse;
            } catch (Exception e) {
                throw new EmailAlreadyExistException("Email Already Exist! " + profileRequest.getEmail());
            }
    }

    @Override
    public String verify(String email, String OTP) {
            Profile emailUser = profileRepository.getUserByEmailOrUserName(email);
            if(emailUser == null) throw new NotFoundException("Email Not Register Yet");

            EmailVerification user = emailVerificationRepo.getUserVerifyById(emailUser.getId());
            if(LocalTime.now().isAfter(user.getExpireTime())){
                throw new ExpireOTPCodeException("Expire Code");
            }

            if(user.getVerification().equals(OTP)){
                profileRepository.updateProfileToVerify(emailUser.getId(),true);
                if(emailUser.getId().equals(user.getUserId())){
                    emailVerificationRepo.deleteOTPCodeByUserUUID(emailUser.getId());
                }
                return "Verify Success";
            }
            return "Wrong OTP";
    }

    @Override
    public String reSendOTP(String email) {
        Profile emailUser = profileRepository.getUserByEmailOrUserName(email);
        if(emailUser == null) throw new EmailNotRegisterException("Email Not Register Yet");

        EmailVerification user = emailVerificationRepo.getUserVerifyById(emailUser.getId());


        if(emailUser.getId().equals(user.getUserId())){
            emailVerificationRepo.deleteOTPCodeByUserUUID(emailUser.getId());
        }

        // Prepare Email
        var randomNumber = new Random().nextInt(99999);
        EmailVerification emailVerification = new EmailVerification();
        emailVerification.setExpireTime(LocalTime.now().plusMinutes(2));
        emailVerification.setVerification(String.format("%06d",randomNumber));
        emailVerification.setUserId(emailUser.getId());

        // Insert Into EmailVerification
        emailVerificationRepo.insertUserVerify(emailVerification);

        // Prepare Send Email To User
        try{
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();

            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());

            // Thymeleaf context setup
            Context context = new Context();
            context.setVariable("user", emailUser.getUsername());
            context.setVariable("otp", emailVerification.getVerification());

            // Process the template
            String htmlContent = templateEngine.process("otp-form", context);

            mimeMessageHelper.setSubject("Email Verify Company name");
            mimeMessageHelper.setTo(emailUser.getEmail());
            mimeMessageHelper.setFrom(adminEmail);
            mimeMessageHelper.setText(htmlContent,true);
            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            throw  new NotFoundException("Email Not Found!");
        }
        return "Resent";
    }
}
