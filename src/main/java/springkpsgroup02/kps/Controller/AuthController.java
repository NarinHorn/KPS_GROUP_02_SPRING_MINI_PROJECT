package springkpsgroup02.kps.Controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import springkpsgroup02.kps.Exception.NotFoundException;
import springkpsgroup02.kps.Jwt.JwtUtils;
import springkpsgroup02.kps.Model.DTO.Request.AuthRequest;
import springkpsgroup02.kps.Model.DTO.Request.ProfileRequest;
import springkpsgroup02.kps.Model.DTO.Response.ApiResponse;
import springkpsgroup02.kps.Model.DTO.Response.BaseResponse;
import springkpsgroup02.kps.Model.DTO.Response.ProfileResponse;
import springkpsgroup02.kps.Model.DTO.Response.TokenResponse;
import springkpsgroup02.kps.Model.Entity.Profile;
import springkpsgroup02.kps.Service.AuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
public class AuthController extends BaseResponse {
    private final AuthenticationManager authManager;
    private final JwtUtils jwtUtils;
    private final AuthService authService;

    @PostMapping("/login")
    @Operation( summary = "User Login")
    public TokenResponse login(@RequestBody AuthRequest authRequest){
        TokenResponse tokenResponse = new TokenResponse();
        String identifier = authRequest.getIdentifier();
        String password = authRequest.getPassword();

        UserDetails profile = authService.loadUserByUsername(identifier);

        if(!profile.isEnabled()) throw new NotFoundException("Account Not Verify!");

        try {
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(identifier, password)
            );
            tokenResponse.setToken(jwtUtils.generateToken(identifier));
            return tokenResponse;
        } catch (Exception e) {
            throw new NotFoundException("Wrong Password Or Email");
        }
    }

    @PostMapping("/register")
    @Operation( summary = "Register New User")
    public ResponseEntity<ApiResponse<ProfileResponse>> register(@RequestBody @Valid ProfileRequest profileRequest){
        return responseEntity(true,"Create Success", HttpStatus.CREATED,authService.register(profileRequest));
    }

    @PostMapping("/verify")
    @Operation( summary = "Verify email with OTP")
    public ResponseEntity<ApiResponse<String>> verifyOTP(@RequestParam String email, @RequestParam String OTP){
        return responseEntity(true,"Verify Success", HttpStatus.ACCEPTED,authService.verify(email,OTP));
    }

    @PostMapping("/resend")
    @Operation( summary = "Resend Verified OTP")
    public ResponseEntity<ApiResponse<String>> resentOTP(@RequestParam String email){
        return responseEntity(true,"Resend Success", HttpStatus.OK,authService.reSendOTP(email));
    }

}
