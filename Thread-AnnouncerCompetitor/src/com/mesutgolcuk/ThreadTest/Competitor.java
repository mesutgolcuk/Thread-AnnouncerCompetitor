package com.mesutgolcuk.ThreadTest;

import java.util.Random;


public class Competitor implements Runnable {
	
	public final int QUESTIONPOINT = 5;
	private boolean isAnswered;
	String[] competitorAnswers;
	int i;
	int score;
	/**
	 * constructor for competitor class
	 * @param competitorAnswers competitors answers for each question
	 * @param i competitor index
	 */
	public Competitor(String[] competitorAnswers,int i) {
		this.i = i;
		isAnswered = false;
		this.competitorAnswers = competitorAnswers;
		score=0;
	}
	/**
	 * Thread's run method
	 */
	public void run() {
		while(true){
			
			synchronized (competitorAnswers) {
				if( isAnswered ){
					try {
						competitorAnswers.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				else{
					Random rand = new Random();
					boolean tf = rand.nextBoolean();
					if( tf == true )
						competitorAnswers[i] = "true";
					else
						competitorAnswers[i] = "false";
					isAnswered = true;
					competitorAnswers.notifyAll();
				}
			}
			
		}

	}
	/**
	 * Get score
	 * @return score
	 */
	public int getScore() {
		return score;
	}
	/**
	 * increase the score of competitor as a point of question
	 */
	public void increaseScore(){
		score += QUESTIONPOINT;
	}
	/**
	 * decrease the score of competitor as a point of question
	 */
	public void decreaseScore(){
		if( score > 0){
			score -= QUESTIONPOINT;
		}
	}
	/**
	 * sets the answered situation of competitor false
	 */
	public void setIsAnswered(){
		isAnswered = false;
	}


}
