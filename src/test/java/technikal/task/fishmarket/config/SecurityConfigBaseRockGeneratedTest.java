package technikal.task.fishmarket.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.atLeast;

@Timeout(10)
class SecurityConfigBaseRockGeneratedTest {

    @Mock
    private HttpSecurity httpSecurity;

    @Mock
    private DefaultSecurityFilterChain defaultSecurityFilterChain;

    private SecurityConfig securityConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        securityConfig = new SecurityConfig();
    }

    @Test
    void testUserDetailsService() {
        UserDetailsService result = securityConfig.userDetailsService();
        assertNotNull(result);
        assertThat(result, instanceOf(InMemoryUserDetailsManager.class));
        InMemoryUserDetailsManager userDetailsManager = (InMemoryUserDetailsManager) result;
        UserDetails adminUser = userDetailsManager.loadUserByUsername("admin");
        assertNotNull(adminUser);
        assertEquals("admin", adminUser.getUsername());
        assertTrue(adminUser.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
        UserDetails regularUser = userDetailsManager.loadUserByUsername("user");
        assertNotNull(regularUser);
        assertEquals("user", regularUser.getUsername());
        assertTrue(regularUser.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
        assertTrue(adminUser.getPassword().startsWith("$2"));
        assertTrue(regularUser.getPassword().startsWith("$2"));
    }

    @Test
    void testFilterChain() throws Exception {
        SecurityConfig spyConfig = spy(securityConfig);
        PasswordEncoder mockPasswordEncoder = mock(BCryptPasswordEncoder.class);
        doReturn(mockPasswordEncoder).when(spyConfig).passwordEncoder();
        doReturn(httpSecurity).when(httpSecurity).authorizeHttpRequests(any());
        doReturn(httpSecurity).when(httpSecurity).formLogin(any());
        doReturn(httpSecurity).when(httpSecurity).logout(any());
        doReturn(httpSecurity).when(httpSecurity).csrf(any());
        doReturn(defaultSecurityFilterChain).when(httpSecurity).build();
        SecurityFilterChain result = spyConfig.filterChain(httpSecurity);
        assertNotNull(result);
        assertThat(result, instanceOf(DefaultSecurityFilterChain.class));
        verify(httpSecurity, atLeast(1)).authorizeHttpRequests(any());
        verify(httpSecurity, atLeast(1)).formLogin(any());
        verify(httpSecurity, atLeast(1)).logout(any());
        verify(httpSecurity, atLeast(1)).csrf(any());
        verify(httpSecurity, atLeast(1)).build();
    }

    @Test
    void testPasswordEncoder() {
        PasswordEncoder result = securityConfig.passwordEncoder();
        assertNotNull(result);
        assertThat(result, instanceOf(BCryptPasswordEncoder.class));
    }

    @Test
    void testPasswordEncoderFunctionality() {
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        String rawPassword = "testPassword";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        assertNotNull(encodedPassword);
        assertTrue(encodedPassword.startsWith("$2"));
        assertTrue(encodedPassword.length() > rawPassword.length());
    }

    @Test
    void testUserDetailsServicePasswordEncoding() {
        SecurityConfig spyConfig = spy(securityConfig);
        PasswordEncoder mockPasswordEncoder = mock(BCryptPasswordEncoder.class);
        String encodedAdminPassword = "$2a$10$encodedAdminPassword";
        String encodedUserPassword = "$2a$10$encodedUserPassword";
        doReturn(encodedAdminPassword).when(mockPasswordEncoder).encode("admin");
        doReturn(encodedUserPassword).when(mockPasswordEncoder).encode("user");
        doReturn(mockPasswordEncoder).when(spyConfig).passwordEncoder();
        UserDetailsService result = spyConfig.userDetailsService();
        assertNotNull(result);
        verify(mockPasswordEncoder, atLeast(1)).encode("admin");
        verify(mockPasswordEncoder, atLeast(1)).encode("user");
        InMemoryUserDetailsManager userDetailsManager = (InMemoryUserDetailsManager) result;
        UserDetails adminUser = userDetailsManager.loadUserByUsername("admin");
        UserDetails regularUser = userDetailsManager.loadUserByUsername("user");
        assertEquals(encodedAdminPassword, adminUser.getPassword());
        assertEquals(encodedUserPassword, regularUser.getPassword());
    }

    @Test
    void testSecurityConfigInstantiation() {
        SecurityConfig config = new SecurityConfig();
        assertNotNull(config);
    }

    @Test
    void testMultiplePasswordEncoderCalls() {
        String password1 = "password1";
        String password2 = "password2";
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        String encoded1 = passwordEncoder.encode(password1);
        String encoded2 = passwordEncoder.encode(password2);
        assertNotNull(encoded1);
        assertNotNull(encoded2);
        assertTrue(encoded1.startsWith("$2"));
        assertTrue(encoded2.startsWith("$2"));
        String encoded1Again = passwordEncoder.encode(password1);
        assertTrue(encoded1Again.startsWith("$2"));
    }

    @Test
    void testUserDetailsServiceMultipleCalls() {
        UserDetailsService service1 = securityConfig.userDetailsService();
        UserDetailsService service2 = securityConfig.userDetailsService();
        assertNotNull(service1);
        assertNotNull(service2);
        assertThat(service1, instanceOf(InMemoryUserDetailsManager.class));
        assertThat(service2, instanceOf(InMemoryUserDetailsManager.class));
    }

    @Test
    void testFilterChainWithNullHttpSecurity() throws Exception {
        SecurityConfig spyConfig = spy(securityConfig);
        PasswordEncoder mockPasswordEncoder = mock(BCryptPasswordEncoder.class);
        doReturn(mockPasswordEncoder).when(spyConfig).passwordEncoder();
        try {
            spyConfig.filterChain(null);
        } catch (Exception e) {
            assertThat(e, instanceOf(Exception.class));
        }
    }
}
