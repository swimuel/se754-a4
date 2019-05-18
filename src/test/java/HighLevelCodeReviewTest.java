// import static org.junit.Assert.assertEquals;

// import org.junit.*;
// import org.mockito.Mockito;

// public class HighLevelCodeReviewTest {


//     NonDeveloper nonDeveloper;
//     Review review;
//     Results results;
//     User reviewAuthor;
    

//     @Before
//     public void setup() {
//         nonDeveloper = Mockito.mock(NonDeveloper.class);
//         results = Mockito.mock(Results.class);
//         reviewAuthor = new User(true);
//         review = new Review(results, reviewAuthor, Mockito.mock(Database.class));
//         review.setReviewers(developer, nonDeveloper);
//     }

//     @Test
//     public void sendAutomatedReview() {
//         Mockito.doReturn(results).when(nonDeveloper).fetchResults();
//         review.sendAutomatedResults(results);
//         Results recievedReviewResults = nonDeveloper.fetchResults(); 
//         assertEquals(results, recievedReviewResults);
//     }
// }