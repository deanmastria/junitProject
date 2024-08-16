package org.example;

import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;                                    // Injecting the UserService class to test

    @Mock
    private User mockUser;                                              // Mocking the User class to simulate user interactions

    private User user1;                                                 // Sample user for testing
    private User user2;

    @BeforeAll
    static void beforeAllTests() {                                      // Log message before all tests
        System.out.println("Starting UserService tests...");
    }

    @AfterAll
    static void afterAllTests() {
        System.out.println("Completed UserService tests.");             // Log message after all tests
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);                                                 // Initialize mocks before each test
        user1 = new User("user1", "password1", "user1@example.com");        // Creating a user object
        user2 = new User("user2", "password2", "user2@example.com");        // Creating another user object
        userService.registerUser(user1);                                                            // Registering the first user
    }

    @AfterEach
    void tearDown() {
        userService = null;                                                                         // Clean up after each test
    }

    @Nested
    class RegisterUserTests {

        @Test
        void testRegisterUser_PositiveCase() {
            boolean result = userService.registerUser(user2);                                       // Attempting to register a new user
            assertTrue(result, "Expected the user to be registered successfully.");
        }

        @Test
        void testRegisterUser_NegativeCase() {
            when(mockUser.getUsername()).thenReturn("user1");                                                   // Mocking the username of the existing user
            boolean result = userService.registerUser(mockUser);                                                // Trying to register the same user again
            assertFalse(result, "Expected the registration to fail since the user already exists.");    // Verifying registration failure
        }

        @Test
        void testRegisterUser_EdgeCase() {
            when(mockUser.getUsername()).thenReturn("");                                                            // Mocking an empty username
            boolean result = userService.registerUser(mockUser);                                                        // Trying to register the user with empty fields
            assertTrue(result, "Expected the user to be registered successfully, even with empty fields.");     // Verifying successful registration
        }
    }

    @Nested
    class LoginUserTests {

        @Test
        void testLoginUser_PositiveCase() {
            when(mockUser.getUsername()).thenReturn("user1");                                           // Mocking the correct username
            when(mockUser.getPassword()).thenReturn("password1");                                       // Mocking the correct password

            User result = userService.loginUser("user1", "password1");                  // Attempting to log in with correct credentials
            assertNotNull(result, "Expected the login to be successful.");                          // Verifying successful login
        }

        @Test
        void testLoginUser_NegativeCase() {
            when(mockUser.getUsername()).thenReturn("user1");                                   // Mocking the correct username
            when(mockUser.getPassword()).thenReturn("password1");                               // Mocking the correct password

            User result = userService.loginUser("user1", "wrongpassword");       // Attempt to log in with an incorrect password
            assertNull(result, "Expected the login to fail due to incorrect password.");    // Verifying login failure
        }

        @Test
        void testLoginUser_EdgeCase() {
            User result = userService.loginUser("", "");                            // Attempting to log in with empty credentials
            assertNull(result, "Expected the login to fail due to empty credentials.");     // Verifying login failure
        }
    }

    @Nested
    class UpdateUserProfileTests {

        @Test
        void testUpdateUserProfile_PositiveCase() {
            when(mockUser.getUsername()).thenReturn("user1");                               // Mocking the correct username
            when(mockUser.getPassword()).thenReturn("password1");                           // Mocking the correct password

            boolean result = userService.updateUserProfile(user1, "newUsername", "newPassword", "newEmail@example.com");        // Attempting to update the profile with new details
            assertTrue(result, "Expected the user profile to be updated successfully.");                                                                // Verifying successful profile update
        }
        }

        @Test
        void testUpdateUserProfile_NegativeCase() {
            when(mockUser.getUsername()).thenReturn("user2");
            userService.registerUser(user2);                                                                                                            // Registering a new user to create a conflict
            boolean result = userService.updateUserProfile(user1, "user2", "newPassword", "newEmail@example.com");      // Attempting to update the profile with an existing username
            assertFalse(result, "Expected the update to fail since the new username is already taken.");                                        // Verifying update failure
        }

        @Test
        void testUpdateUserProfile_EdgeCase() {
            boolean result = userService.updateUserProfile(user1, "", "", "");                      // Attempting to update the profile with empty fields
            assertTrue(result, "Expected the user profile to be updated even with empty fields.");                          // Verifying successful profile update
        }
        @Test
        void testUpdateUserProfile_NullFields() {
            boolean result = userService.updateUserProfile(user1, null, null, null);  // Attempting to update the profile with null fields
            assertTrue(result, "Expected the user profile to be updated even with null fields.");  // Verifying successful profile update
        }
    }


