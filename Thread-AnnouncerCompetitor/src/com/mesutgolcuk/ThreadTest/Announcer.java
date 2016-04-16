package com.mesutgolcuk.ThreadTest;

import java.util.ArrayList;

public class Announcer implements Runnable {
	
	ArrayList<String> questions;
	ArrayList<String> answers;
	String[] competitorAnswers;
	ArrayList<Competitor> al;
	ArrayList<Thread> tAl;
	int competitorNumber;
	/**
	 * constructor for Announcer clas
	 * @param questions arraylist that keeps questions
	 * @param answers arraylist that keep answers of questions
	 * @param competitorAnswers array of competitors answers
	 * @param competitorNumber number of competitors
	 */
	public Announcer(ArrayList<String> questions,ArrayList<String> answers,String[] competitorAnswers,int competitorNumber){
		this.questions = questions;
		this.competitorAnswers = competitorAnswers;
		this.competitorNumber = competitorNumber;
		this.answers = answers;
		al = new ArrayList<>();
		tAl = new ArrayList<>();
		for( int i=0;i<competitorNumber;i++){
			Competitor competitor = new Competitor(competitorAnswers,i);
			al.add(competitor);
			tAl.add(new Thread(competitor));
			tAl.get(i).start();
		}
	}
	/**
	 * Thread's run method
	 */
	public void run() {

		int i = 0;
		while( i < questions.size() ){
			synchronized (competitorAnswers) {
				if( !isAllAnswered() ){
					try {
						competitorAnswers.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				else{
					System.out.println("---------------------------------------------------");
					System.out.println(i+1+".Question: "+ questions.get(i));
					System.out.println("---------------------------------------------------");
					nextTurn(i);
					for(int j=0;j<al.size();j++){
						al.get(j).setIsAnswered();
					}
					i++;
					resetAnswers();
					competitorAnswers.notifyAll();
				}

			}

		}
		announceScores();
		System.exit(1);
	}
	/**
	 * checks for everyone is answered question
	 * @return everyone answered or not
	 */
	public boolean isAllAnswered(){
		for(int j=0;j<competitorAnswers.length;j++){
			if( competitorAnswers[j].equalsIgnoreCase("0"))
				return false;
		}
		return true;
	}
	/**
	 * resets the answers
	 */
	public void resetAnswers(){
		for( int j=0; j<competitorAnswers.length;j++){
			competitorAnswers[j] = "0";
		}
	}
	/**
	 * does necessary things to go next question
	 * @param i index of question
	 */
	public void nextTurn( int i){
		
		System.out.println("CORRECT ANSWER --> "+answers.get(i));
		
		for( int j=0;j<competitorAnswers.length;j++){
			
			System.out.println((j+1)+". competitor's answer is-->"+competitorAnswers[j]);
			if( competitorAnswers[j].equalsIgnoreCase(answers.get(i))){
				
				if(tAl.get(j).getPriority() == Thread.MAX_PRIORITY ){
					tAl.get(j).setPriority(Thread.NORM_PRIORITY);
				}
				else{
					tAl.get(j).setPriority(tAl.get(j).getPriority()+1);
				}
				al.get(j).increaseScore();
				
			}
			else{
				al.get(j).decreaseScore();
			}
			
		}	
		
	}
	/**
	 * prints each competitor's score
	 */
	public void announceScores(){
		System.out.println();
		System.out.println("CONTEST IS OVER------ RESULTS :");
		for( int j=0; j<competitorNumber;j++){
			System.out.println((j+1)+". competitor's score is -->"+al.get(j).getScore());
		}
		System.out.println("Winner is: "+(findWinner()+1)+" . competitor");
	}
	/**
	 * finds the competitor which has highest score
	 * @return index of competitor with highest score
	 */
	public int findWinner(){
		int max = Integer.MIN_VALUE;
		int maxIndex=0;
		for( int j=0; j<competitorNumber;j++){
			if(al.get(j).getScore() > max){
				max = al.get(j).getScore();
				maxIndex = j;
			}		
		}
		return maxIndex;
	}

}
