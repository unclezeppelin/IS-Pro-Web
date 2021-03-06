package ua.in.usv.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

import ua.in.usv.entity.root.CustomUser;
import ua.in.usv.helper.Byte2String;
import ua.in.usv.service.UserService;
import ua.in.usv.stay.Md5HashEncoder;
import ua.in.usv.stay.PasswordBlock;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserService userService;

    @Autowired
    public CustomAuthenticationProvider(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userName = authentication.getName();
        String password = authentication.getCredentials().toString();

        CustomUser customUser = userService.findByLogin(userName);

        if (customUser == null) {
            logger.info("User not found: " + userName);
            return null;
        }

        /// custom authentication by password's hash
        if (!passwordsHashAuthentication(customUser, password)) {
            logger.info("Wrong password for user: " + userName);
            return null;
        }

        Set<GrantedAuthority> roles = new HashSet<>();
        roles.add(new SimpleGrantedAuthority(customUser.getRole().toString()));

        return new UsernamePasswordAuthenticationToken(userName, password, roles);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    private boolean passwordsHashAuthentication(CustomUser customUser, String password) {
        PasswordBlock passwordBlock = new PasswordBlock(customUser.getUserPassword().getPasswordBlob());
        String hashFromBase = passwordBlock.getPasswordHash();

        Md5HashEncoder md = new Md5HashEncoder();
        long salt = passwordBlock.getSalt();
        long key = customUser.getId();
        byte[] output = new byte[Md5HashEncoder.digest_len];
        md.generateHash(key, password, salt, output);
        String hashFromPass = Byte2String.encode(output);

        return hashFromPass.equals(hashFromBase);
    }
}

