package com.example.Rider_Co.tests.serviceTests;

import com.example.Rider_Co.models.ApiKeyManager;
import com.example.Rider_Co.models.User;
import com.example.Rider_Co.repositories.ApiKeyRepository;
import com.example.Rider_Co.repositories.UserRepository;
import com.example.Rider_Co.services.ApiKeyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class ApiKeyServiceTest {

    @InjectMocks
    private ApiKeyService apiKeyService;

    @Mock
    private ApiKeyRepository apiKeyRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    private User mockUser;

    @BeforeEach
    public void setUp() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn("testUser");
        mockUser = new User();
        mockUser.setId(1);
        mockUser.setUsername("testUser");
    }

    @Test
    public void testCreateApiKey_whenUserNotFound() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.empty());
        String result = apiKeyService.createApiKey();
        assertEquals("user not found .", result);
    }

    @Test
    public void testCreateApiKey_whenUserHasThreeKeys() {
        List<ApiKeyManager> keys = Arrays.asList(new ApiKeyManager(), new ApiKeyManager(), new ApiKeyManager());
        mockUser.setApiKeyManager(keys);
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(mockUser));
        String result = apiKeyService.createApiKey();
        assertTrue(result.contains("A user can have maximum of 3 keys"));
    }

    @Test
    public void testCreateApiKey_whenUserCanCreateKey() {
        mockUser.setApiKeyManager(new ArrayList<>());
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(mockUser));
        when(apiKeyRepository.save(any(ApiKeyManager.class))).thenAnswer(i -> i.getArguments()[0]);
        String result = apiKeyService.createApiKey();
        assertTrue(result.contains("Your API Key is"));
    }

    @Test
    public void testGetApiKeysList_whenUserExists() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(mockUser));
        List<ApiKeyManager> mockKeys = List.of(new ApiKeyManager(), new ApiKeyManager());
        when(apiKeyRepository.findByUserId(1)).thenReturn(mockKeys);
        List<ApiKeyManager> result = apiKeyService.getApiKeysList();
        assertEquals(2, result.size());
    }

    @Test
    public void testDeleteApiKey_whenKeyBelongsToUser() {
        ApiKeyManager key = new ApiKeyManager();
        key.setUser(mockUser);
        key.setApiKeyIdentifier("...............123456");
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(mockUser));
        when(apiKeyRepository.findByApiKeyIdentifier("...............123456")).thenReturn(Optional.of(key));
        String result = apiKeyService.deleteApiKey("123456");
        verify(apiKeyRepository).delete(key);
        assertTrue(result.contains("is deleted successfully"));
    }

    @Test
    public void testDeleteApiKey_whenKeyDoesNotBelongToUser() {
        User otherUser = new User();
        otherUser.setId(2);
        ApiKeyManager key = new ApiKeyManager();
        key.setUser(otherUser);
        key.setApiKeyIdentifier("...............abcdef");
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(mockUser));
        when(apiKeyRepository.findByApiKeyIdentifier("...............abcdef")).thenReturn(Optional.of(key));
        String result = apiKeyService.deleteApiKey("abcdef");
        verify(apiKeyRepository, never()).delete(any());
        assertTrue(result.contains("you don't have credentials"));
    }
}
