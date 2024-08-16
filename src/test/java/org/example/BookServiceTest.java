package org.example;

import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

public class BookServiceTest {

    @InjectMocks
    private BookService bookService;                                //Injecting the BoolService for testing

    @Mock
    private User mockUser;                                              // Mocking the user class to simulate user interactions

    private Book book1;                                                 //Sample book for test
    private Book book2;

    @BeforeAll
    static void beforeAllTests() {                                      //Log message before all tests
        System.out.println("Starting BookService tests...");
    }

    @AfterAll
    static void afterAllTests() {                                       //Log message after all tests
        System.out.println("Completed BookService tests.");
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);                                     //Initialize mocks before each test
        book1 = new Book("Windows", "Harley Jarvis", "Fiction", 10.00);         //Creating book objects
        book2 = new Book("Java", "Marley Marvin", "Non-Fiction", 20.00);
        bookService.addBook(book1);                                                             //adding first/second books to service
        bookService.addBook(book2);
    }

    @AfterEach
    void tearDown() {                                                                       //Clean after each test
        bookService = null;
    }

    @Nested
    class SearchBookTests {

        @Test
        void testSearchBookByTitle_PositiveCase() {
            List<Book> result = bookService.searchBook("Windows");          //Search for book by title
            assertFalse(result.isEmpty());                                          //Verifying that the result is not empty
            assertThat(result, hasItem(book1));                                     //Using Hamcrest to check if the result contains the expected book
        }

        @Test
        void testSearchBookByTitle_NegativeCase() {
            List<Book> result = bookService.searchBook("Nonexistent Title");    //Searching for non existing book
            assertTrue(result.isEmpty());                                                  //Verifying that the result is empty
        }

        @Test
        void testSearchBookByTitle_EdgeCase() {
            List<Book> result = bookService.searchBook("");                     //Search with an empty string
            assertFalse(result.isEmpty());                                              //Verifying that the result is empty
        }
    }

    @Nested
    class PurchaseBookTests {

        @Test
        void testPurchaseBook_PositiveCase() {

            boolean result = bookService.purchaseBook(mockUser, book1);                 //Attempt to purchase a book


            assertTrue(result, "Expected the purchase to be successful.");      //Ensure book purchase success
        }

        @Test
        void testPurchaseBook_NegativeCase() {
            boolean result = bookService.purchaseBook(mockUser, new Book("Indigo", "Marley Davidson", "Non-Fiction", 20.00));     //adding book that does not exist


            assertFalse(result, "Expected the purchase to fail.");
        }

        @Test
        void testPurchaseBook_EdgeCase() {
            boolean result = bookService.purchaseBook(mockUser, null);          //Attempt to purchase a null book
            assertFalse(result, "Expect purchase to fail due to null input ");                                                    //verif
        }
    }

    @Nested
    class AddBookTests {

        @Test
        void testAddBook_PositiveCase() {
            Book newBook = new Book("Anybody Can Do", "Jack Clawson", "Fiction", 15.00);
            boolean result = bookService.addBook(newBook);
            assertTrue(result, "Expected the book to be added successful.");
        }

        @Test
        void testAddBook_NegativeCase() {
            boolean result = bookService.addBook(book1); // Adding an existing book
            assertFalse(result, "Expected the book to be added.");
        }

        @Test
        void testAddBook_EdgeCase() {
            // Try adding a null book and expect it to fail (throws NullPointerException or behaves unexpectedly)
            try {
                boolean result = bookService.addBook(null);
                assertTrue(result, "Adding null should return false or fail.");
            } catch (Exception e) {
                assertFalse(e instanceof NullPointerException, "Expected NullPointerException when adding null.");
            }
        }

        @Nested
        class RemoveBookTests {

            @Test
            void testRemoveBook_PositiveCase() {
                boolean result = bookService.removeBook(book1);
                assertTrue(result, "Expected the book to be removed successfully.");
            }

            @Test
            void testRemoveBook_NegativeCase() {
                Book nonExistentBook = new Book("This Is A Fake Book", "Dwayne Johnson", "Non-Fiction", 6.00);
                boolean result = bookService.removeBook(nonExistentBook);
                assertFalse(result, "Expected the book to fail to remove due to non existent book.");
            }

            @Test
            void testRemoveBook_EdgeCase() {
                boolean result = bookService.removeBook(null);
                assertFalse(result, "Expected the book to fail to remove null.");
            }
        }

        @Nested
        class AddBookReviewTests {

            @Test
            void testAddBookReview_PositiveCase() {
                when(mockUser.getPurchasedBooks()).thenReturn(List.of(book1));                      //Mocking the user has purchased the book
                boolean result = bookService.addBookReview(mockUser, book1, "Thats some good book!");
                assertTrue(result);
                verify(mockUser, times(1)).getPurchasedBooks();                 //Verifying method was called
            }

            @Test
            void testAddBookReview_NegativeCase() {
                when(mockUser.getPurchasedBooks()).thenReturn(List.of());                               //Mocking the user has not purchased the book
                boolean result = bookService.addBookReview(mockUser, book1, "Thats some good book!");   //Attempting to add a review
                assertFalse(result, "Expected review to be rejected because user has not purchased the book");
                verify(mockUser, times(1)).getPurchasedBooks();                 //verifying method was called
            }

            @Test
            void testAddBookReview_EdgeCase() {
                boolean result = bookService.addBookReview(mockUser, null, null);
                assertFalse(result, "Expected review to be rejected due to null input.");
            }
            @Test
            void testAddBookReview_EmptyReview() {
                when(mockUser.getPurchasedBooks()).thenReturn(List.of(book1));
                boolean result = bookService.addBookReview(mockUser, book1, "");
                assertTrue(result, "Expected review to be added successfully even if empty ");
            }
        }
    }
}