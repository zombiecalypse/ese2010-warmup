package ch.zombiecalypse.MyStackOverflow.Tests;


import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import ch.zombiecalypse.MyStackOverflow.Answer;
import ch.zombiecalypse.MyStackOverflow.Content;
import ch.zombiecalypse.MyStackOverflow.MissingArgument;
import ch.zombiecalypse.MyStackOverflow.Question;
import ch.zombiecalypse.MyStackOverflow.User;
import ch.zombiecalypse.MyStackOverflow.Vote;

public class Requirements {
	private User marvin;
	private Question question;
	private Answer answer;
	private Date now = new Date(1285325291);
	private User chuck;

	@Before
	public void setUp() throws Exception {
		marvin = new User("Marvin");
		question = marvin.askQuestion(now,"Hi, I got a problem with Java...");
		answer = marvin.answerQuestion(question,now,"Nevermind");
		chuck = new User("Chuck");
	}
	
	//Requirement 1
	@Test
	public void shouldGetName(){
		assertEquals(marvin.getName(),"Marvin");
	}
	
	@Test(expected=MissingArgument.class)
	public void shouldNotAllowEmptyString() throws Exception {
		new User("");
	}
	
	@Test(expected=MissingArgument.class)
	public void shouldNotAllowNullName() throws Exception {
		new User(null);
	}
	
	//Req 2
	@Test
	public void shouldGetOwnerOfQuestion(){
		assertEquals(question.getOwner(),marvin);
	}
	
	@Test(expected=MissingArgument.class)
	public void shouldNotAllowNullOwnersInQuestions() throws MissingArgument{
		new Question(null, "L33t H4x0R");
	}
	
	//Req 3
	@Test
	public void shouldGetOwnerAndQuestionOfAnswer(){
		assertEquals(answer.getOwner(),marvin);
		assertEquals(answer.getQuestion(),question);
	}
	
	@Test(expected=MissingArgument.class)
	public void shouldNotAllowNullOwnersInAnswers() throws MissingArgument{
		new Answer(question, null, "Bow before me for I am null");
	}
	
	@Test(expected=MissingArgument.class)
	public void shouldNotAllowNullQuestionInAnswers() throws MissingArgument{
		new Answer(null, marvin, "Um...Hello?");
	}
	
	// Req 4
	
	@Test
	public void shouldGetTimestampAndContentOfUmmContent(){
		assertEquals(answer.getTimestamp(),now);
		assertEquals(question.getTimestamp(),now);
		assertEquals(answer.getText(),"Nevermind");
		assertEquals(question.getText(),"Hi, I got a problem with Java...");
	}
	
	@Test(expected=MissingArgument.class)
	public void shouldNotAllowNullTextsInContent() throws MissingArgument{
		marvin.answerQuestion(question, null);
	}
	@Test(expected=MissingArgument.class)
	public void shouldNotAllowNullTimeStampsInContent() throws MissingArgument{
		marvin.answerQuestion(question, null,"I've been here since before the dawn of time");
	}
	
	// Req 5: Finally something new
	
	@Test
	public void shouldNotHaveVotesAfterCreation() {
		assertEquals(0,question.getRating());
	}
	@Test
	public void shouldVoteUpCorrectly(){
		
		question.vote(Vote.UP,marvin);
		assertEquals(1,question.getRating());
		
		// No double votes
		question.vote(Vote.UP,marvin);
		assertEquals(1,question.getRating());
	}
	
	@Test
	public void shouldVoteDownCorrectly(){
		question.vote(Vote.DOWN,marvin);
		assertEquals(-1,question.getRating());
		
		// No double votes
		question.vote(Vote.DOWN,marvin);
		assertEquals(-1,question.getRating());
	}
	
	@Test
	public void shouldVoteUpAndDownCorrectly() throws MissingArgument{
		
		
		question.vote(Vote.DOWN,marvin);
		
		// Can Change
		question.vote(Vote.UP,marvin);
		assertEquals(1,question.getRating());
		
		question.vote(Vote.UP, chuck);
		assertEquals(2,question.getRating());
		
		question.vote(Vote.DOWN, chuck);

		assertEquals(0,question.getRating());
	}
	
	// Req 6
	
	@Test
	public void shouldDeleteAnswer() throws MissingArgument{
		Answer chucksAnswer = chuck.answerQuestion(question, now, "Shut up");
		assertTrue(question.getAnswers().contains(chucksAnswer));
		chuck.delete();
		assertFalse(question.getAnswers().contains(chucksAnswer));
	}
	
	@Test
	public void shouldDeleteVote() {
		marvin.vote(Vote.DOWN,question);
		chuck.vote(Vote.DOWN,question);
		assertEquals(-2,question.getRating());
		chuck.delete();
		assertEquals(-1,question.getRating());
	}
}
