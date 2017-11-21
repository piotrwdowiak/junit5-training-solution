package com.piotrwdowiak.junitTraining;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoInitializer.class)
@ExtendWith(TimeoutExtension.class)
public class BookServiceTest {

    @Mock
    BookDAO bookDAO;

    @Test
    @DisplayName("Test get book by id")
    @Timeout
    public void testGetBookById() {
        Mockito.when(bookDAO.getBookById("1")).thenReturn(new Book("1", "Potop", "Henryk Sienkiewicz"));
        Mockito.when(bookDAO.getBookById("100")).thenThrow(new NoSuchBookException());

        BookService bookService = new BookService(bookDAO);
        Book book = bookService.getBookById("1");

        assertEquals(book.getAuthor(), "Henryk Sienkiewicz");
        assertEquals(book.getTitle(), "Potop");

        assertThrows(NoSuchBookException.class, () -> {
            bookService.getBookById("100");
        });
    }

    @Test
    @DisplayName("Test add book")
    @Timeout
    public void testAddBook() {
        Book potop = new Book("1", "Potop", "Henryk Sienkiewicz");
        Mockito.when(bookDAO.addBook(potop)).thenReturn(true);
        BookService bookService = new BookService(bookDAO);
        assertTrue(bookService.addBook(potop));
    }

    @Test
    @DisplayName("Test get all books")
    @Timeout
    public void testGetAddBooks() {
        Book potop = new Book("1", "Potop", "Henryk Sienkiewicz");
        Book dzuma = new Book("2", "Dzuma", "Albert Camus");

        List<Book> mockList = new ArrayList<>();
        mockList.add(potop);
        mockList.add(dzuma);

        Mockito.when(bookDAO.getBooks()).thenReturn(mockList);

        BookService bookService = new BookService(bookDAO);
        List testList = bookService.getBooks();

        assertEquals(2, testList.size());
        assertEquals(testList.get(0), potop);
        assertEquals(testList.get(1), dzuma);
    }

    @Test
    @Tag("slow")
    @RepeatedTest(10)
    @Timeout
    public void testBookServiceOnMultipleThreads() {
        Mockito.when(bookDAO.getBooks()).thenReturn(new ArrayList<>());

        new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                BookService bookService = new BookService(bookDAO);
                assertNotNull(bookService.getBooks());
            }
        }).start();

        new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                BookService bookService = new BookService(bookDAO);
                assertNotNull(bookService.getBooks());
            }
        }).start();
    }
}